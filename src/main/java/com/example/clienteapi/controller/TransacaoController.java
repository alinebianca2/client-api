package com.example.clienteapi.controller;

import com.example.clienteapi.model.TransacaoEntity;
import com.example.clienteapi.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    // Cadastrar uma transação (crédito ou débito)
    @PostMapping("/{clienteId}")
    public ResponseEntity<String> cadastrarTransacao(
            @PathVariable Long clienteId,
            @RequestParam String tipo, // tipo da transação: "credito" ou "debito"
            @RequestParam BigDecimal valor) {

        String resultado = transacaoService.cadastrarTransacao(clienteId, tipo, valor);

        if (resultado.equals("Transação realizada com sucesso.")) {
            return new ResponseEntity<>(resultado, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
        }
    }

    // Buscar o extrato do cliente (todas as transações) e calcular saldo total
    @GetMapping("/{clienteId}/extrato")
    public ResponseEntity<Map<String, Object>> buscarExtrato(@PathVariable Long clienteId) {
        List<TransacaoEntity> extrato = transacaoService.buscarExtrato(clienteId);

        if (extrato == null || extrato.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404, cliente não encontrado
        }

        // Calcular o saldo total
        BigDecimal saldoTotal = BigDecimal.ZERO;

        for (TransacaoEntity transacao : extrato) {
            if ("credito".equalsIgnoreCase(transacao.getTipo())) {
                saldoTotal = saldoTotal.add(transacao.getValor()); // Adiciona o crédito
            } else if ("debito".equalsIgnoreCase(transacao.getTipo())) {
                saldoTotal = saldoTotal.subtract(transacao.getValor()); // Subtrai o débito
            }
        }

        // Retornar transações e saldo total em um mapa
        Map<String, Object> response = new HashMap<>();
        response.put("extrato", extrato);
        response.put("saldoTotal", saldoTotal);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
