package com.easy.eats.endereco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easy.eats.endereco.service.EnderecoService;

@Controller
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    EnderecoService service;

    
    
}
