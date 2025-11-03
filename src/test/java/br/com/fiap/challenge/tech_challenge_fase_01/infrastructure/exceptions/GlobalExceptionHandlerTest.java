package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.exceptions;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.BusinessRuleException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.DomainValidationException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ProblemDetail;
import org.springframework.web.context.request.WebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private WebRequest request;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        handler = new GlobalExceptionHandler();
        when(request.getDescription(false)).thenReturn("uri=/api/test");
    }

    @Test
    void handleInvalidFieldException_setsFieldAndDetails() {
        InvalidFieldException ex = new InvalidFieldException("fieldX", "Campo X inválido");
        ProblemDetail pd = handler.handleInvalidFieldException(ex, request);

        assertThat(pd.getStatus()).isEqualTo(400);
        assertThat(pd.getTitle()).isEqualTo("Campo Inválido");
        assertThat(pd.getType().toString()).contains("/invalid-field");
        assertThat(pd.getProperties().get("fieldName")).isEqualTo("fieldX");
        assertThat(pd.getProperties().get("errorType")).isEqualTo("VALIDATION_ERROR");
        assertThat(pd.getInstance().toString()).isEqualTo("/api/test");
    }

    @Test
    void handleBusinessRuleException_setsUnprocessable() {
        BusinessRuleException ex = new BusinessRuleException("regra");
        ProblemDetail pd = handler.handleBusinessRuleException(ex, request);

        assertThat(pd.getStatus()).isEqualTo(422);
        assertThat(pd.getProperties().get("errorType")).isEqualTo("BUSINESS_RULE_VIOLATION");
    }

    @Test
    void handleDomainValidationException_setsBadRequest() {
        DomainValidationException ex = new DomainValidationException("dom");
        ProblemDetail pd = handler.handleDomainValidationException(ex, request);

        assertThat(pd.getStatus()).isEqualTo(400);
        assertThat(pd.getProperties().get("errorType")).isEqualTo("DOMAIN_VALIDATION_ERROR");
    }

    @Test
    void handleUserNotFound_sets404() {
        UserNotFoundException ex = new UserNotFoundException("no user");
        ProblemDetail pd = handler.handleUserNotFoundException(ex, request);

        assertThat(pd.getStatus()).isEqualTo(404);
        assertThat(pd.getProperties().get("errorType")).isEqualTo("RESOURCE_NOT_FOUND");
    }
}
