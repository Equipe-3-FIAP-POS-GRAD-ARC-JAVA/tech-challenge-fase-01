# Comandos para testar os endpoints do Actuator

## Health Checks (Públicos)

# Health check geral
curl -X GET http://localhost:8080/actuator/health

# Liveness probe (Kubernetes)
curl -X GET http://localhost:8080/actuator/health/liveness

# Readiness probe (Kubernetes)
curl -X GET http://localhost:8080/actuator/health/readiness

# Health check da base de dados
curl -X GET http://localhost:8080/actuator/health/db

# Health check customizado do UserService
curl -X GET http://localhost:8080/actuator/health/userService

## Informações da Aplicação (Público)

# Informações gerais da aplicação
curl -X GET http://localhost:8080/actuator/info

## Endpoints Protegidos (Requer ADMIN)

# Primeiro, fazer login para obter o token JWT
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"emerson.silva","password":"senha123"}' | \
  jq -r '.token')

# Métricas gerais
curl -X GET http://localhost:8080/actuator/metrics \
  -H "Authorization: Bearer $TOKEN"

# Métrica específica - uso de memória JVM
curl -X GET http://localhost:8080/actuator/metrics/jvm.memory.used \
  -H "Authorization: Bearer $TOKEN"

# Métrica específica - requests HTTP
curl -X GET http://localhost:8080/actuator/metrics/http.server.requests \
  -H "Authorization: Bearer $TOKEN"

# Métricas para Prometheus (formato específico)
curl -X GET http://localhost:8080/actuator/prometheus \
  -H "Authorization: Bearer $TOKEN"

# Variáveis de ambiente (apenas algumas são expostas)
curl -X GET http://localhost:8080/actuator/env \
  -H "Authorization: Bearer $TOKEN"

# Configuração de loggers
curl -X GET http://localhost:8080/actuator/loggers \
  -H "Authorization: Bearer $TOKEN"

# Mudar o nível de log de um pacote específico (POST)
curl -X POST http://localhost:8080/actuator/loggers/br.com.fiap.challenge \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"configuredLevel":"DEBUG"}'

## Lista de todos os endpoints disponíveis
curl -X GET http://localhost:8080/actuator

## Testando com autenticação via Postman/Insomnia
# 1. POST /api/v1/auth/login com credenciais para obter token
# 2. Usar o token nos headers: Authorization: Bearer <token>
# 3. Acessar qualquer endpoint /actuator/* (exceto health e info que são públicos)

## Exemplos de resposta esperada

# /actuator/health (UP quando tudo funcionando):
# {
#   "status": "UP",
#   "components": {
#     "db": {"status": "UP"},
#     "userService": {"status": "UP", "details": {"status": "User repository is accessible"}},
#     "diskSpace": {"status": "UP"}
#   }
# }

# /actuator/info (informações customizadas):
# {
#   "application": {
#     "name": "Tech Challenge Fase 01",
#     "version": "1.0.0"
#   },
#   "technical": {
#     "architecture": "Hexagonal Architecture (Ports & Adapters)",
#     "framework": "Spring Boot 3.5.6"
#   }
# }