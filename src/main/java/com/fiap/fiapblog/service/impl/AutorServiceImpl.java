package com.fiap.fiapblog.service.impl;

import com.fiap.fiapblog.model.Autor;
import com.fiap.fiapblog.repository.AutorRepository;
import com.fiap.fiapblog.service.AutorService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AutorServiceImpl implements AutorService {

  private final AutorRepository autorRepository;

  public AutorServiceImpl(AutorRepository autorRepository) {
    this.autorRepository = autorRepository;
  }

  @Override
  public Autor criar(Autor autor) {
    return autorRepository.save(autor);
  }

  @Override
  public Autor obterPorCodigo(String codigo) {
    return autorRepository.findById(codigo)
        .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado!"));
  }

  @Override
  public List<Autor> obterTodos() {
    return autorRepository.findAll();
  }
}
