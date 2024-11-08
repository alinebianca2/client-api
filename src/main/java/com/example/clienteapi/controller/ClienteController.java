package com.example.clienteapi.controller;

import com.example.clienteapi.model.ClienteEntity;
import com.example.clienteapi.model.TransacaoEntity;
import com.example.clienteapi.service.ClienteService;
import com.example.clienteapi.service.TransacaoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")

public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private TransacaoService transacaoService;

    // Criar um cliente
    @PostMapping
    public ResponseEntity<ClienteEntity> createCliente(@RequestBody ClienteEntity cliente) {
        ClienteEntity novoCliente = clienteService.saveCliente(cliente);
        return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
    }

    @GetMapping
    public List<ClienteEntity> getAllClientes() {
        return clienteService.getAllClientes();
    }

    // Atualizar um cliente
    @PutMapping("/{id}")
    public ResponseEntity<ClienteEntity> updateCliente(@PathVariable Long id,
            @RequestBody ClienteEntity updatedCliente) {
        try {
            ClienteEntity clienteAtualizado = clienteService.updateCliente(id, updatedCliente);
            return new ResponseEntity<>(clienteAtualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 caso o cliente não seja encontrado
        }
    }

    // Buscar cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<ClienteEntity> getClienteById(@PathVariable Long id) {
        Optional<ClienteEntity> cliente = clienteService.getClienteById(id);
        if (cliente.isPresent()) {
            return new ResponseEntity<>(cliente.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 se não encontrar o cliente
        }
    }

    // Deletar cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        try {
            clienteService.deleteCliente(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 caso a exclusão seja bem-sucedida
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 se o cliente não for encontrado
        }
    }

    @GetMapping("/{clienteId}/extrato")
    public List<TransacaoEntity> getExtratoCliente(@PathVariable Long clienteId) {
        return transacaoService.buscarExtrato(clienteId);
    }
}
