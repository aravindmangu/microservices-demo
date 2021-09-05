package au.com.demo.clientservice.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.demo.clientservice.client.v1.model.Client;
import au.com.demo.clientservice.entity.ClientEntity;
import au.com.demo.clientservice.exceptions.InvalidRequestException;
import au.com.demo.clientservice.exceptions.RecordNotFoundException;
import au.com.demo.clientservice.service.ClientService;

@WebMvcTest(ClientController.class)
class ClientControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ClientService clientService;

    @MockBean
    private Tracer tracer;

    ClientEntity client1 = ClientEntity.builder()
            .id(1l)
            .name("Bob")
            .email("bob@pro.com")
            .build();

    ClientEntity client2 = ClientEntity.builder()
            .id(2l)
            .name("Stuart")
            .email("stuart@pro.com")
            .build();

    ClientEntity client3 = ClientEntity.builder()
            .id(3l)
            .name("Kevin")
            .email("kevin@pro.com")
            .build();

    @Test
    void getClient_success() throws Exception {
        Client client = new Client();
        client.setName("Bob");
        client.setEmail("");
        client.setId("");

        Mockito.when(clientService.getClient(ArgumentMatchers.any())).thenReturn(client);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/client/getClient/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Bob")));
    }

    @Test
    void getClient_not_found() throws Exception {

        Mockito.when(clientService.getClient(ArgumentMatchers.any())).thenThrow(new RecordNotFoundException("Client with ID 8 does not exist."));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/client/getClient/8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result ->
                        assertEquals("Client with ID 8 does not exist.", result.getResolvedException().getMessage()));
    }

    @Test
    void createClient_success() throws Exception {

        Client clientRecord = new Client();
        clientRecord.setId(client2.getId()+"");
        clientRecord.setName(client2.getName());
        clientRecord.setEmail(client2.getEmail());

        Mockito.when(clientService.addClient(ArgumentMatchers.any())).thenReturn(clientRecord);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/client/addClient")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientRecord));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is("2")))
                .andExpect(jsonPath("$.name", is("Stuart")));
    }

    @Test
    void updateClient_methodNotAllowed() throws Exception {
        Client clientRecord = new Client();
        clientRecord.setId(client3.getId()+"");
        clientRecord.setName(client3.getName()+"1");
        clientRecord.setEmail(client3.getEmail());

        Mockito.when(clientService.updateClient(ArgumentMatchers.any())).thenReturn(clientRecord);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/client/updateClient")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientRecord));

        mockMvc.perform(mockRequest)
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void updateClient_success() throws Exception {
        Client clientRecord = new Client();
        clientRecord.setId(client3.getId()+"");
        clientRecord.setName(client3.getName()+"1");
        clientRecord.setEmail(client3.getEmail());

        Mockito.when(clientService.updateClient(ArgumentMatchers.any())).thenReturn(clientRecord);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/client/updateClient")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientRecord));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is("3")))
                .andExpect(jsonPath("$.name", is("Kevin1")));
    }

    @Test
    void updateClient_nullId() throws Exception {
        Client clientRecord = new Client();
        clientRecord.setId(null);
        clientRecord.setName(client3.getName());
        clientRecord.setEmail(client3.getEmail());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/client/updateClient")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(clientRecord));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof InvalidRequestException))
                .andExpect(result ->
                        assertEquals("Client payload or ID must not be null", result.getResolvedException().getMessage()));
    }

    @Test
    void deleteClient_success() throws Exception {
        Mockito.when(clientService.deleteClient(ArgumentMatchers.any())).thenReturn("Deleted client with ID 2");

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/client/deleteClient/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(result ->
                        assertEquals("\"Deleted client with ID 2\"", result.getResponse().getContentAsString()));
    }
}
