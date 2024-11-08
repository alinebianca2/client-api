package com.example.clienteapi.repository;

import com.example.clienteapi.model.TransacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<TransacaoEntity, Long> {

    List<TransacaoEntity> findByClienteId(Long clienteId);
}