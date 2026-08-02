# k8s/README.md — Secrets imperativos

Estos Secrets NO se versionan como YAML (contienen credenciales reales) y deben crearse manualmente
antes de desplegar. Reemplazar los placeholders con los valores reales (ver .env local o el gestor
de secretos del equipo).

## localstack-secret

kubectl create secret generic localstack-secret --from-literal=LOCALSTACK_AUTH_TOKEN=<TU_TOKEN_AQUI>