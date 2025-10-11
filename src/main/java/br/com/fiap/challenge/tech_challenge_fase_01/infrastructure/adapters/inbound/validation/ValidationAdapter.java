package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.validation;

import java.util.Set;

import org.springframework.stereotype.Component;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

/**
 * Adapter para validação de entrada seguindo princípios SOLID.
 * 
 * Responsabilidades (SRP):
 * - Validar objetos usando Bean Validation (JSR-303)
 * - Converter violações em exceções de domínio apropriadas
 * - Fornecer interface simplificada para validação
 * 
 * Arquitetura Hexagonal:
 * - Adapter Inbound para validação
 * - Converte detalhes técnicos de validação para exceções de domínio
 * - Mantém a camada de aplicação independente de JSR-303
 * 
 * Princípios SOLID aplicados:
 * - SRP: Responsabilidade única de validação
 * - OCP: Extensível para novos tipos de validação
 * - DIP: Depende da abstração Validator
 */
@Component
@RequiredArgsConstructor
public class ValidationAdapter {

    private final Validator validator;

    /**
     * Valida um objeto e lança exceção de domínio se houver violações.
     * 
     * @param object objeto a ser validado
     * @param <T> tipo do objeto
     * @throws InvalidFieldException se houver violações de validação
     */
    public <T> void validateAndThrow(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        
        if (!violations.isEmpty()) {
            ConstraintViolation<T> firstViolation = violations.iterator().next();
            String fieldName = firstViolation.getPropertyPath().toString();
            String message = firstViolation.getMessage();
            
            throw new InvalidFieldException(fieldName, message);
        }
    }

    /**
     * Valida um objeto e retorna verdadeiro se for válido.
     * 
     * @param object objeto a ser validado
     * @param <T> tipo do objeto
     * @return true se válido, false caso contrário
     */
    public <T> boolean isValid(T object) {
        return validator.validate(object).isEmpty();
    }

    /**
     * Retorna todas as violações de validação para um objeto.
     * 
     * @param object objeto a ser validado
     * @param <T> tipo do objeto
     * @return conjunto de violações
     */
    public <T> Set<ConstraintViolation<T>> getViolations(T object) {
        return validator.validate(object);
    }
}