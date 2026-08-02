# Kubernetes — Comandos de referencia (Día 13, Taskflow)

## Cluster (kind)

```bash
kind create cluster --name taskflow          # crear cluster
kind get clusters                             # listar clusters
kind delete cluster --name taskflow           # borrar cluster (destruye todo)
kubectl cluster-info --context kind-taskflow  # info del control plane
```

## Imágenes propias en kind

`kind` no ve tu Docker host — hay que cargar la imagen explícitamente:

```bash
docker build -t taskflow-api:1.0.0 .
kind load docker-image taskflow-api:1.0.0 --name taskflow
```

## Aplicar / ver / borrar manifiestos

```bash
kubectl apply -f k8s/archivo.yaml       # crea o actualiza un recurso
kubectl apply -f k8s/                   # aplica TODOS los yaml de la carpeta
kubectl delete -f k8s/archivo.yaml      # borra ese recurso
```

## Ver estado

```bash
kubectl get pods                        # lista pods
kubectl get pods -w                     # "watch" — en vivo hasta Ctrl+C
kubectl get svc                         # lista Services
kubectl get statefulset
kubectl get deployment
kubectl get pvc                         # volúmenes (PersistentVolumeClaim)
kubectl get storageclass
kubectl get configmap
kubectl get secret
kubectl get endpoints <nombre-service>  # a qué Pods resuelve un Service
```

## Diagnóstico (bugs reales de hoy)

```bash
kubectl describe pod <nombre-pod>              # eventos, causa real de errores
kubectl logs <nombre-pod>                      # logs del contenedor actual
kubectl logs <nombre-pod> --previous           # logs del intento anterior (crash loop)
kubectl logs <nombre-pod> --tail=50 --timestamps
kubectl logs deploy/<nombre-deployment>        # atajo, sin escribir el nombre random completo
```

## Secrets y ConfigMaps

```bash
# Secret declarativo (valores dummy, versionable)
kubectl apply -f k8s/xxx-secret.yaml

# Secret imperativo (credenciales reales, NO versionar)
kubectl create secret generic <nombre> \
  --from-literal=CLAVE=valor

# ConfigMap desde un archivo
kubectl create configmap <nombre> \
  --from-file=nombre-key=ruta/al/archivo \
  --dry-run=client -o yaml > k8s/xxx-configmap.yaml
```

## Conectividad / pruebas manuales

```bash
# Pod temporal de un solo uso, se borra al salir
kubectl run -it --rm psql-test --image=postgres:16-alpine --restart=Never \
  --env="PGPASSWORD=taskflow" -- psql -h postgres-0.postgres -U taskflow -d taskflow

kubectl run -it --rm redis-test --image=redis:7-alpine --restart=Never -- \
  redis-cli -h redis-0.redis ping

# Ejecutar un comando dentro de un pod/deployment ya corriendo
kubectl exec -it deploy/localstack -- awslocal dynamodb list-tables --region us-east-1

# Exponer un Service hacia tu máquina (desarrollo/pruebas)
kubectl port-forward svc/keycloak 8081:8080
kubectl port-forward svc/taskflow-api 8080:8080
```

## Escalado

```bash
kubectl scale deployment taskflow-api --replicas=3
kubectl scale statefulset postgres --replicas=3   # da identidad+storage, NO configura replicación de datos
```

## Recrear un Pod (tomar cambios de env/config)

```bash
kubectl delete pod <nombre-pod>              # el Deployment/StatefulSet lo recrea solo
kubectl delete pod -l app=taskflow-api       # borra por label, útil si el nombre es random (Deployment)
```

---

## Bugs reales resueltos hoy (para no repetir el debugging)

1. **Keycloak `CrashLoopBackOff`**: usar `args:` en vez de `command:` cuando la imagen ya tiene un
   ENTRYPOINT (`kc.sh`) pensado para recibir subcomandos — `command` lo reemplaza, `args` lo
   respeta.
2. **Kafka deadlock de arranque**: un StatefulSet de un solo nodo con roles `broker,controller`
   combinados necesita resolver su propio DNS (`kafka-0.kafka`) *antes* de estar `Ready` — pero el
   Service headless no publica DNS hasta que el Pod está `Ready`. Fix:
   `publishNotReadyAddresses: true` en el Service.
3. **Kafka `readinessProbe` timeout**: el default de `timeoutSeconds` es 1s — insuficiente para un
   comando que arranca una JVM cliente. Fijar `timeoutSeconds` explícito y generoso para probes
   basados en JVM.
4. **ConfigMap/Secret "not found" al montar un volumen**: el
   `kubectl create ... --dry-run=client -o yaml > archivo.yaml` solo genera el YAML — falta el
   `kubectl apply -f archivo.yaml` después, o el recurso nunca se crea en el cluster.
5. **Actuator devolviendo 401**: si `SecurityConfig` tiene `.anyRequest().authenticated()`, hay que
   agregar explícitamente `.requestMatchers("/actuator/health/**").permitAll()` — el kubelet le pega
   a los probes sin JWT.

---

## ⚠️ Cuidado: `docker stop` vs `docker rm` en el nodo de kind

El "nodo" de `kind` (`taskflow-control-plane`) es un contenedor Docker — todo el cluster (etcd,
manifiestos aplicados, y los volúmenes PVC de Postgres/Keycloak) vive **dentro de su filesystem**.

- **`docker stop` / reiniciar Docker/WSL/PC**: el contenedor vuelve a arrancar con todo intacto. Los
  Pods se relevantan solos (no hace falta reaplicar nada), solo hay que esperar a que lleguen a
  `Ready` de nuevo y volver a correr los `port-forward` (esos sí son efímeros).
- **`docker rm` del nodo**: borra el cluster completo de forma **irrecuperable**, incluyendo los
  datos de los PVC. Recrear desde cero:
  ```bash
  kind create cluster --name taskflow
  kubectl apply -f k8s/postgres-secret.yaml
  kubectl apply -f k8s/postgres-statefulset.yaml
  kubectl apply -f k8s/postgres-service.yaml
  # ... repetir con cada manifiesto, mismo orden en que se armaron
  # + recrear los Secrets imperativos (ver sección de arriba)
  ```