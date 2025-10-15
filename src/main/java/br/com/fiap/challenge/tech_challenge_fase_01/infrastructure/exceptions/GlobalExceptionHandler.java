package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.exceptions;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.BusinessRuleException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.DomainValidationException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserAlreadyExistsException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;

/**
 * Global Exception Handler para toda a aplicação.
 * 
 * Seguindo RFC 7807 - Problem Details for HTTP APIs:
 * - type: URI que identifica o tipo do problema
 * - title: Título legível do problema
 * - status: Código HTTP
 * - detail: Explicação detalhada específica para esta ocorrência
 * - instance: URI que identifica a ocorrência específica (path da requisição)
 * - timestamp: Data/hora do erro
 * - errorType: Categoria do erro (custom property)
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String BASE_PROBLEM_TYPE = "/problems";

    // ═══════════════════════════════════════════════════════════════
    // EXCEÇÕES DE DOMÍNIO - Domain Layer
    // ═══════════════════════════════════════════════════════════════

    /**
     * Trata exceções de validação de campo inválido.
     * HTTP 400 - Bad Request
     * 
     * Cenários:
     * - Email inválido
     * - Nome com formato incorreto
     * - Login fora dos padrões
     * - Senha sem complexidade mínima
     */
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

    /**
     * Trata exceções de regras de negócio violadas.
     * HTTP 422 - Unprocessable Entity
     * 
     * Cenários:
     * - Tentar atualizar usuário inativo
     * - Tentar ativar usuário já ativo
     * - Senha nova igual à senha atual
     * - Operação sem permissão necessária
     */
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

    /**
     * Trata exceções genéricas de validação de domínio.
     * HTTP 400 - Bad Request
     * 
     * Fallback para validações que não são InvalidFieldException nem
     * BusinessRuleException
     */
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

    // ═══════════════════════════════════════════════════════════════
    // EXCEÇÕES DE APLICAÇÃO - Application Layer
    // ═══════════════════════════════════════════════════════════════

    /**
     * Trata exceções de usuário não encontrado.
     * HTTP 404 - Not Found
     */
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

    /**
     * Trata exceções de usuário já existente (duplicação).
     * HTTP 409 - Conflict
     * 
     * Cenários:
     * - Username já cadastrado
     * - Email já cadastrado
     */
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

    // ═══════════════════════════════════════════════════════════════
    // EXCEÇÕES DE INFRAESTRUTURA - Infrastructure Layer
    // ═══════════════════════════════════════════════════════════════

    /**
     * Trata exceções genéricas de recurso não encontrado.
     * HTTP 404 - Not Found
     */
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

    /**
     * Trata exceções de não autorizado.
     * HTTP 401 - Unauthorized
     */
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

    /**
     * Trata exceções de acesso negado (autorização).
     * HTTP 403 - Forbidden
     */
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

    // ═══════════════════════════════════════════════════════════════
    // EXCEÇÕES GENÉRICAS - Fallback
    // ═══════════════════════════════════════════════════════════════

    /**
     * Trata exceções genéricas de argumento ilegal.
     * HTTP 400 - Bad Request
     * 
     * NOTA: Esta exceção é genérica e deve ser evitada.
     * Preferir exceções de domínio específicas.
     */
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

    /**
     * Trata exceções do sistema JPA (incluindo PSQLException).
     * HTTP 500 - Internal Server Error
     * 
     * Captura erros de baixo nível do JPA/Hibernate, incluindo PSQLException
     * como "No results were returned by the query" que podem ocorrer
     * durante validações de existência de usuários.
     */
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

    /**
     * Trata exceções genéricas não capturadas.
     * HTTP 500 - Internal Server Error
     * 
     * IMPORTANTE: Este handler deve ser o último recurso.
     * Em produção, não expor detalhes internos.
     */
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

        // Em desenvolvimento, pode adicionar mais detalhes
        // problemDetail.setProperty("exceptionClass", ex.getClass().getSimpleName());

        return problemDetail;
    }

    // ═══════════════════════════════════════════════════════════════
    // MÉTODOS AUXILIARES - RFC 7807
    // ═══════════════════════════════════════════════════════════════

    // ═══════════════════════════════════════════════════════════════
    // MÉTODOS UTILITÁRIOS - DRY Principle
    // ═══════════════════════════════════════════════════════════════

    /**
     * Factory method para criar ProblemDetail base.
     * Elimina duplicação de código e padroniza estrutura RFC 7807.
     * Aplica DRY (Don't Repeat Yourself).
     */
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

    // Constantes para evitar duplicação de strings
    private static final String TIMESTAMP_PROPERTY = "timestamp";
    private static final String ERROR_TYPE_PROPERTY = "errorType";
}
