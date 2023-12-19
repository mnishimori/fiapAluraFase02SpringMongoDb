package com.fiap.fiapblog.controller;

import com.fiap.fiapblog.model.Artigo;
import com.fiap.fiapblog.service.ArtigoService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/artigos")
public class ArtigoController {

  private final ArtigoService artigoService;

  public ArtigoController(ArtigoService artigoService) {
    this.artigoService = artigoService;
  }

  @GetMapping
  public List<Artigo> obterTodos() {
    return artigoService.obterTodos();
  }

  @GetMapping("/{codigo}")
  public Artigo obterPorCodigo(@PathVariable String codigo) {
    return artigoService.obterPorCodigo(codigo);
  }

  @PostMapping
  public Artigo criar(@RequestBody Artigo artigo) {
    return artigoService.criar(artigo);
  }

}
