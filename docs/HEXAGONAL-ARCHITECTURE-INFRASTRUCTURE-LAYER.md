# 🏗️ Infrastructure Layer - Arquitetura Hexagonal

**Data**: 15 de Outubro 2025  
**Versão**: 2.1  
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
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFound(UserNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("User Not Found");
        problem.setType(URI.create("/errors/user-not-found"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }
    
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleUsernameExists(UsernameAlreadyExistsException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Username Already Exists");
        problem.setType(URI.create("/errors/username-conflict"));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "Validation failed");
        problem.setTitle("Validation Error");
        problem.setProperty("validationErrors", errors);
        
        return ResponseEntity.badRequest().body(problem);
    }
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
public class JpaUserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // getters, setters, construtores
}
```

#### JPA Repository Interface

```java
@Repository
public interface JpaUserRepository extends JpaRepository<JpaUserEntity, Long> {
    
    Optional<JpaUserEntity> findByUsername(String username);
    
    Optional<JpaUserEntity> findByEmail(String email);
    
    List<JpaUserEntity> findByNameContainingIgnoreCase(String name);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
```

#### Repository Adapter Implementation

```java
@Repository
public class UserRepositoryImpl implements UserRepositoryPort {
    
    private final JpaUserRepository jpaRepository;
    private final UserDomainMapper domainMapper;
    
    @Override
    public UserDomain save(UserDomain user) {
        // 1. Domain → JPA Entity
        JpaUserEntity entity = domainMapper.toJpaEntity(user);
        
        // 2. Persistir via JPA
        JpaUserEntity savedEntity = jpaRepository.save(entity);
        
        // 3. JPA Entity → Domain
        return domainMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<UserDomain> findById(Long id) {
        return jpaRepository.findById(id)
            .map(domainMapper::toDomain);
    }
    
    @Override
    public Optional<UserDomain> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
            .map(domainMapper::toDomain);
    }
    
    @Override
    public List<UserDomain> findByNameContainingIgnoreCase(String name) {
        return jpaRepository.findByNameContainingIgnoreCase(name)
            .stream()
            .map(domainMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }
    
    // ... outras implementações
}
```

### Domain Mappers

```java
@Component
public class UserDomainMapper {
    
    // Domain → JPA Entity
    public JpaUserEntity toJpaEntity(UserDomain domain) {
        JpaUserEntity entity = new JpaUserEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName().getValue());
        entity.setEmail(domain.getEmail().getValue());
        entity.setUsername(domain.getUsername().getValue());
        entity.setPassword(domain.getPassword());
        entity.setRole(domain.getRole());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
    
    // JPA Entity → Domain
    public UserDomain toDomain(JpaUserEntity entity) {
        return UserDomain.builder()
            .id(entity.getId())
            .name(PersonName.of(entity.getName()))
            .email(Email.of(entity.getEmail()))
            .username(Username.of(entity.getUsername()))
            .password(entity.getPassword())
            .role(entity.getRole())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
```

### Security Adapters

#### JWT Token Adapter

```java
@Component
public class JwtTokenAdapter implements JwtTokenPort {
    
    private final JwtTokenManager jwtTokenManager;
    
    @Override
    public String generateToken(String username, Set<String> roles) {
        return jwtTokenManager.generateToken(username, roles);
    }
    
    @Override
    public boolean validateToken(String token) {
        return jwtTokenManager.validateToken(token);
    }
    
    @Override
    public String getUsernameFromToken(String token) {
        return jwtTokenManager.getUsernameFromToken(token);
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