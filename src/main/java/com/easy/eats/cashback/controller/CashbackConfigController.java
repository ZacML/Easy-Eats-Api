package com.easy.eats.cashback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easy.eats.cashback.model.CashbackConfig;
import com.easy.eats.cashback.service.CashbackConfigService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cashback-config")
public class CashbackConfigController {

    @Autowired
    CashbackConfigService service;

    @GetMapping
    public ResponseEntity<CashbackConfig> buscar() {
        return ResponseEntity.ok(service.buscarOuCriar());
    }

    @PutMapping
    public ResponseEntity<CashbackConfig> atualizar(@Valid @RequestBody CashbackConfig config) {
        return ResponseEntity.ok(service.atualizar(config));
    }
}
