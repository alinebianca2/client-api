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
            @RequestParam String tipo,
            @RequestParam BigDecimal valor) {

        String resultado = transacaoService.cadastrarTransacao(clienteId, tipo, valor);

        if (resultado.equals("Transação realizada com sucesso.")) {
            return new ResponseEntity<>(resultado, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{clienteId}/extrato")
    public ResponseEntity<Map<String, Object>> buscarExtrato(@PathVariable Long clienteId) {
        List<TransacaoEntity> extrato = transacaoService.buscarExtrato(clienteId);

        if (extrato == null || extrato.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("extrato", extrato);
            response.put("saldoTotal", BigDecimal.ZERO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // Saldo total
        BigDecimal saldoTotal = BigDecimal.ZERO;

        for (TransacaoEntity transacao : extrato) {
            if ("credito".equalsIgnoreCase(transacao.getTipo())) {
                saldoTotal = saldoTotal.add(transacao.getValor());
            } else if ("debito".equalsIgnoreCase(transacao.getTipo())) {
                saldoTotal = saldoTotal.subtract(transacao.getValor());
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("extrato", extrato);
        response.put("saldoTotal", saldoTotal);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
