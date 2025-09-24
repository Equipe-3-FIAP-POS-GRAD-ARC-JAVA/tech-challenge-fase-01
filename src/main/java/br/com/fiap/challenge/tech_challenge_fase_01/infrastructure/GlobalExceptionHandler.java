package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;



@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException ex) {
        return this.buildProblemDetail(ex.getStatusCode(), ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
        return this.buildProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ProblemDetail buildProblemDetail(HttpStatusCode statusCode, String message) {
        var problemDetail = ProblemDetail.forStatusAndDetail(statusCode, message);
        problemDetail.setProperty("timestamp", this.getDateTimeFormatted());
        return problemDetail;
    }

    private String getDateTimeFormatted() {
        var now = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(now);
    }

}
