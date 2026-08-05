package com.bruno.taskflow_api.shared.infrastructure.outbox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)   // <- clave: NO reemplaces Postgres por H2
@Testcontainers
@Tag("integration")
class OutboxEventRepositoryTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  @Autowired
  OutboxEventRepository repository;

  @Autowired
  TransactionTemplate transactionTemplate;

  @Test
  @Transactional
  void devuelveSoloPendientesOrdenadosPorFecha() {
    OutboxEvent viejo = OutboxEvent.of("TASK", UUID.randomUUID(), "TASK_COMPLETED", "{}");
    OutboxEvent nuevo = OutboxEvent.of("TASK", UUID.randomUUID(), "TASK_COMPLETED", "{}");
    OutboxEvent yaProcesado = OutboxEvent.of("TASK", UUID.randomUUID(), "TASK_COMPLETED", "{}");
    yaProcesado.markAsProcessed();

    repository.saveAll(List.of(viejo, nuevo, yaProcesado));

    List<OutboxEvent> pendientes = repository.lockNextPendingBatch(10);

    assertThat(pendientes).extracting(OutboxEvent::getId)
        .containsExactly(viejo.getId(), nuevo.getId()); // orden por created_at, sin el ya procesado
  }

  @Test
  void unaTransaccionNoVeLasFilasQueOtraYaBloqueo() throws Exception {
    OutboxEvent e1 = OutboxEvent.of("TASK", UUID.randomUUID(), "TASK_COMPLETED", "{}");
    OutboxEvent e2 = OutboxEvent.of("TASK", UUID.randomUUID(), "TASK_COMPLETED", "{}");
    transactionTemplate.executeWithoutResult(status -> repository.saveAll(List.of(e1, e2)));

    CountDownLatch primeraTransaccionTieneElLock = new CountDownLatch(1);
    CountDownLatch puedeCommitear = new CountDownLatch(1);
    Future<List<OutboxEvent>> primera;
    Future<List<OutboxEvent>> segunda;
    try (ExecutorService pool = Executors.newFixedThreadPool(2)) {

      primera = pool.submit(() -> transactionTemplate.execute(status -> {
        List<OutboxEvent> batch = repository.lockNextPendingBatch(1); // toma 1 fila y la bloquea
        primeraTransaccionTieneElLock.countDown();
        await(String.valueOf(puedeCommitear));
        return batch;
      }));

      primeraTransaccionTieneElLock.await();

      segunda = pool.submit(
          () -> transactionTemplate.execute(status -> repository.lockNextPendingBatch(1)));
    }

    List<OutboxEvent> resultadoSegunda = segunda.get(2, TimeUnit.SECONDS);
    puedeCommitear.countDown();
    List<OutboxEvent> resultadoPrimera = primera.get();

    // la segunda transacción NO debe haber visto la fila que la primera ya bloqueó
    assertThat(resultadoSegunda).extracting(OutboxEvent::getId)
        .doesNotContain(resultadoPrimera.getFirst().getId());
  }
}