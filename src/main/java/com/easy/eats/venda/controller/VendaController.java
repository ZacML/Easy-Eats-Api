package com.easy.eats.venda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    VendaService service;

    
}
