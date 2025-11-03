# RFC 7807 - Exception Handling Documentation

## 📋 Visão Geral

Este documento descreve a implementação completa do tratamento de exceções seguindo a **RFC 7807 - Problem Details for HTTP APIs** no projeto Tech Challenge FIAP.

## 🎯 RFC 7807 - Problem Details

A RFC 7807 define um formato padrão para retornar detalhes de problemas em APIs HTTP. Cada resposta de erro **DEVE** conter:

### Campos Obrigatórios RFC 7807

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `type` | URI | Identifica o tipo do problema |
| `title` | String | Resumo legível do tipo de problema |
| `status` | Integer | Código de status HTTP |
| `detail` | String | Explicação específica desta ocorrência |
| `instance` | URI | Identifica especificamente esta ocorrência (path da requisição) |

### Campos Customizados (Extension Members)

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `timestamp` | String | Data/hora da ocorrência (ISO-8601) |
| `errorType` | String | Categoria do erro (ENUM interno) |
| `fieldName` | String | Nome do campo (apenas para erros de validação) |

---

## 🏗️ Hierarquia de Exceções

### 1. Domain Layer (Camada de Domínio)

```
DomainValidationException (base)
├── InvalidFieldException
└── BusinessRuleException
```

### 2. Application Layer (Camada de Aplicação)

```
RuntimeException
├── UserNotFoundException
└── UserAlreadyExistsException
```

### 3. Infrastructure Layer (Camada de Infraestrutura)

```
RuntimeException
├── NotFoundException
└── UnauthorizedException
```

---

## 📊 Mapeamento: Exceção → HTTP Status → Problem Type

| Status | Problem Type | Exceção | Cenário |
|--------|-------------|---------|---------|
| 400 | `/problems/invalid-field` | InvalidFieldException | Campo com valor inválido |
| 400 | `/problems/validation-error` | DomainValidationException | Erro genérico de validação |
| 400 | `/problems/invalid-argument` | IllegalArgumentException | Argumento ilegal (fallback) |
| 401 | `/problems/unauthorized` | UnauthorizedException | Não autorizado |
| 404 | `/problems/not-found` | UserNotFoundException, NotFoundException | Recurso não encontrado |
| 409 | `/problems/conflict` | UserAlreadyExistsException | Conflito de dados (duplicação) |
| 422 | `/problems/business-rule-violation` | BusinessRuleException | Violação de regra de negócio |
| 500 | `/problems/internal-server-error` | Exception | Erro interno não tratado |

---

## 📝 Exemplos de Respostas RFC 7807

### 1. Campo Inválido (400 - Bad Request)

**Request:**
```http
POST /api/users
Content-Type: application/json

{
  "firstName": "Jo",
  "email": "invalido",
  "username": "user123",
  "password": "Pass@123"
}
```

**Response:**
```http
HTTP/1.1 400 Bad Request
Content-Type: application/problem+json

{
  "type": "https://api.fiap.com.br/problems/invalid-field",
  "title": "Campo Inválido",
  "status": 400,
  "detail": "Email com formato inválido",
  "instance": "/api/users",
  "timestamp": "2025-01-19T15:30:00-03:00",
  "errorType": "VALIDATION_ERROR",
  "fieldName": "email"
}
```

---

### 2. Regra de Negócio Violada (422 - Unprocessable Entity)

**Request:**
```http
PUT /api/users/123/activate
```

**Cenário:** Usuário já está ativo

**Response:**
```http
HTTP/1.1 422 Unprocessable Entity
Content-Type: application/problem+json

{
  "type": "https://api.fiap.com.br/problems/business-rule-violation",
  "title": "Regra de Negócio Violada",
  "status": 422,
  "detail": "Usuário já está ativo. Não é possível ativar novamente",
  "instance": "/api/users/123/activate",
  "timestamp": "2025-01-19T15:32:15-03:00",
  "errorType": "BUSINESS_RULE_VIOLATION"
}
```

---

### 3. Recurso Não Encontrado (404 - Not Found)

**Request:**
```http
GET /api/users/999
```

**Response:**
```http
HTTP/1.1 404 Not Found
Content-Type: application/problem+json

{
  "type": "https://api.fiap.com.br/problems/not-found",
  "title": "Usuário Não Encontrado",
  "status": 404,
  "detail": "Usuário com ID 999 não foi encontrado",
  "instance": "/api/users/999",
  "timestamp": "2025-01-19T15:35:00-03:00",
  "errorType": "RESOURCE_NOT_FOUND"
}
```

---

### 4. Conflito de Dados (409 - Conflict)

**Request:**
```http
POST /api/users
Content-Type: application/json

{
  "username": "john.doe",
  "email": "john@example.com"
}
```

**Cenário:** Username já cadastrado

**Response:**
```http
HTTP/1.1 409 Conflict
Content-Type: application/problem+json

{
  "type": "https://api.fiap.com.br/problems/conflict",
  "title": "Conflito de Dados",
  "status": 409,
  "detail": "Username 'john.doe' já está em uso",
  "instance": "/api/users",
  "timestamp": "2025-01-19T15:37:20-03:00",
  "errorType": "DUPLICATE_RESOURCE"
}
```

---

### 5. Não Autorizado (401 - Unauthorized)

**Request:**
```http
GET /api/users/profile
Authorization: Bearer invalid_token
```

**Response:**
```http
HTTP/1.1 401 Unauthorized
Content-Type: application/problem+json

{
  "type": "https://api.fiap.com.br/problems/unauthorized",
  "title": "Não Autorizado",
  "status": 401,
  "detail": "Token de autenticação inválido ou expirado",
  "instance": "/api/users/profile",
  "timestamp": "2025-01-19T15:40:00-03:00",
  "errorType": "UNAUTHORIZED"
}
```

---

### 6. Erro Interno (500 - Internal Server Error)

**Request:**
```http
POST /api/users
```

**Cenário:** Exceção não tratada

**Response:**
```http
HTTP/1.1 500 Internal Server Error
Content-Type: application/problem+json

{
  "type": "https://api.fiap.com.br/problems/internal-server-error",
  "title": "Erro Interno do Servidor",
  "status": 500,
  "detail": "Ocorreu um erro interno. Por favor, tente novamente mais tarde.",
  "instance": "/api/users",
  "timestamp": "2025-01-19T15:45:00-03:00",
  "errorType": "INTERNAL_SERVER_ERROR"
}
```

---

## ✅ Boas Práticas

### O QUE FAZER

#### 1. Usar Exceções Específicas
```java
// ❌ Ruim
throw new Exception("Erro");

// ✅ Bom
throw new InvalidFieldException("email", "Email com formato inválido");
```

#### 2. Mensagens Descritivas
```java
// ❌ Ruim
throw new InvalidFieldException("email", "Inválido");

// ✅ Bom
throw new InvalidFieldException("email", "Email com formato inválido. Use: usuario@dominio.com");
```

#### 3. Lançar no Lugar Certo
- **Domain Layer**: Validações de regras de negócio
- **Application Layer**: Orquestração (não encontrado, já existe)
- **Infrastructure Layer**: Problemas técnicos (banco, rede)

#### 4. Códigos HTTP Adequados
- `400`: Problema com a requisição do cliente
- `404`: Recurso não existe
- `409`: Conflito (duplicação, estado inconsistente)
- `422`: Requisição bem formada mas semanticamente incorreta
- `500`: Erro do servidor (inesperado)

#### 5. Type URI Consistente
- Use sempre o mesmo base: `https://api.fiap.com.br/problems`
- Types descritivos: `/invalid-field`, `/business-rule-violation`
- Documente cada tipo em página web acessível

#### 6. Instance (Path) Sempre
- Capture o caminho exato da requisição
- Útil para rastreamento e debugging
- Exemplo: `/api/users/123/activate`

---

### O QUE NÃO FAZER

#### 1. NÃO usar Exception genérica
```java
// ❌ Evitar
throw new Exception("Erro");

// ✅ Preferir
throw new InvalidEmailException("Email", email);
```

#### 2. NÃO expor detalhes internos em produção
```java
// ❌ Evitar
"NullPointerException at UserService.java:42"

// ✅ Preferir
"Ocorreu um erro interno. Contate o suporte."
```

#### 3. NÃO usar códigos HTTP incorretos
```java
// ❌ Evitar
return ResponseEntity.status(500).body("User not found");

// ✅ Preferir
return ResponseEntity.status(404).body(problemDetail);
```

#### 4. NÃO misturar responsabilidades
```java
// ❌ Evitar - Domain lançando SQLException
public class UserDomain {
    public void save() throws SQLException { }
}

// ✅ Preferir - Domain lança exceção de domínio
public class UserDomain {
    public void validate() {
        throw new BusinessRuleException("Regra violada");
    }
}
```

#### 5. NÃO omitir campos RFC 7807
```java
// ❌ Evitar
return Map.of("error", "Not found");

// ✅ Preferir - Incluir type, title, status, detail, instance
ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(...);
problemDetail.setType(URI.create("..."));
problemDetail.setInstance(URI.create("..."));
```

---

## 🔄 Fluxo de Exceções

```
┌─────────────────────────────────────────────────────────────┐
│ Domain Layer (Value Object)                                 │
│                                                             │
│ Email.of("invalido")                                        │
│   └─> throw InvalidFieldException("email", "...")          │
└─────────────────────────────────────────────────────────────┘
                           │
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ Application Layer (Use Case)                                │
│                                                             │
│ CreateUserUseCase.execute(request)                          │
│   ├─> Propaga InvalidFieldException                        │
│   └─> throw UserAlreadyExistsException("...")              │
└─────────────────────────────────────────────────────────────┘
                           │
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ Infrastructure Layer (Controller)                           │
│                                                             │
│ @ExceptionHandler captura a exceção                         │
└─────────────────────────────────────────────────────────────┘
                           │
                           ↓
┌─────────────────────────────────────────────────────────────┐
│ RFC 7807 Response                                           │
│                                                             │
│ {                                                           │
│   "type": "https://api.fiap.com.br/problems/invalid-field",│
│   "title": "Campo Inválido",                                │
│   "status": 400,                                            │
│   "detail": "Email com formato inválido",                   │
│   "instance": "/api/users",                                 │
│   "timestamp": "2025-01-19T15:30:00-03:00",                 │
│   "errorType": "VALIDATION_ERROR",                          │
│   "fieldName": "email"                                      │
│ }                                                           │
└─────────────────────────────────────────────────────────────┘
```

---

## 📚 Referências

- **RFC 7807**: [Problem Details for HTTP APIs](https://tools.ietf.org/html/rfc7807)
- **Spring ProblemDetail**: [Official Documentation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ProblemDetail.html)
- **HTTP Status Codes**: [httpstatuses.com](https://httpstatuses.com/)
- **REST API Error Handling**: [Baeldung Guide](https://www.baeldung.com/rest-api-error-handling-best-practices)

---

## 📝 Implementação

### GlobalExceptionHandler.java

Todos os handlers seguem o padrão:

```java
@ExceptionHandler(ExceptionType.class)
public ProblemDetail handleException(ExceptionType ex, WebRequest request) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.STATUS_CODE, 
        ex.getMessage()
    );
    
    // RFC 7807 - Campos obrigatórios
    problemDetail.setType(URI.create(BASE_PROBLEM_TYPE + "/type-path"));
    problemDetail.setTitle("Título Descritivo");
    problemDetail.setInstance(getRequestUri(request));
    
    // Extension members (customizados)
    problemDetail.setProperty("timestamp", getTimestamp());
    problemDetail.setProperty("errorType", "ERROR_CATEGORY");
    
    // Campos opcionais específicos
    // problemDetail.setProperty("fieldName", ex.getFieldName());
    
    return problemDetail;
}
```

### Métodos Auxiliares

```java
/**
 * Extrai o caminho da requisição (URI).
 * Usado para preencher o campo 'instance' do RFC 7807.
 */
private URI getRequestUri(WebRequest request) {
    String path = request.getDescription(false).replace("uri=", "");
    return URI.create(path);
}

/**
 * Retorna timestamp formatado em ISO-8601 com timezone.
 * Exemplo: 2025-01-19T15:30:00-03:00[America/Sao_Paulo]
 */
private String getTimestamp() {
    return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
}
```

---

## ✨ Conclusão

A implementação segue rigorosamente a RFC 7807, garantindo:

- ✅ Respostas padronizadas e previsíveis
- ✅ Fácil debugging com `instance` (path) e `timestamp`
- ✅ Categorização clara com `type` URI
- ✅ Internacionalização possível via `title` e `detail`
- ✅ Extensibilidade via custom properties


