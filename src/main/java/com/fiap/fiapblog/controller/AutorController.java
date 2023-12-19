package com.fiap.fiapblog.controller;

import com.fiap.fiapblog.model.Autor;
import com.fiap.fiapblog.service.AutorService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autores")
public class AutorController {

  private final AutorService autorService;

  public AutorController(AutorService autorService) {
    this.autorService = autorService;
  }

  @GetMapping
  public List<Autor> obterTodos() {
    return autorService.obterTodos();
  }

  @PostMapping
  public Autor criar(@RequestBody Autor autor) {
    return autorService.criar(autor);
  }

  @GetMapping("/{codigo}")
  public Autor obterPorCodigo(@PathVariable String codigo){
    return autorService.obterPorCodigo(codigo);
  }
}
