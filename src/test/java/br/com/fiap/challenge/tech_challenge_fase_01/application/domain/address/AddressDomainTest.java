package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;

class AddressDomainTest {

    private final UUID validUserId = UUID.randomUUID();
    private final String validStreet = "Rua das Flores";
    private final String validNumber = "123";
    private final String validComplement = "Apto 45";
    private final String validNeighborhood = "Centro";
    private final String validCity = "São Paulo";
    private final String validZipCode = "01234567";

    @Test
    @DisplayName("Deve criar endereço com dados válidos")
    void shouldCreateAddressWithValidData() {
        // When
        AddressDomain address = AddressDomain.create(
            validUserId, validStreet, validNumber, validComplement, 
            validNeighborhood, validCity, validZipCode
        );

        // Then
        assertNotNull(address);
        assertEquals(validUserId, address.getUserId());
        assertEquals(validStreet, address.getStreet());
        assertEquals(validNumber, address.getNumber());
        assertEquals(validComplement, address.getComplement());
        assertEquals(validNeighborhood, address.getNeighborhood());
        assertEquals(validCity, address.getCity());
        assertEquals(validZipCode, address.getZipCode());
        assertNotNull(address.getCreatedAt());
        assertNull(address.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve criar endereço sem complemento")
    void shouldCreateAddressWithoutComplement() {
        // When
        AddressDomain address = AddressDomain.create(
            validUserId, validStreet, validNumber, null, 
            validNeighborhood, validCity, validZipCode
        );

        // Then
        assertNotNull(address);
        assertNull(address.getComplement());
    }

    @Test
    @DisplayName("Deve remover espaços em branco dos campos")
    void shouldTrimFields() {
        // When
        AddressDomain address = AddressDomain.create(
            validUserId, "  Rua das Flores  ", "  123  ", "  Apto 45  ", 
            "  Centro  ", "  São Paulo  ", "  01234567  "
        );

        // Then
        assertEquals("Rua das Flores", address.getStreet());
        assertEquals("123", address.getNumber());
        assertEquals("Apto 45", address.getComplement());
        assertEquals("Centro", address.getNeighborhood());
        assertEquals("São Paulo", address.getCity());
        assertEquals("01234567", address.getZipCode());
    }

    @Test
    @DisplayName("Deve lançar exceção quando userId for null")
    void shouldThrowExceptionWhenUserIdIsNull() {
        // When & Then
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(null, validStreet, validNumber, validComplement, 
                validNeighborhood, validCity, validZipCode)
        );
        
        assertEquals("userId", exception.getFieldName());
        assertTrue(exception.getMessage().contains("Usuário é obrigatório"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando street for null ou inválida")
    void shouldThrowExceptionWhenStreetIsInvalid() {
        // Street null
        InvalidFieldException exception1 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, null, validNumber, validComplement, 
                validNeighborhood, validCity, validZipCode)
        );
        assertEquals("street", exception1.getFieldName());
        assertTrue(exception1.getMessage().contains("Rua é obrigatório"));

        // Street too short
        InvalidFieldException exception2 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, "AB", validNumber, validComplement, 
                validNeighborhood, validCity, validZipCode)
        );
        assertEquals("street", exception2.getFieldName());
        assertTrue(exception2.getMessage().contains("Rua deve ter no mínimo 3 caracteres"));

        // Street too long
        String longStreet = "A".repeat(101);
        InvalidFieldException exception3 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, longStreet, validNumber, validComplement, 
                validNeighborhood, validCity, validZipCode)
        );
        assertEquals("street", exception3.getFieldName());
        assertTrue(exception3.getMessage().contains("Rua deve ter no máximo 100 caracteres"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando number for inválido")
    void shouldThrowExceptionWhenNumberIsInvalid() {
        // Number null
        InvalidFieldException exception1 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, validStreet, null, validComplement, 
                validNeighborhood, validCity, validZipCode)
        );
        assertEquals("number", exception1.getFieldName());
        assertTrue(exception1.getMessage().contains("Número é obrigatório"));

        // Number too long
        String longNumber = "A".repeat(21);
        InvalidFieldException exception2 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, validStreet, longNumber, validComplement, 
                validNeighborhood, validCity, validZipCode)
        );
        assertEquals("number", exception2.getFieldName());
        assertTrue(exception2.getMessage().contains("Número deve ter no máximo 20 caracteres"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando complement for muito longo")
    void shouldThrowExceptionWhenComplementIsTooLong() {
        String longComplement = "A".repeat(51);
        
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, validStreet, validNumber, longComplement, 
                validNeighborhood, validCity, validZipCode)
        );
        
        assertEquals("complement", exception.getFieldName());
        assertTrue(exception.getMessage().contains("Complemento deve ter no máximo 50 caracteres"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando neighborhood for inválido")
    void shouldThrowExceptionWhenNeighborhoodIsInvalid() {
        // Neighborhood null
        InvalidFieldException exception1 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, validStreet, validNumber, validComplement, 
                null, validCity, validZipCode)
        );
        assertEquals("neighborhood", exception1.getFieldName());
        assertTrue(exception1.getMessage().contains("Bairro é obrigatório"));

        // Neighborhood too short
        InvalidFieldException exception2 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, validStreet, validNumber, validComplement, 
                "A", validCity, validZipCode)
        );
        assertEquals("neighborhood", exception2.getFieldName());
        assertTrue(exception2.getMessage().contains("Bairro deve ter no mínimo 2 caracteres"));

        // Neighborhood too long
        String longNeighborhood = "A".repeat(51);
        InvalidFieldException exception3 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, validStreet, validNumber, validComplement, 
                longNeighborhood, validCity, validZipCode)
        );
        assertEquals("neighborhood", exception3.getFieldName());
        assertTrue(exception3.getMessage().contains("Bairro deve ter no máximo 50 caracteres"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando city for inválida")
    void shouldThrowExceptionWhenCityIsInvalid() {
        // City null
        InvalidFieldException exception1 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, validStreet, validNumber, validComplement, 
                validNeighborhood, null, validZipCode)
        );
        assertEquals("city", exception1.getFieldName());
        assertTrue(exception1.getMessage().contains("Cidade é obrigatório"));

        // City too short
        InvalidFieldException exception2 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, validStreet, validNumber, validComplement, 
                validNeighborhood, "A", validZipCode)
        );
        assertEquals("city", exception2.getFieldName());
        assertTrue(exception2.getMessage().contains("Cidade deve ter no mínimo 2 caracteres"));

        // City too long
        String longCity = "A".repeat(51);
        InvalidFieldException exception3 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, validStreet, validNumber, validComplement, 
                validNeighborhood, longCity, validZipCode)
        );
        assertEquals("city", exception3.getFieldName());
        assertTrue(exception3.getMessage().contains("Cidade deve ter no máximo 50 caracteres"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando zipCode for inválido")
    void shouldThrowExceptionWhenZipCodeIsInvalid() {
        // ZipCode null
        InvalidFieldException exception1 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, validStreet, validNumber, validComplement, 
                validNeighborhood, validCity, null)
        );
        assertEquals("zipCode", exception1.getFieldName());
        assertTrue(exception1.getMessage().contains("CEP é obrigatório"));

        // ZipCode too short
        InvalidFieldException exception2 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, validStreet, validNumber, validComplement, 
                validNeighborhood, validCity, "1234567")
        );
        assertEquals("zipCode", exception2.getFieldName());
        assertTrue(exception2.getMessage().contains("CEP deve ter no mínimo 8 caracteres"));

        // ZipCode too long
        InvalidFieldException exception3 = assertThrows(InvalidFieldException.class, () ->
            AddressDomain.create(validUserId, validStreet, validNumber, validComplement, 
                validNeighborhood, validCity, "12345678901")
        );
        assertEquals("zipCode", exception3.getFieldName());
        assertTrue(exception3.getMessage().contains("CEP deve ter no máximo 10 caracteres"));
    }

    @Test
    @DisplayName("Deve atualizar informações do endereço")
    void shouldUpdateAddressInfo() {
        // Given
        AddressDomain address = AddressDomain.create(
            validUserId, validStreet, validNumber, validComplement, 
            validNeighborhood, validCity, validZipCode
        );
        
        String newStreet = "Rua Nova";
        String newNumber = "456";
        String newComplement = "Casa";
        String newNeighborhood = "Jardim";
        String newCity = "Rio de Janeiro";
        String newZipCode = "87654321";

        // When
        address.updateInfo(newStreet, newNumber, newComplement, newNeighborhood, newCity, newZipCode);

        // Then
        assertEquals(newStreet, address.getStreet());
        assertEquals(newNumber, address.getNumber());
        assertEquals(newComplement, address.getComplement());
        assertEquals(newNeighborhood, address.getNeighborhood());
        assertEquals(newCity, address.getCity());
        assertEquals(newZipCode, address.getZipCode());
        assertNotNull(address.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve validar dados ao atualizar informações")
    void shouldValidateDataWhenUpdating() {
        // Given
        AddressDomain address = AddressDomain.create(
            validUserId, validStreet, validNumber, validComplement, 
            validNeighborhood, validCity, validZipCode
        );

        // When & Then
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () ->
            address.updateInfo(null, validNumber, validComplement, validNeighborhood, validCity, validZipCode)
        );
        
        assertEquals("street", exception.getFieldName());
        assertTrue(exception.getMessage().contains("Rua é obrigatório"));
    }

    @Test
    @DisplayName("Deve implementar equals e hashCode corretamente")
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // Given
        UUID addressId = UUID.randomUUID();
        AddressDomain address1 = AddressDomain.builder()
            .id(addressId)
            .userId(validUserId)
            .street(validStreet)
            .build();
        
        AddressDomain address2 = AddressDomain.builder()
            .id(addressId)
            .userId(UUID.randomUUID())
            .street("Different Street")
            .build();
        
        AddressDomain address3 = AddressDomain.builder()
            .id(UUID.randomUUID())
            .userId(validUserId)
            .street(validStreet)
            .build();

        // Then
        assertEquals(address1, address2); // Same ID
        assertNotEquals(address1, address3); // Different ID
        assertEquals(address1.hashCode(), address2.hashCode());
        assertNotEquals(address1.hashCode(), address3.hashCode());
    }

    @Test
    @DisplayName("Deve implementar toString corretamente")
    void shouldImplementToStringCorrectly() {
        // Given
        UUID addressId = UUID.randomUUID();
        AddressDomain address = AddressDomain.builder()
            .id(addressId)
            .userId(validUserId)
            .street(validStreet)
            .number(validNumber)
            .complement(validComplement)
            .neighborhood(validNeighborhood)
            .city(validCity)
            .zipCode(validZipCode)
            .build();

        // When
        String toString = address.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("AddressDomain"));
        assertTrue(toString.contains(addressId.toString()));
        assertTrue(toString.contains(validUserId.toString()));
        assertTrue(toString.contains(validStreet));
        assertTrue(toString.contains(validNumber));
        assertTrue(toString.contains(validComplement));
        assertTrue(toString.contains(validNeighborhood));
        assertTrue(toString.contains(validCity));
        assertTrue(toString.contains(validZipCode));
    }
}