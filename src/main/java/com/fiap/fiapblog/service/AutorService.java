package com.fiap.fiapblog.service;

import com.fiap.fiapblog.model.Autor;
import java.util.List;

public interface AutorService {

  Autor criar(Autor autor);
  Autor obterPorCodigo(String codigo);

  List<Autor> obterTodos();
}
