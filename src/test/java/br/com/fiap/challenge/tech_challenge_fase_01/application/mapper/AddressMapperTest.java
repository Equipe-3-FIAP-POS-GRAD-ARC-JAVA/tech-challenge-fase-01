package br.com.fiap.challenge.tech_challenge_fase_01.application.mapper;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address.AddressDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AddressMapperTest {

    @Test
    void toResponse_shouldMap(){
        AddressDomain d = AddressDomain.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .street("Rua A")
                .number("99")
                .city("SP")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();

        AddressResponse r = AddressMapper.toResponse(d);
        assertNotNull(r);
        assertEquals(d.getStreet(), r.street());
        assertEquals(d.getNumber(), r.number());
        assertEquals(d.getCity(), r.city());
        assertEquals(d.getUserId(), r.userId());
    }

    @Test
    void toResponse_whenNull_shouldReturnNull(){
        assertNull(AddressMapper.toResponse(null));
    }

    @Test
    void toResponseList_shouldMapList(){
        var list = List.of(
                AddressDomain.builder().id(UUID.randomUUID()).userId(UUID.randomUUID()).street("A").number("1").city("SP").build(),
                AddressDomain.builder().id(UUID.randomUUID()).userId(UUID.randomUUID()).street("B").number("2").city("RJ").build()
        );
        var res = AddressMapper.toResponseList(list);
        assertEquals(2, res.size());
    }

    @Test
    void toResponseList_whenEmpty_shouldReturnEmpty(){
        assertTrue(AddressMapper.toResponseList(null).isEmpty());
        assertTrue(AddressMapper.toResponseList(List.of()).isEmpty());
    }
}
