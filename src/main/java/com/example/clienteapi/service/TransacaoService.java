package com.example.clienteapi.service;

import com.example.clienteapi.model.ClienteEntity;
import com.example.clienteapi.model.TransacaoEntity;
import com.example.clienteapi.repository.ClienteRepository;
import com.example.clienteapi.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransacaoService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Transactional
    public String cadastrarTransacao(Long clienteId, String tipo, BigDecimal valor) {
        ClienteEntity cliente = clienteRepository.findById(clienteId).orElse(null);
        if (cliente == null) {
            return "Cliente não encontrado.";
        }

        TransacaoEntity transacao = new TransacaoEntity();
        transacao.setTipo(tipo);
        transacao.setValor(valor);
        transacao.setData(LocalDateTime.now());
        transacao.setCliente(cliente);

        transacaoRepository.save(transacao);
        return "Transação realizada com sucesso.";
    }

    public List<TransacaoEntity> buscarExtrato(Long clienteId) {
        List<TransacaoEntity> transacoes = transacaoRepository.findByClienteId(clienteId);
        return transacoes;
    }
}
