package com.example.clienteapi.service;

import com.example.clienteapi.model.ClienteEntity;
import com.example.clienteapi.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Salvar cliente
    public ClienteEntity saveCliente(ClienteEntity cliente) {
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email do cliente é obrigatório.");
        }
        if (cliente.getNumeroConta() == null || cliente.getNumeroConta().isEmpty()) {
            throw new IllegalArgumentException("Número da conta do cliente é obrigatório.");
        }
        return clienteRepository.save(cliente);
    }

    // Lista todos os clientes
    public List<ClienteEntity> getAllClientes() {
        return clienteRepository.findAll();
    }

    // Lista cliente pelo ID
    public Optional<ClienteEntity> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    // Checar se cliente existe pelo ID
    public boolean existeCliente(Long clienteId) {
        return clienteRepository.existsById(clienteId);
    }

    // Deletar cliente pelo ID
    public void deleteCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        clienteRepository.deleteById(id);
    }

    // Atualiza cliente
    public ClienteEntity updateCliente(Long id, ClienteEntity updatedCliente) {
        Optional<ClienteEntity> existingClienteOpt = clienteRepository.findById(id);

        if (existingClienteOpt.isPresent()) {
            ClienteEntity existingCliente = existingClienteOpt.get();

            // Atualizando os campos do cliente conforme o enviado na requisição
            if (updatedCliente.getNome() != null && !updatedCliente.getNome().isEmpty()) {
                existingCliente.setNome(updatedCliente.getNome());
            }

            if (updatedCliente.getIdade() != null) {
                existingCliente.setIdade(updatedCliente.getIdade());
            }

            if (updatedCliente.getEmail() != null && !updatedCliente.getEmail().isEmpty()) {
                existingCliente.setEmail(updatedCliente.getEmail());
            }

            if (updatedCliente.getNumeroConta() != null && !updatedCliente.getNumeroConta().isEmpty()) {
                existingCliente.setNumeroConta(updatedCliente.getNumeroConta());
            }

            // Salvando as alterações
            return clienteRepository.save(existingCliente);
        } else {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
    }
}
