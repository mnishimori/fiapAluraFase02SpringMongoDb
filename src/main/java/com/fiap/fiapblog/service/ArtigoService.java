package com.fiap.fiapblog.service;

import com.fiap.fiapblog.model.Artigo;
import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoService {

  List<Artigo> obterTodos();
  Artigo obterPorCodigo(String codigo);
  Artigo criar(Artigo artigo);
  List<Artigo> findByDataGreaterThan(LocalDateTime data);

}
