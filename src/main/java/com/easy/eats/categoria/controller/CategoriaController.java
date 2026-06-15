package com.easy.eats.categoria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easy.eats.categoria.service.CategoriaService;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    CategoriaService service;

    
    
}
