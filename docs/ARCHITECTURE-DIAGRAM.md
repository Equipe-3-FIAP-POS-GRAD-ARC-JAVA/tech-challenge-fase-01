# 📐 Diagrama de Arquitetura - Validação SOLID e Hexagonal

**Data de Atualização**: 04/10/2025  
**Status**: ✅ **VALIDADO E ATUALIZADO**  
**Total de Arquivos Java**: 63 arquivos

## 🏗️ Visão Geral da Arquitetura

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          INFRASTRUCTURE LAYER                                │
│                     (Frameworks, Drivers, Adapters)                          │
│                                                                               │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                      INBOUND ADAPTERS                                 │   │
│  │                    (Driving Side)                                     │   │
│  │                                                                        │   │
│  │  ┌────────────────┐  ┌─────────────────┐  ┌──────────────────┐     │   │
│  │  │  Controllers   │  │   JWT Filter    │  │  Exception       │     │   │
│  │  │  (REST API)    │  │   Security      │  │  Handlers        │     │   │
│  │  │                │  │                 │  │                  │     │   │
│  │  │ @RestController│  │ @Component      │  │ @ControllerAdvice│     │   │
│  │  │ ✅ 2 classes   │  │ ✅ 5 classes    │  │ ✅ 2 handlers    │     │   │
│  │  └───────┬────────┘  └────────┬────────┘  └────────┬─────────┘     │   │
│  │          │                    │                     │               │   │
│  └──────────┼────────────────────┼─────────────────────┼───────────────┘   │
│             │                    │                     │                    │
│             │ Web DTOs           │ Security DTOs       │ RFC 7807 DTOs      │
│             │ (Jakarta Valid.)   │                     │                    │
│             ↓                    ↓                     ↓                    │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                       WEB MAPPERS                                     │   │
│  │         AuthWebMapper, UserWebMapper (@Component)                    │   │
│  │                      ✅ 2 mappers                                     │   │
│  │                                                                        │   │
│  │  Converte: Web DTO ←→ Application DTO                                │   │
│  └────────────────────────────┬──────────────────────────────────────────┘   │
│                               │                                              │
└───────────────────────────────┼──────────────────────────────────────────────┘
                                ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│                          APPLICATION LAYER                                   │
│                  (Business Logic, Use Cases, Ports)                          │
│                       ⚠️ NO FRAMEWORK DEPENDENCIES                          │
│                                                                               │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                      INBOUND PORTS (Use Case Interfaces)              │   │
│  │                         ✅ 9 ports definidas                          │   │
│  │                                                                        │   │
│  │  UserCreatePort, UserCreateOwnerPort, UserUpdatePort,                │   │
│  │  UserUpdatePasswordPort, UserDeletePort, UserFindByIdPort,           │   │
│  │  UserFindByNamePort, UserFindAuthenticatedUserByUsernamePort,        │   │
│  │  AuthPort                                                             │   │
│  └───────────────────────────┬──────────────────────────────────────────┘   │
│                              │                                               │
│                              ↓ implemented by                                │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                        USE CASES                                      │   │
│  │                      ✅ 7 use cases + 1 facade                        │   │
│  │                                                                        │   │
│  │  CreateUserUseCase, CreateOwnerUseCase, UpdateUserUseCase,           │   │
│  │  UpdatePasswordUseCase, DeleteUserUseCase, FindUserByIdUseCase,      │   │
│  │  FindUserByNameUseCase                                                │   │
│  │  AuthUseCases (autenticação)                                         │   │
│  │  UserService (facade pattern - delega para use cases)                │   │
│  │                                                                        │   │
│  │  ⚠️ NO @Service, NO @Component - Plain Java Classes                  │   │
│  └───────┬──────────────────────────────┬───────────────────────────────┘   │
│          │                              │                                    │
│          │ uses                         │ uses                               │
│          ↓                              ↓                                    │
│  ┌──────────────────┐          ┌─────────────────────────────┐              │
│  │ DOMAIN SERVICE   │          │   OUTBOUND PORTS            │              │
│  │  ✅ 1 service    │          │   (Repository, Security)    │              │
│  │                  │          │     ✅ 3 ports              │              │
│  │ UserDomainService│          │                             │              │
│  │ - Validações     │          │ UserRepositoryPort          │              │
│  │ - Regras cross-  │          │ PasswordEncoderPort         │              │
│  │   entity         │          │ JwtTokenPort                │              │
│  │                  │          │                             │              │
│  │ ⚠️ NO @Service   │          │ ⚠️ Interfaces only          │              │
│  └──────┬───────────┘          └──────────────┬──────────────┘              │
│         │                                     │                              │
│         │ uses                                │                              │
│         ↓                                     │                              │
│  ┌──────────────────┐                         │                              │
│  │  DOMAIN MODELS   │                         │                              │
│  │  ✅ 1 aggregate  │                         │                              │
│  │  ✅ 3 VOs        │                         │                              │
│  │                  │                         │                              │
│  │  UserDomain      │                         │                              │
│  │  Value Objects:  │                         │                              │
│  │    - Email       │                         │                              │
│  │    - Username    │                         │                              │
│  │    - PersonName  │                         │                              │
│  │                  │                         │                              │
│  │ ⚠️ Pure Java     │                         │                              │
│  └──────────────────┘                         │                              │
│                                               │                              │
│  ┌───────────────────────────────────────────┘                              │
│  │                                                                            │
│  │  ┌──────────────────┐          ┌─────────────────────────────┐           │
│  │  │ APPLICATION DTOs │          │   APPLICATION MAPPERS       │           │
│  │  │  ✅ 4 requests   │          │     ✅ 1 mapper             │           │
│  │  │  ✅ 2 responses  │          │                             │           │
│  │  │                  │◄─────────│ UserMapper (static methods) │           │
│  │  │ LoginRequest     │          │                             │           │
│  │  │ UserCreateRequest│          │ ⚠️ NO @Component            │           │
│  │  │ UserUpdateRequest│          └─────────────────────────────┘           │
│  │  │ UpdatePassword...│                                                     │
│  │  │ UserResponse     │                                                     │
│  │  │ LoginResponse    │                                                     │
│  │  │                  │                                                     │
│  │  │ ⚠️ NO Jakarta    │                                                     │
│  │  │    Validation    │                                                     │
│  │  └──────────────────┘                                                     │
│  │                                                                            │
│  │  ┌─────────────────────────────────────────────────────────┐             │
│  │  │           DOMAIN EXCEPTIONS (✅ 3 classes)               │             │
│  │  │                                                           │             │
│  │  │  DomainValidationException (base)                        │             │
│  │  │  ├── InvalidFieldException                               │             │
│  │  │  └── BusinessRuleException                               │             │
│  │  └─────────────────────────────────────────────────────────┘             │
│  │                                                                            │
└──┼────────────────────────────────────────────────────────────────────────────┘
   │ implemented by
   ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│                          INFRASTRUCTURE LAYER                                │
│                     (Frameworks, Drivers, Adapters)                          │
│                                                                               │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                      OUTBOUND ADAPTERS                                │   │
│  │                      (Driven Side)                                    │   │
│  │                                                                        │   │
│  │  ┌────────────────────┐  ┌──────────────────┐  ┌─────────────────┐  │   │
│  │  │ Repository Impl    │  │ Security Adapters│  │ Entity Mappers  │  │   │
│  │  │                    │  │                  │  │                 │  │   │
│  │  │ UserRepositoryImpl │  │ BCryptPassword   │  │ UserEntityMapper│  │   │
│  │  │                    │  │   EncoderAdapter │  │                 │  │   │
│  │  │ @Repository        │  │ JwtTokenAdapter  │  │ @Component      │  │   │
│  │  │                    │  │                  │  │                 │  │   │
│  │  │ implements         │  │ @Component       │  │ Domain ↔ Entity │  │   │
│  │  │ UserRepositoryPort │  │                  │  │                 │  │   │
│  │  └──────┬─────────────┘  └────────┬─────────┘  └────────┬────────┘  │   │
│  │         │                         │                     │           │   │
│  │         │ uses                    │ uses                │           │   │
│  │         ↓                         ↓                     ↓           │   │
│  │  ┌────────────────┐  ┌──────────────────┐  ┌──────────────────┐   │   │
│  │  │ JPA Repository │  │ Spring Security  │  │  JPA Entities    │   │   │
│  │  │                │  │                  │  │                  │   │   │
│  │  │ JpaUserRepo    │  │ BCryptPassword   │  │  JpaUserEntity   │   │   │
│  │  │                │  │   Encoder        │  │                  │   │   │
│  │  │ extends JPA    │  │ JwtTokenManager  │  │  @Entity         │   │   │
│  │  │   Repository   │  │                  │  │  @Table          │   │   │
│  │  └────────────────┘  └──────────────────┘  └──────────────────┘   │   │
│  │                                                                      │   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                                                                               │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                      SPRING CONFIGURATION                             │   │
│  │                                                                        │   │
│  │  ┌─────────────────────────┐  ┌──────────────────────────────┐      │   │
│  │  │ SecurityBeansConfig     │  │ WebSecurityConfig            │      │   │
│  │  │                         │  │                              │      │   │
│  │  │ @Configuration          │  │ @Configuration               │      │   │
│  │  │                         │  │ @EnableWebSecurity           │      │   │
│  │  │ Cria Use Cases beans    │  │                              │      │   │
│  │  │ Injeta dependências     │  │ Configura JWT, CORS, etc    │      │   │
│  │  └─────────────────────────┘  └──────────────────────────────┘      │   │
│  │                                                                        │   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                                                                               │
└─────────────────────────────────────────────────────────────────────────────┘
```

## 🔄 Fluxo de Dados (Exemplo: Criar Usuário)

```
1️⃣ Request HTTP
   └─→ UserController (@RestController)
       └─→ Recebe UserCreateRequestDTO (Jakarta Validation)
           └─→ UserWebMapper.toApplicationRequest()
               ↓
2️⃣ Application Layer
   └─→ UserCreateRequest (NO frameworks)
       └─→ UserCreatePort.create()
           └─→ CreateUserUseCase (implements UserCreatePort)
               ├─→ UserDomainService.ensureUsernameIsUnique()
               │   └─→ UserRepositoryPort.existsByUsername()
               │       └─→ UserRepositoryImpl (@Repository)
               │           └─→ JpaUserRepository.findByLogin()
               │               └─→ PostgreSQL Database
               │
               ├─→ PasswordEncoderPort.encode()
               │   └─→ BCryptPasswordEncoderAdapter (@Component)
               │       └─→ BCryptPasswordEncoder (Spring Security)
               │
               └─→ UserDomain.createClient() (Factory Method)
                   └─→ UserRepositoryPort.save()
                       └─→ UserRepositoryImpl.save()
                           └─→ UserEntityMapper.toEntity()
                               └─→ JpaUserRepository.save()
                                   └─→ PostgreSQL Database
                                       ↓
3️⃣ Response
   └─→ UserDomain
       └─→ UserMapper.toResponse()
           └─→ UserResponse (Application Layer)
               └─→ UserWebMapper.toWebResponse()
                   └─→ UserResponseDTO (Infrastructure Layer)
                       └─→ HTTP Response JSON
```

## 🎯 Princípios Aplicados

### ✅ SOLID

```
┌─────────────────────────────────────────────────────────┐
│ S - Single Responsibility Principle                    │
├─────────────────────────────────────────────────────────┤
│ ✅ CreateUserUseCase: apenas criar usuário             │
│ ✅ UserWebMapper: apenas converter DTOs                │
│ ✅ UserDomainService: apenas regras de negócio         │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│ O - Open/Closed Principle                              │
├─────────────────────────────────────────────────────────┤
│ ✅ Extensível: Novos Use Cases via Ports               │
│ ✅ Fechado: Core não muda ao adicionar adapters        │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│ L - Liskov Substitution Principle                      │
├─────────────────────────────────────────────────────────┤
│ ✅ Qualquer implementação de Port é intercambiável     │
│ ✅ BCrypt pode ser trocado por Argon2                  │
│ ✅ PostgreSQL pode ser trocado por MongoDB             │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│ I - Interface Segregation Principle                    │
├─────────────────────────────────────────────────────────┤
│ ✅ Ports específicas (UserCreatePort, UserUpdatePort)  │
│ ✅ Nenhuma interface "gorda"                           │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│ D - Dependency Inversion Principle                     │
├─────────────────────────────────────────────────────────┤
│ ✅ Use Cases dependem de Ports (abstrações)            │
│ ✅ Não dependem de Adapters (implementações)           │
│ ✅ Infrastructure depende de Application               │
└─────────────────────────────────────────────────────────┘
```

### ✅ Arquitetura Hexagonal

```
┌────────────────────────────────────────────────┐
│               HEXAGONAL CORE                   │
│           (Application Layer)                  │
│                                                │
│  ┌──────────────────────────────────────┐     │
│  │          INBOUND PORTS               │     │
│  │      (Driving Interfaces)            │     │
│  │                                      │     │
│  │  • UserCreatePort                    │     │
│  │  • UserUpdatePort                    │     │
│  │  • UserDeletePort                    │     │
│  │  • AuthPort                          │     │
│  └──────────────────────────────────────┘     │
│                     ↑                          │
│                     │ implements                │
│  ┌──────────────────────────────────────┐     │
│  │          USE CASES                   │     │
│  │                                      │     │
│  │  • CreateUserUseCase                 │     │
│  │  • UpdateUserUseCase                 │     │
│  │  • DeleteUserUseCase                 │     │
│  │  • AuthUseCases                      │     │
│  └──────────────────────────────────────┘     │
│                     │                          │
│                     │ uses                     │
│                     ↓                          │
│  ┌──────────────────────────────────────┐     │
│  │          OUTBOUND PORTS              │     │
│  │       (Driven Interfaces)            │     │
│  │                                      │     │
│  │  • UserRepositoryPort                │     │
│  │  • PasswordEncoderPort               │     │
│  │  • JwtTokenPort                      │     │
│  └──────────────────────────────────────┘     │
│                                                │
└────────────────────────────────────────────────┘
          ↑                           ↑
          │ calls                     │ implements
          │                           │
┌─────────────────┐         ┌─────────────────┐
│ INBOUND ADAPTERS│         │OUTBOUND ADAPTERS│
│   (Driving)     │         │   (Driven)      │
│                 │         │                 │
│ • Controllers   │         │ • Repositories  │
│ • JWT Filter    │         │ • Security      │
│ • Handlers      │         │ • Mappers       │
└─────────────────┘         └─────────────────┘
```

## ✅ Validações de Independência

### 🔍 Application Layer - Zero Dependencies

```
✅ NO @Component
✅ NO @Service
✅ NO @Repository
✅ NO @Controller
✅ NO @RestController
✅ NO @Entity
✅ NO @Table
✅ NO @Column
✅ NO Jakarta Validation (@NotBlank, @Email, etc)
✅ NO Spring imports (org.springframework.*)
✅ NO Jakarta Persistence imports (jakarta.persistence.*)
✅ NO Hibernate imports (org.hibernate.*)

✅ ONLY Java Standard Library
✅ ONLY Lombok (for boilerplate reduction)
```

### 🔧 Infrastructure Layer - Framework Specific

```
✅ @Component - Mappers, Adapters
✅ @Repository - Repository implementations
✅ @RestController - REST Controllers
✅ @Configuration - Spring configurations
✅ @Entity, @Table - JPA entities
✅ Jakarta Validation - Web DTOs only
✅ Spring Security - Security configs
✅ JWT libraries - Token management
```

---

**Diagrama validado em:** 04 de outubro de 2025  
**Status:** ✅ 100% em conformidade
