package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressDomain {

    private static final int STREET_MIN = 3, STREET_MAX = 100;
    private static final int NUMBER_MIN = 0, NUMBER_MAX = 20;
    private static final int COMPLEMENT_MIN = 0, COMPLEMENT_MAX = 50;
    private static final int NEIGHBORHOOD_MIN = 2, NEIGHBORHOOD_MAX = 50;
    private static final int CITY_MIN = 2, CITY_MAX = 50;
    private static final int ZIP_CODE_MIN = 8, ZIP_CODE_MAX = 10;

    private UUID id;
    private UUID userId;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String zipCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AddressDomain create(UUID userId, String street, String number, String complement, String neighborhood, String city, String zipCode) {
        validate(userId, street, number, complement, neighborhood, city, zipCode);
        return AddressDomain.builder()
                .userId(userId)
                .street(street.trim())
                .number(number.trim())
                .complement(complement != null ? complement.trim() : null)
                .neighborhood(neighborhood.trim())
                .city(city.trim())
                .zipCode(zipCode.trim())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static void validate(UUID userId, String street, String number, String complement, String neighborhood, String city, String zipCode) {
        if (userId == null) throw new InvalidFieldException("userId", "Usuário é obrigatório");
        checkLen("street", street, STREET_MIN, STREET_MAX, "Rua");
        checkLen("number", number, NUMBER_MIN, NUMBER_MAX, "Número");
        checkOptionalLen("complement", complement, COMPLEMENT_MIN, COMPLEMENT_MAX, "Complemento");
        checkLen("neighborhood", neighborhood, NEIGHBORHOOD_MIN, NEIGHBORHOOD_MAX, "Bairro");
        checkLen("city", city, CITY_MIN, CITY_MAX, "Cidade");
        checkLen("zipCode", zipCode, ZIP_CODE_MIN, ZIP_CODE_MAX, "CEP");
    }

    private static void checkLen(String field, String value, int min, int max, String label) {
        if (value == null)
            throw new InvalidFieldException(field, label + " é obrigatório");
        var v = value.trim();
        if (v.length() < min)
            throw new InvalidFieldException(field, label + " deve ter no mínimo " + min + " caracteres");
        if (v.length() > max)
            throw new InvalidFieldException(field, label + " deve ter no máximo " + max + " caracteres");
    }

    private static void checkOptionalLen(String field, String value, int min, int max, String label) {
        if (value != null) {
            var v = value.trim();
            if (v.length() < min)
                throw new InvalidFieldException(field, label + " deve ter no mínimo " + min + " caracteres");
            if (v.length() > max)
                throw new InvalidFieldException(field, label + " deve ter no máximo " + max + " caracteres");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDomain that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AddressDomain{id=" + id + ", userId=" + userId + ", street='" + street + "', number='" + number + 
               "', complement='" + complement + "', neighborhood='" + neighborhood + "', city='" + city + "', zipCode='" + zipCode + "'}";
    }

    public void updateInfo(String street, String number, String complement, String neighborhood, String city, String zipCode) {
        validate(this.userId, street, number, complement, neighborhood, city, zipCode);
        this.street = street.trim();
        this.number = number.trim();
        this.complement = complement != null ? complement.trim() : null;
        this.neighborhood = neighborhood.trim();
        this.city = city.trim();
        this.zipCode = zipCode.trim();
        this.updatedAt = LocalDateTime.now();
    }
}
