package com.easy.eats.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Tratamento centralizado de exceções da API.
 *
 * Hoje os services lançam RuntimeException genérica com mensagens do tipo
 * "X não encontrado(a)" quando um registro não existe. Em vez de deixar isso
 * estourar como erro 500 (o padrão do Spring para exceções não tratadas),
 * aqui a mensagem é inspecionada e convertida para 404 quando fizer sentido.
 *
 * O ideal a médio prazo é substituir RuntimeException por uma exceção
 * dedicada (ex.: RecursoNaoEncontradoException) nos services, eliminando a
 * necessidade de inspecionar a mensagem de texto.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> tratarRuntimeException(RuntimeException ex) {

        String mensagem = ex.getMessage() != null ? ex.getMessage() : "";
        String mensagemLower = mensagem.toLowerCase();

        boolean naoEncontrado = mensagemLower.contains("não encontrad") || mensagemLower.contains("nao encontrad");
        boolean credenciaisInvalidas = mensagemLower.contains("e-mail ou senha") || mensagemLower.contains("email ou senha");

        HttpStatus status = naoEncontrado
                ? HttpStatus.NOT_FOUND
                : credenciaisInvalidas
                        ? HttpStatus.UNAUTHORIZED
                        : HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status).body(corpoErro(status, mensagem));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> tratarIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(corpoErro(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(MethodArgumentNotValidException ex) {

        Map<String, String> erroPorCampo = new LinkedHashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            erroPorCampo.put(erro.getField(), erro.getDefaultMessage());
        }

        Map<String, Object> corpo = corpoErro(HttpStatus.BAD_REQUEST, "Dados inválidos");
        corpo.put("campos", erroPorCampo);

        return ResponseEntity.badRequest().body(corpo);
    }

    private Map<String, Object> corpoErro(HttpStatus status, String mensagem) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("timestamp", LocalDateTime.now());
        corpo.put("status", status.value());
        corpo.put("erro", status.getReasonPhrase());
        corpo.put("mensagem", mensagem);
        return corpo;
    }
}
