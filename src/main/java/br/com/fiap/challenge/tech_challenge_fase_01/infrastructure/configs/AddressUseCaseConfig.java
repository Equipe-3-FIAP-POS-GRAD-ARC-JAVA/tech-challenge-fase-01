package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.configs;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service.AddressDomainService;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service.UserDomainService;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressCreatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressDeletePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressFindByUserPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.address.CreateAddressUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.address.DeleteAddressUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.address.FindAddressUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddressUseCaseConfig {

    @Bean
    public AddressDomainService addressDomainService(AddressRepositoryPort addressRepositoryPort) {
        return new AddressDomainService(addressRepositoryPort);
    }

    @Bean
    public AddressFindByUserPort addressFindByUserPort(
            AddressRepositoryPort addressRepositoryPort) {
        return new FindAddressUseCase(addressRepositoryPort);
    }

    @Bean
    public AddressCreatePort addressCreatePort(
            AddressRepositoryPort addressRepository) {
        return new CreateAddressUseCase(addressRepository);
    }

    @Bean
    public AddressDeletePort addressDeletePort(AddressRepositoryPort addressRepositoryPort,
                                               AddressDomainService addressDomainService) {
        return new DeleteAddressUseCase(addressRepositoryPort, addressDomainService);
    }
}
