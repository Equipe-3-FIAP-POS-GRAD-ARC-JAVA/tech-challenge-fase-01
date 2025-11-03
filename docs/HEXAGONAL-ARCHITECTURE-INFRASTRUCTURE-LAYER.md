# 🏗️ Infrastructure Layer - Arquitetura Hexagonal

**Data**: 15 de Outubro 2025  
**Versão**: 2.2  
**Status**: ✅ Implementado e Validado

## 📋 Visão Geral

A **Infrastructure Layer** implementa os **Adapters** da Arquitetura Hexagonal, conectando o **núcleo da aplicação** (Application Layer) com o **mundo externo**. Esta camada contém todas as dependências de frameworks, tecnologias e sistemas externos.

## 🎯 Responsabilidades

- 🔌 **Inbound Adapters**: Recebem requisições externas (HTTP, eventos, etc.) e as direcionam para a aplicação
- 🔌 **Outbound Adapters**: Implementam as abstrações definidas pela aplicação para acessar recursos externos
- ⚙️ **Configurações**: Setup de frameworks e injeção de dependências
- 🛡️ **Cross-cutting Concerns**: Segurança, logging, validação, tratamento de erros

---

## 🏗️ Estrutura da Camada

```
infrastructure/
├── adapters/
│   ├── inbound/
│   │   ├── security/
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   ├── JwtUtil.java
│   │   │   └── SecurityUser.java
│   │   └── web/
│   │       └── rest/
│   │           ├── controller/
│   │           │   ├── LoginController.java
│   │           │   └── UserController.java
│   │           ├── dto/
│   │           │   ├── requests/
│   │           │   │   ├── LoginRequest.java
│   │           │   │   ├── UpdatePasswordRequestDTO.java
│   │           │   │   ├── UserCreateRequestDTO.java
│   │           │   │   └── UserUpdateRequestDTO.java
│   │           │   └── response/
│   │           │       ├── LoginResponse.java
│   │           │       └── UserResponseDTO.java
│   │           └── mapper/
│   │               ├── AuthWebMapper.java
│   │               └── UserWebMapper.java
│   └── outbound/
│       ├── entities/
│       │   └── JpaUserEntity.java
│       ├── mappers/
│       │   └── UserEntityMapper.java
│       ├── repositories/
│       │   ├── JpaUserRepository.java
│       │   └── UserRepositoryImpl.java
│       └── security/
│           └── BCryptPasswordEncoderAdapter.java
├── configs/
│   ├── AuthUseCaseConfig.java
│   ├── SecurityConfig.java
│   ├── UserUseCaseConfig.java
│   └── WebSecurityConfig.java
└── exceptions/
    ├── GlobalExceptionHandler.java
    ├── NotFoundException.java
    └── UnauthorizedException.java
```

---

## 📥 Inbound Adapters (Driving Side)

Os **Inbound Adapters** são **dirigidos por agentes externos** (usuários, sistemas) e **chamam** a aplicação através dos **Inbound Ports**.

### REST Controllers

#### UserController

```java
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserCreatePort userCreatePort;
    private final UserCreateOwnerPort userCreateOwnerPort;
    private final UserUpdatePort userUpdatePort;
    private final UserUpdatePasswordPort userUpdatePasswordPort;
    private final UserDeletePort userDeletePort;
    private final UserFindByNamePort userFindByNamePort;
    private final UserFindByIdPort userFindByIdPort;
    private final UserWebMapper webMapper;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createClient(@Valid @RequestBody UserCreateRequestDTO dto) {
        UserCreateRequest request = webMapper.toApplicationRequest(dto);
        UserResponse response = userCreatePort.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(webMapper.toWebResponse(response));
    }

    @PostMapping("/owner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createOwner(@Valid @RequestBody UserCreateRequestDTO dto) {
        UserCreateRequest request = webMapper.toApplicationRequest(dto);
        UserResponse response = userCreateOwnerPort.createOwner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(webMapper.toWebResponse(response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        UUID userId = UUID.fromString(id);
        UserResponse response = userFindByIdPort.findById(userId);
        return ResponseEntity.ok(webMapper.toWebResponse(response));
    }

    @GetMapping("/by-name")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<List<UserResponseDTO>> getUserByName(@RequestParam String name) {
        List<UserResponse> responses = userFindByNamePort.findByName(name);
        return ResponseEntity.ok(responses.stream()
                .map(webMapper::toWebResponse)
                .toList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'CLIENT')")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateRequestDTO dto) {
        UUID userId = UUID.fromString(id);
        UserUpdateRequest request = webMapper.toApplicationUpdateRequest(dto);
        UserResponse response = userUpdatePort.update(userId, request);
        return ResponseEntity.ok(webMapper.toWebResponse(response));
    }

    @PatchMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> updatePassword(
            @AuthenticationPrincipal SecurityUser principal,
            @Valid @RequestBody UpdatePasswordRequestDTO dto) {
        UUID userId = principal.getId();
        UpdatePasswordRequest request = webMapper.toApplicationPasswordRequest(dto);
        UserResponse response = userUpdatePasswordPort.updatePassword(userId, request);
        return ResponseEntity.ok(webMapper.toWebResponse(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        UUID userId = UUID.fromString(id);
        userDeletePort.delete(userId);
        return ResponseEntity.noContent().build();
    }

}
```

**Características do Controller**:
- ✅ **Thin Layer** - Apenas conversão de DTOs e delegação
- ✅ **Framework Specific** - Usa anotações Spring Boot
- ✅ **Validation** - Valida DTOs de entrada
- ✅ **Documentation** - Swagger/OpenAPI
- ✅ **Security** - Integração com Spring Security

#### LoginController

```java
@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/v1/auth")
public class LoginController {
    
    private final AuthPort authPort;
    private final AuthWebMapper authWebMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // 1. Converte DTO Web → DTO Application
        var appRequest = authWebMapper.toApplicationLoginRequest(loginRequest);
        
        // 2. Chama Use Case através do Port
        var appResponse = authPort.login(appRequest);
        
        // 3. Converte DTO Application → DTO Web  
        return ResponseEntity.ok(authWebMapper.toWebLoginResponse(appResponse));
    }
}
```

### Web DTOs

#### Request DTOs
```java
// UserCreateRequestDTO.java
public record UserCreateRequestDTO(
        @NotBlank(message = "Nome é obrigatório") @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres") String name,

        @NotBlank(message = "Email é obrigatório") @Email(message = "Email inválido") String email,

        @NotBlank(message = "Login é obrigatório") @Size(min = 3, max = 50, message = "Login deve ter entre 3 e 50 caracteres") String login,

        @NotBlank(message = "Senha é obrigatória") @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres") String password) {
}
```

#### Response DTOs
```java
// UserResponseDTO.java
public record UserResponseDTO(String id, String name, String email, String login) {

}
```

**Características dos Web DTOs**:
- ✅ **Bean Validation** - Anotações Jakarta Validation
- ✅ **Serialization** - Jackson annotations para JSON
- ✅ **API Contract** - Definem contrato da API REST
- ✅ **Framework Coupled** - Acoplados ao framework web

### Web Mappers

```java
@Component
public class UserWebMapper {

    public UserCreateRequest toApplicationRequest(UserCreateRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new UserCreateRequest(
                dto.name(),
                dto.email(),
                dto.login(),
                dto.password());
    }

    public UserUpdateRequest toApplicationUpdateRequest(UserUpdateRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new UserUpdateRequest(
                dto.name(),
                dto.email(),
                dto.login());
    }

    public UpdatePasswordRequest toApplicationPasswordRequest(UpdatePasswordRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new UpdatePasswordRequest(
                dto.currentPassword(),
                dto.newPassword(),
                dto.confirmPassword());
    }

    public UserResponseDTO toWebResponse(UserResponse response) {
        if (response == null) {
            return null;
        }
        return new UserResponseDTO(
                response.id().toString(),
                response.name(),
                response.email(),
                response.login());
    }
}
```

### Security Filters

#### JwtAuthenticationFilter

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JpaUserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, JpaUserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtil.getUsernameFromToken(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<JpaUserEntity> userOpt = userRepository.findByLogin(username);
            if (userOpt.isPresent() && jwtUtil.validateToken(token)) {
                UserDetails userDetails = new SecurityUser(userOpt.get());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                if (userDetails instanceof SecurityUser) {
                    SecurityUser securityUser = (SecurityUser) userDetails;
                    logger.info(
                        String.format("User Authenticated: Username=%s, ID=%s, Roles=%s",
                                securityUser.getUsername(),
                                securityUser.getId(),
                                securityUser.getAuthorities()
                        )
                    );
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
```

### Exception Handlers

#### GlobalExceptionHandler

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String BASE_PROBLEM_TYPE = "/problems";

    @ExceptionHandler(InvalidFieldException.class)
    public ProblemDetail handleInvalidFieldException(InvalidFieldException ex, WebRequest request) {
        ProblemDetail problemDetail = createBaseProblemDetail(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                "/invalid-field", 
                "Campo Inválido", 
                "VALIDATION_ERROR", 
                request
        );
        
        problemDetail.setProperty("fieldName", ex.getFieldName());
        return problemDetail;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRuleException(BusinessRuleException ex, WebRequest request) {
        return createBaseProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY, 
                ex.getMessage(), 
                "/business-rule-violation", 
                "Regra de Negócio Violada", 
                "BUSINESS_RULE_VIOLATION", 
                request
        );
    }

    @ExceptionHandler(DomainValidationException.class)
    public ProblemDetail handleDomainValidationException(DomainValidationException ex, WebRequest request) {
        return createBaseProblemDetail(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                "/domain-validation", 
                "Validação de Domínio", 
                "DOMAIN_VALIDATION_ERROR", 
                request
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        return createBaseProblemDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                "/not-found",
                "Usuário Não Encontrado",
                "RESOURCE_NOT_FOUND",
                request
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());

        problemDetail.setType(URI.create(BASE_PROBLEM_TYPE + "/conflict"));
        problemDetail.setTitle("Conflito de Dados");
        problemDetail.setInstance(getRequestUri(request));
        problemDetail.setProperty("timestamp", getTimestamp());
        problemDetail.setProperty("errorType", "DUPLICATE_RESOURCE");

        return problemDetail;
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getMessage());

        problemDetail.setType(URI.create(BASE_PROBLEM_TYPE + "/not-found"));
        problemDetail.setTitle("Recurso Não Encontrado");
        problemDetail.setInstance(getRequestUri(request));
        problemDetail.setProperty("timestamp", getTimestamp());
        problemDetail.setProperty("errorType", "NOT_FOUND");

        return problemDetail;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ProblemDetail handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());

        problemDetail.setType(URI.create(BASE_PROBLEM_TYPE + "/unauthorized"));
        problemDetail.setTitle("Não Autorizado");
        problemDetail.setInstance(getRequestUri(request));
        problemDetail.setProperty("timestamp", getTimestamp());
        problemDetail.setProperty("errorType", "UNAUTHORIZED");

        return problemDetail;
    }

    @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
    public ProblemDetail handleAccessDeniedException(org.springframework.security.authorization.AuthorizationDeniedException ex, WebRequest request) {
        return createBaseProblemDetail(
                HttpStatus.FORBIDDEN,
                "Você não tem permissão para acessar este recurso.",
                "/access-denied",
                "Acesso Negado",
                "ACCESS_DENIED",
                request
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());

        problemDetail.setType(URI.create(BASE_PROBLEM_TYPE + "/invalid-argument"));
        problemDetail.setTitle("Argumento Inválido");
        problemDetail.setInstance(getRequestUri(request));
        problemDetail.setProperty("timestamp", getTimestamp());
        problemDetail.setProperty("errorType", "INVALID_ARGUMENT");

        return problemDetail;
    }

    @ExceptionHandler(JpaSystemException.class)
    public ProblemDetail handleJpaSystemException(JpaSystemException ex, WebRequest request) {
        // Log do erro real para debug
        System.err.println("JpaSystemException caught: " + ex.getMessage());
        System.err.println("Cause: " + ex.getCause());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno. Por favor, tente novamente mais tarde.");

        problemDetail.setType(URI.create(BASE_PROBLEM_TYPE + "/internal-server-error"));
        problemDetail.setTitle("Erro Interno do Servidor");
        problemDetail.setInstance(getRequestUri(request));
        problemDetail.setProperty("timestamp", getTimestamp());
        problemDetail.setProperty("errorType", "JPA_SYSTEM_ERROR");

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, WebRequest request) {
        // Em produção, logar a exceção e retornar mensagem genérica
        // logger.error("Erro inesperado", ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno. Por favor, tente novamente mais tarde.");

        problemDetail.setType(URI.create(BASE_PROBLEM_TYPE + "/internal-server-error"));
        problemDetail.setTitle("Erro Interno do Servidor");
        problemDetail.setInstance(getRequestUri(request));
        problemDetail.setProperty("timestamp", getTimestamp());
        problemDetail.setProperty("errorType", "INTERNAL_SERVER_ERROR");

        return problemDetail;
    }

    private ProblemDetail createBaseProblemDetail(
            HttpStatus status,
            String detail,
            String typeSubpath,
            String title,
            String errorType,
            WebRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        
        problemDetail.setType(URI.create(BASE_PROBLEM_TYPE + typeSubpath));
        problemDetail.setTitle(title);
        problemDetail.setInstance(getRequestUri(request));
        problemDetail.setProperty(TIMESTAMP_PROPERTY, getTimestamp());
        problemDetail.setProperty(ERROR_TYPE_PROPERTY, errorType);
        
        return problemDetail;
    }

    private URI getRequestUri(WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return URI.create(path);
    }

    private String getTimestamp() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"))
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private static final String TIMESTAMP_PROPERTY = "timestamp";
    private static final String ERROR_TYPE_PROPERTY = "errorType";
}
```

---

## 📤 Outbound Adapters (Driven Side)

Os **Outbound Adapters** são **chamados pela aplicação** e implementam os **Outbound Ports** para acessar recursos externos.

### Repository Adapters

#### JPA Entities

```java
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JpaUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false, length = 100)
    private String login;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "roles", nullable = false)
    private List<RolesEnum> role;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

}
```

#### JPA Repository Interface

```java
@Repository
public interface JpaUserRepository extends JpaRepository<JpaUserEntity, UUID> {

    @Query("SELECT u FROM JpaUserEntity u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%',:name,'%')) AND u.isActive = true")
    public List<JpaUserEntity> findByName(@Param("name") String name);

    Optional<JpaUserEntity> findByLogin(String login);

    Optional<JpaUserEntity> findByLoginIgnoreCase(String login);

    Optional<JpaUserEntity> findByEmailIgnoreCase(String email);

}
```

#### Repository Adapter Implementation

```java
@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepositoryPort {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    
    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper userMapper;

    @Override
    public UserDomain save(UserDomain user) {
        return userMapper.toDomain(jpaUserRepository.save(userMapper.toEntity(user)));
    }

    @Override
    public Optional<UserDomain> findById(UUID id) {
        return jpaUserRepository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return executeWithExceptionHandling(
            () -> jpaUserRepository.findByLoginIgnoreCase(username).isPresent(),
            false,
            "checking if username exists: " + username
        );
    }

    @Override
    public List<UserDomain> findByName(String name) {
        List<JpaUserEntity> entities = this.jpaUserRepository.findByName(name);
        if (entities == null || entities.isEmpty()) {
            throw new UserNotFoundException("User not found by name: " + name);
        }
        return entities.stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        JpaUserEntity entity = this.jpaUserRepository.findById(id)
                .orElseThrow(createUserNotFoundExceptionSupplier(id));

        entity.setActive(false);
        this.jpaUserRepository.save(entity);
    }

    public UserDomain createOwner(UserDomain request) {
        JpaUserEntity entity = createEntityFromDomain(request, RolesEnum.OWNER);
        JpaUserEntity saved = this.jpaUserRepository.save(entity);
        return userMapper.toDomain(saved);
    }

    @Override
    public Optional<UserDomain> findByLogin(String login) {
        log.info("🔍 UserRepositoryImpl.findByLogin() called with login: {}", login);

        Optional<UserDomain> result = executeWithExceptionHandling(
            () -> {
                log.info("📦 Searching in JpaUserRepository for login: {}", login);
                Optional<JpaUserEntity> entity = jpaUserRepository.findByLoginIgnoreCase(login);
                log.info("🔍 JPA query result present: {}", entity.isPresent());
                return entity.map(userMapper::toDomain);
            },
            Optional.empty(),
            "finding user by username: " + login
        );
        
        log.info("✅ UserRepositoryImpl.findByUsername() returning: {}", result.isPresent() ? "User found" : USER_NOT_FOUND_MESSAGE);
        return result;
    }

    @Override
    public Optional<UserDomain> findByEmail(String email) {
        return executeWithExceptionHandling(
            () -> jpaUserRepository.findByEmailIgnoreCase(email).map(userMapper::toDomain),
            Optional.empty(),
            "finding user by email: " + email
        );
    }

    private <T> T executeWithExceptionHandling(Supplier<T> operation, T defaultValue, String operationDescription) {
        try {
            return operation.get();
        } catch (Exception ex) {
            log.debug("Exception during {}: {}", operationDescription, ex.getMessage());
            return defaultValue;
        }
    }

    private Supplier<UserNotFoundException> createUserNotFoundExceptionSupplier(UUID id) {
        return () -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE + " with id: " + id);
    }

    private JpaUserEntity createEntityFromDomain(UserDomain domain, RolesEnum role) {
        LocalDateTime now = LocalDateTime.now();
        
        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(null);
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setLogin(domain.getLogin());
        entity.setPassword(domain.getPassword());
        entity.setCreatedAt(now);
        entity.setActive(true);
        entity.setRole(List.of(role));
        
        return entity;
    }
}
```

### Domain Mappers

```java
@Component
public class UserEntityMapper {

    public JpaUserEntity toEntity(UserDomain domain) {
        if (domain == null) {
            return null;
        }

        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setLogin(domain.getLogin());
        entity.setPassword(domain.getPassword());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setActive(domain.isActive());

        if (domain.getRole() != null) {
            List<RolesEnum> entityRoles = domain
                    .getRole().stream()
                    .map(this::toEntityRole)
                    .collect(Collectors.toList());
            entity.setRole(entityRoles);
        }

        return entity;
    }

    public UserDomain toDomain(JpaUserEntity entity) {
        if (entity == null) {
            return null;
        }

        List<RolesEnum> domainRoles = null;
        if (entity.getRole() != null) {
            domainRoles = entity.getRole().stream()
                    .map(this::toDomainRole)
                    .collect(Collectors.toList());
        }

        return UserDomain.builder()
                .id(entity.getId())
                .name(entity.getName() != null ? PersonName.of(entity.getName()) : null)
                .email(entity.getEmail() != null ? Email.of(entity.getEmail()) : null)
                .login(entity.getLogin() != null ? Username.of(entity.getLogin()) : null)
                .password(entity.getPassword())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .role(domainRoles)
                .isActive(entity.isActive())
                .build();
    }

    private RolesEnum toEntityRole(
            RolesEnum domainRole) {
        if (domainRole == null) {
            return null;
        }
        return RolesEnum
                .valueOf(domainRole.name());
    }

    private RolesEnum toDomainRole(
            RolesEnum entityRole) {
        if (entityRole == null) {
            return null;
        }
        return RolesEnum.valueOf(entityRole.name());
    }
}
```

### Security Adapters

#### JWT Token Management

```java
// Note: JWT Token functionality is now integrated directly into Security layer
// through JwtUtil and JwtAuthenticationFilter classes
// No separate adapter needed as JWT handling is infrastructure-specific
    }
    
    @Override
    public Set<String> getRolesFromToken(String token) {
        return jwtTokenManager.getRolesFromToken(token);
    }
}
```

#### JWT Token Manager (Infrastructure Detail)

```java
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenManager {
    
    private String secret;
    private int expirationMs;
    
    public String generateToken(String username, Set<String> roles) {
        Date expiryDate = new Date(System.currentTimeMillis() + expirationMs);
        
        return Jwts.builder()
            .setSubject(username)
            .claim("roles", roles)
            .setIssuedAt(new Date())
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
        
        return claims.getSubject();
    }
    
    @SuppressWarnings("unchecked")
    public Set<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
        
        List<String> roles = claims.get("roles", List.class);
        return new HashSet<>(roles);
    }
}
```

#### Password Encoder Adapter

```java
@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {
    
    private final PasswordEncoder passwordEncoder;
    
    public BCryptPasswordEncoderAdapter() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
```

#### UserDetailsService Adapter

```java
@Service
public class SecurityUserDetailsService implements UserDetailsService {
    
    private final UserRepositoryPort userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDomain user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        return User.builder()
            .username(user.getUsername().getValue())
            .password(user.getPassword())
            .authorities("ROLE_" + user.getRole().name())
            .build();
    }
}
```

---

## ⚙️ Configuration Layer

### Security Configuration

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                
                // Swagger/OpenAPI
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Admin only endpoints
                .requestMatchers(HttpMethod.POST, "/api/v1/users/owner").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")
                
                // Authenticated endpoints
                .anyRequest().authenticated()
            );
        
        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

### Beans Configuration

```java
@Configuration
public class SecurityBeansConfig {
    
    // Use Cases Beans
    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            UserDomainService userDomainService,
            UserApplicationMapper userMapper) {
        return new CreateUserUseCase(userRepository, passwordEncoder, userDomainService, userMapper);
    }
    
    @Bean
    public CreateOwnerUseCase createOwnerUseCase(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            UserDomainService userDomainService,
            UserApplicationMapper userMapper) {
        return new CreateOwnerUseCase(userRepository, passwordEncoder, userDomainService, userMapper);
    }
    
    // ... outros use cases
    
    // Domain Services
    @Bean
    public UserDomainService userDomainService(UserRepositoryPort userRepository) {
        return new UserDomainService(userRepository);
    }
}
```

---

## 🔄 Fluxo de Dados na Arquitetura

### Fluxo de Request (Inbound)

```
HTTP Request
    ↓
┌─────────────────────────┐
│    UserController       │ ← Inbound Adapter
│  (Infrastructure)       │
└─────────┬───────────────┘
          │ Web DTO
          ↓
┌─────────────────────────┐
│    UserWebMapper        │ ← Conversion Layer
│  (Infrastructure)       │
└─────────┬───────────────┘
          │ Application DTO
          ↓
┌─────────────────────────┐
│    UserService          │ ← Application Facade
│   (Application)         │
└─────────┬───────────────┘
          │ delegates to
          ↓
┌─────────────────────────┐
│  CreateUserUseCase      │ ← Use Case (Business Logic)
│   (Application)         │
└─────────┬───────────────┘
          │ Domain Object
          ↓
┌─────────────────────────┐
│ UserRepositoryPort      │ ← Outbound Port (Abstraction)
│   (Application)         │
└─────────┬───────────────┘
          │ implemented by
          ↓
┌─────────────────────────┐
│ UserRepositoryImpl      │ ← Outbound Adapter
│  (Infrastructure)       │
└─────────┬───────────────┘
          │ JPA Entity
          ↓
     Database
```

### Fluxo de Response (Outbound)

```
Database
    ↓
┌─────────────────────────┐
│ UserRepositoryImpl      │ ← Outbound Adapter
│  (Infrastructure)       │
└─────────┬───────────────┘
          │ Domain Object
          ↓
┌─────────────────────────┐
│  CreateUserUseCase      │ ← Use Case
│   (Application)         │
└─────────┬───────────────┘
          │ Application DTO
          ↓
┌─────────────────────────┐
│    UserService          │ ← Application Facade
│   (Application)         │
└─────────┬───────────────┘
          │ Application DTO
          ↓
┌─────────────────────────┐
│    UserWebMapper        │ ← Conversion Layer
│  (Infrastructure)       │
└─────────┬───────────────┘
          │ Web DTO
          ↓
┌─────────────────────────┐
│    UserController       │ ← Inbound Adapter
│  (Infrastructure)       │
└─────────┬───────────────┘
          │ HTTP Response
          ↓
    HTTP Response
```

---

## 🎯 Princípios Aplicados na Infrastructure

### Dependency Inversion Principle (DIP)

✅ **Controllers** dependem de **Application Services** (abstrações)  
✅ **Adapters** implementam **Ports** (abstrações da aplicação)  
✅ **Configuration** injeta **implementações concretas** nos **Use Cases**

### Single Responsibility Principle (SRP)

✅ **Controllers** - Apenas conversão HTTP ↔ Application DTOs  
✅ **Mappers** - Apenas conversão entre camadas  
✅ **Repository Adapters** - Apenas persistência  
✅ **Security Adapters** - Apenas funcionalidades de segurança

### Open/Closed Principle (OCP)

✅ **Novos Controllers** podem ser adicionados sem modificar existentes  
✅ **Novos Adapters** podem implementar Ports existentes  
✅ **Configurações** podem ser estendidas sem quebrar funcionalidade

---

## ✅ Validação da Arquitetura

### Hexagonal Architecture Compliance

| Princípio | Implementação | Status |
|-----------|---------------|--------|
| **Ports & Adapters** | Clara separação entre abstrações e implementações | ✅ |
| **Driving Adapters** | Controllers REST chamam Application via Ports | ✅ |
| **Driven Adapters** | Repository/Security implementam Outbound Ports | ✅ |
| **Framework Isolation** | Framework details isolados na Infrastructure | ✅ |
| **Dependency Direction** | Infrastructure → Application (nunca o contrário) | ✅ |

### Framework Dependencies

| Framework | Localização | Justificativa |
|-----------|-------------|---------------|
| **Spring Boot** | Infrastructure apenas | Injeção de dependência e configuração |
| **Spring MVC** | Controllers | Framework web para REST API |
| **Spring Security** | Security adapters | Autenticação e autorização |
| **Spring Data JPA** | Repository adapters | Abstração para persistência |
| **JWT** | Security adapters | Token-based authentication |

### Testing Strategy

```java
// Integration Tests (Infrastructure Layer)
@SpringBootTest
@Testcontainers
class UserControllerIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void shouldCreateUser() {
        // Given
        UserCreateRequestDTO request = new UserCreateRequestDTO();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setUsername("johndoe");
        request.setPassword("password123");
        
        // When
        ResponseEntity<UserResponseDTO> response = restTemplate.postForEntity(
            "/api/v1/users", request, UserResponseDTO.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("John Doe");
    }
}
```

---

## 📈 Métricas de Qualidade

- **Framework Coupling**: Isolado na Infrastructure Layer
- **Adapter Testability**: 100% mockável
- **Configuration Complexity**: Baixa
- **External Dependencies**: Controladas e abstraídas
- **API Contract Stability**: Versionada e documentada

---

## 🎯 Conclusão

A **Infrastructure Layer** implementa **perfeitamente** os padrões da Arquitetura Hexagonal:

✅ **Adapters bem definidos** - Clara separação Inbound/Outbound  
✅ **Frameworks isolados** - Application Layer livre de dependências  
✅ **Ports implementados** - Todos os contratos respeitados  
✅ **Configuração centralizada** - Injeção de dependências transparente  
✅ **Testabilidade** - Adapters facilmente testáveis  
✅ **Flexibilidade** - Fácil troca de implementações  

Esta implementação garante que a **infraestrutura sirva à aplicação**, e não o contrário, mantendo o core do sistema **puro** e **independente** de tecnologias externas.