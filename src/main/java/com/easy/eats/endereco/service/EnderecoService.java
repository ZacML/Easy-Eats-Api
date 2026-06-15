package com.easy.eats.endereco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.endereco.repository.EnderecoRepository;

@Service
public class EnderecoService {

    @Autowired
    EnderecoRepository repository;
}
