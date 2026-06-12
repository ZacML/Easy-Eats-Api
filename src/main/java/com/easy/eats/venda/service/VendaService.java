package com.easy.eats.venda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.venda.repository.VendaRepository;

@Service
public class VendaService {

    @Autowired
    VendaRepository repository;
}
