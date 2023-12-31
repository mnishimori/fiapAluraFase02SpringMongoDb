package com.fiap.fiapblog.service;

import com.fiap.fiapblog.model.Artigo;
import com.fiap.fiapblog.model.ArtigoAutorCount;
import com.fiap.fiapblog.model.ArtigoStatusCount;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtigoService {

  List<Artigo> obterTodos();

  Artigo obterPorCodigo(String codigo);

  Artigo criar(Artigo artigo);

  List<Artigo> findByDataGreaterThan(LocalDateTime data);

  List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status);

  void alterar(Artigo artigo);

  void alterarUrl(String codigo, String url);

  void deleteById(String id);

  void deletarArtigoById(String id);

  List<Artigo> findByStatusAndDataIsGreaterThan(Integer status, LocalDateTime data);

  List<Artigo> obterArtigosPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal);

  List<Artigo> encontrarArtigosPorDataStatusTitulo(LocalDateTime data, Integer status,
      String titulo);

  Page<Artigo> listarArtigos(Pageable pageable);

  List<Artigo> findByStatusOrderByTituloAsc(Integer status);

  List<Artigo> obterArtigosPorStatusComOrdenacao(Integer status);

  List<Artigo> findByTexto(String texto);

  List<ArtigoStatusCount> contarArtigosPorStatus();

  List<ArtigoAutorCount> contarArtigosPorAutorPorPeriodo(LocalDate dataInicial,
      LocalDate dataFinal);
}
