package au.com.demo.clientservice.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import au.com.demo.clientservice.client.v1.model.Client;
import au.com.demo.clientservice.entity.ClientEntity;
import au.com.demo.clientservice.repository.ClientRepository;
import au.com.demo.clientservice.service.impl.ClientServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientServiceImpl;

    @Test
    void getClient_success() {
        ClientEntity clientEntity = ClientEntity.builder()
                .name("Bob")
                .email("abc@pro.com")
                .build();

        Client client = new Client();
        client.setName("Bob");
        client.setEmail("abc@pro.com");
        client.setId("1");

        when(clientRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(clientEntity));

        Client client1 = clientServiceImpl.getClient("1");

        assertThat(client1.getName()).isEqualTo("Bob");
        assertThat(client1.getEmail()).isEqualTo("abc@pro.com");
    }

    @Test
    void addClient_success() {
        ClientEntity clientEntity = ClientEntity.builder()
                .id(1L)
                .name("Bob")
                .email("abc@pro.com")
                .build();

        Client client = new Client();
        client.setName("Bob");
        client.setEmail("abc@pro.com");
        client.setId("1");

        when(clientRepository.save(ArgumentMatchers.any())).thenReturn(clientEntity);

        Client client1 = clientServiceImpl.addClient(client);

        assertThat(client1.getId()).isEqualTo("1");
        assertThat(client1.getName()).isEqualTo("Bob");
        assertThat(client1.getEmail()).isEqualTo("abc@pro.com");
    }

    @Test
    void updateClient_success() {
        ClientEntity clientEntity = ClientEntity.builder()
                .id(1L)
                .name("Bob")
                .email("abc@pro.com")
                .build();

        Client client = new Client();
        client.setName("BobNew");
        client.setEmail("abc_new@pro.com");
        client.setId("1");

        when(clientRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(clientEntity));
        when(clientRepository.save(ArgumentMatchers.any())).thenReturn(clientEntity);

        Client client1 = clientServiceImpl.updateClient(client);

        assertThat(client1.getId()).isEqualTo("1");
        assertThat(client1.getName()).isEqualTo("BobNew");
        assertThat(client1.getEmail()).isEqualTo("abc_new@pro.com");
    }

    @Test
    void deleteClient_success() {
        ClientEntity clientEntity = ClientEntity.builder()
                .id(1L)
                .name("Bob")
                .email("abc@pro.com")
                .build();

        when(clientRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(clientEntity));

        String ss = clientServiceImpl.deleteClient("1");
        assertThat(ss).isEqualTo("Deleted client with ID 1");
    }
}
