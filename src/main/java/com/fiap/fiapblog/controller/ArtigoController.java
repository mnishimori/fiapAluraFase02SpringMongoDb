package com.fiap.fiapblog.controller;

import com.fiap.fiapblog.model.Artigo;
import com.fiap.fiapblog.service.ArtigoService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping("/maiordata")
  public List<Artigo> findByDataGreaterThan(@RequestParam LocalDateTime data) {
    return artigoService.findByDataGreaterThan(data);
  }

  @GetMapping("/data-status")
  public List<Artigo> findByDataAndStatus(@RequestParam LocalDateTime data,
      @RequestParam Integer status) {
    return artigoService.findByDataAndStatus(data, status);
  }

  @PutMapping
  public void alterar(@RequestBody Artigo artigo) {
    artigoService.alterar(artigo);
  }

  @PutMapping("/altera-parcialmente/{codigo}")
  public void alterarParcialmente(@PathVariable String codigo, @RequestBody Artigo artigo) {
    artigoService.alterarParcialmente(codigo, artigo.getUrl());
  }

  @DeleteMapping("/{codigo}")
  public void deletar(@PathVariable String codigo){
    artigoService.deleteById(codigo);
  }

  @DeleteMapping("/delete-por-id/{codigo}")
  public void deletarPorId(@PathVariable String codigo){
    artigoService.deletarArtigoById(codigo);
  }
}