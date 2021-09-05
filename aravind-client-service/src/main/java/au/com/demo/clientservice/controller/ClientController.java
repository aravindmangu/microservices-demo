package au.com.demo.clientservice.controller;

import java.util.HashMap;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import au.com.demo.clientservice.client.v1.api.ClientApi;
import au.com.demo.clientservice.client.v1.model.Client;
import au.com.demo.clientservice.exceptions.InvalidRequestException;
import au.com.demo.clientservice.service.ClientService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Log4j2
@Api(value = "/client", tags = { "Client Management API" })
public class ClientController extends AbstractController implements ClientApi {

    private HashMap<String, Client> cache = new HashMap<>();

    private final ClientService clientService;

    @Override
    @RequestMapping(value = "/getClient/{id}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<Client> getClient(@PathVariable("id") String id, String xAuthUser) {
        if (cache.containsKey(id)) {
            return new ResponseEntity<>(writeToResponse(cache.get(id)));
        }

        Client cl = clientService.getClient(id);
        cache.put(cl.getId(), cl);
        return new ResponseEntity<>(writeToResponse(cl));
    }

    @Override
    @RequestMapping(value = "/addClient",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    public ResponseEntity<Client> addClient(@Valid @RequestBody Client client, String xAuthUser) {
        Client cl = clientService.addClient(client);
        cache.put(cl.getId(), cl);

        return new ResponseEntity<>(writeToResponse(cl));
    }

    @Override
    @RequestMapping(value = "/deleteClient/{id}",
            produces = { "application/json" },
            method = RequestMethod.DELETE)
    public ResponseEntity<Client> deleteClient(@PathVariable("id") String id, String xAuthUser) {
        String deleteMsg = clientService.deleteClient(id);
        if (cache.containsKey(id)) {
            cache.remove(id);
        }

        return new ResponseEntity<>(writeToResponse(deleteMsg));
    }

    @Override
    @RequestMapping(value = "/updateClient",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.PUT)
    public ResponseEntity<Client> updateClient(@Valid @RequestBody Client client, @RequestHeader(value="X-Auth-User", required=false) String xAuthUser) {
        if (client == null || StringUtils.isEmpty(client.getId())) {
            throw new InvalidRequestException("Client payload or ID must not be null");
        }

        Client cl = clientService.updateClient(client);
        cache.put(cl.getId(), cl);

        return new ResponseEntity<>(writeToResponse(cl));
    }
}
