package com.fiap.fiapblog.service.impl;

import com.fiap.fiapblog.model.Artigo;
import com.fiap.fiapblog.repository.ArtigoRepository;
import com.fiap.fiapblog.service.ArtigoService;
import com.fiap.fiapblog.service.AutorService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ArtigoServiceImpl implements ArtigoService {

  private final ArtigoRepository artigoRepository;
  private final AutorService autorService;
  private final MongoTemplate mongoTemplate;

  public ArtigoServiceImpl(ArtigoRepository artigoRepository, AutorService autorService,
      MongoTemplate mongoTemplate) {
    this.artigoRepository = artigoRepository;
    this.autorService = autorService;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public List<Artigo> obterTodos() {
    return artigoRepository.findAll();
  }

  @Override
  public Artigo obterPorCodigo(String codigo) {
    return artigoRepository.findById(codigo)
        .orElseThrow(() -> new IllegalArgumentException("Artigo não encontrado"));
  }

  @Override
  public Artigo criar(Artigo artigo) {
    atribuirAutorSeInformadoEexistente(artigo);
    return artigoRepository.save(artigo);
  }

  private void atribuirAutorSeInformadoEexistente(Artigo artigo) {
    if (autorFoiInformadoNo(artigo)) {
      var autor = autorService.obterPorCodigo(artigo.getAutor().getCodigo());
      artigo.setAutor(autor);
    }
  }

  private boolean autorFoiInformadoNo(Artigo artigo) {
    return artigo.getAutor() != null && artigo.getAutor().getCodigo() != null;
  }

  @Override
  public List<Artigo> findByDataGreaterThan(LocalDateTime data) {
    var query = new Query(Criteria.where("data").gt(data));
    return mongoTemplate.find(query, Artigo.class);
  }

  @Override
  public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status) {
    var query = new Query(Criteria.where("data").is(data).and("status").is(status));
    return mongoTemplate.find(query, Artigo.class);
  }

  @Override
  public void alterar(Artigo artigo) {
    atribuirAutorSeInformadoEexistente(artigo);
    artigoRepository.save(artigo);
  }

  @Override
  public void alterarParcialmente(String codigo, String url) {
    var query = new Query(Criteria.where("_id").is(codigo));
    var update = new Update().set("url", url);
    mongoTemplate.updateFirst(query, update, Artigo.class);
  }

  @Override
  public void deleteById(String id) {
    artigoRepository.deleteById(id);
  }

  @Override
  public void deletarArtigoById(String id) {
    var query = new Query(Criteria.where("_id").is(id));
    mongoTemplate.remove(query, Artigo.class);
  }

  @Override
  public List<Artigo> findByStatusAndDataIsGreaterThan(Integer status, LocalDateTime data) {
    return artigoRepository.findByStatusAndDataIsGreaterThan(status, data);
  }

  @Override
  public List<Artigo> obterArtigosPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
    return artigoRepository.obterArtigosPorPeriodo(dataInicial, dataFinal);
  }
}
