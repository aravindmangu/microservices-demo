package au.com.demo.clientservice.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import au.com.demo.clientservice.entity.ClientEntity;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveClientTest(){

        ClientEntity client = ClientEntity.builder()
                .name("Bob")
                .email("abc@pro.com")
                .build();

        clientRepository.save(client);

        Assertions.assertThat(client.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getClientTest(){
        ClientEntity client = clientRepository.findById(1L).get();

        Assertions.assertThat(client.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    public void updateClientTest(){
        ClientEntity client = clientRepository.findById(1L).get();
        client.setEmail("dev@pro.com");

        ClientEntity clientUpdated =  clientRepository.save(client);

        Assertions.assertThat(clientUpdated.getEmail()).isEqualTo("dev@pro.com");
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void deleteClientTest(){
        clientRepository.deleteById(1L);

        Optional<ClientEntity> optionalRecord = clientRepository.findById(1L);

        Assertions.assertThat(optionalRecord).isEmpty();
    }
}
