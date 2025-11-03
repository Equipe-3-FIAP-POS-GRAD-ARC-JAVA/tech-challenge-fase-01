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

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String BASE_PROBLEM_TYPE = "/problems";
    private static final String TIMESTAMP_PROPERTY = "timestamp";
    private static final String ERROR_TYPE_PROPERTY = "errorType";

    @ExceptionHandler(InvalidFieldException.class)
    public ProblemDetail handleInvalidFieldException(InvalidFieldException ex, WebRequest request) {
        ProblemDetail problemDetail = createBaseProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage(), "/invalid-field",
                "Campo Inválido", "VALIDATION_ERROR", request);

        problemDetail.setProperty("fieldName", ex.getFieldName());
        return problemDetail;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRuleException(BusinessRuleException ex, WebRequest request) {
        return createBaseProblemDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), "/business-rule-violation",
                "Regra de Negócio Violada", "BUSINESS_RULE_VIOLATION", request);
    }

    @ExceptionHandler(DomainValidationException.class)
    public ProblemDetail handleDomainValidationException(DomainValidationException ex, WebRequest request) {
        return createBaseProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage(), "/domain-validation",
                "Validação de Domínio", "DOMAIN_VALIDATION_ERROR", request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        return createBaseProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), "/not-found", "Usuário Não Encontrado",
                "RESOURCE_NOT_FOUND", request);
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
    public ProblemDetail handleAccessDeniedException(
            org.springframework.security.authorization.AuthorizationDeniedException ex, WebRequest request) {
        return createBaseProblemDetail(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar este recurso.",
                "/access-denied", "Acesso Negado", "ACCESS_DENIED", request);
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
        System.err.println("JpaSystemException caught: " + ex.getMessage());
        System.err.println("Cause: " + ex.getCause());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
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

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno. Por favor, tente novamente mais tarde.");

        problemDetail.setType(URI.create(BASE_PROBLEM_TYPE + "/internal-server-error"));
        problemDetail.setTitle("Erro Interno do Servidor");
        problemDetail.setInstance(getRequestUri(request));
        problemDetail.setProperty("timestamp", getTimestamp());
        problemDetail.setProperty("errorType", "INTERNAL_SERVER_ERROR");

        return problemDetail;
    }

    private ProblemDetail createBaseProblemDetail(HttpStatus status, String detail, String typeSubpath, String title,
            String errorType, WebRequest request) {

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
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

}