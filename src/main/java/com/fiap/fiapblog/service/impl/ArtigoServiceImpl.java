package com.fiap.fiapblog.service.impl;

import com.fiap.fiapblog.model.Artigo;
import com.fiap.fiapblog.model.ArtigoAutorCount;
import com.fiap.fiapblog.model.ArtigoStatusCount;
import com.fiap.fiapblog.repository.ArtigoRepository;
import com.fiap.fiapblog.service.ArtigoService;
import com.fiap.fiapblog.service.AutorService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
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
    return realizarPesquisa(Criteria.where("data").gt(data));
  }

  @Override
  public List<Artigo> findByDataAndStatus(LocalDateTime data, Integer status) {
    return realizarPesquisa(Criteria.where("data").is(data).and("status").is(status));
  }

  @Transactional
  @Override
  public void alterar(Artigo artigo) {
    atribuirAutorSeInformadoEexistente(artigo);
    try {
      artigoRepository.save(artigo);
    } catch (OptimisticLockingFailureException e) {
      atualizarArtigoExistenteEmConflito(artigo);
    }
  }

  private void atualizarArtigoExistenteEmConflito(Artigo artigo) {
    var artigoFound = findByIdRequired(artigo.getCodigo());
    artigoFound.setTitulo(artigo.getTitulo());
    artigoFound.setData(artigo.getData());
    artigoFound.setTexto(artigo.getTexto());
    artigoFound.setUrl(artigo.getUrl());
    artigoFound.setAutor(artigo.getAutor());
    artigoFound.setStatus(artigo.getStatus());
    var version = artigoFound.getVersion();
    artigoFound.setVersion(version++);
    artigoRepository.save(artigoFound);
  }

  @Transactional
  @Override
  public void alterarUrl(String codigo, String url) {
    var query = new Query(Criteria.where("_id").is(codigo));
    var update = new Update().set("url", url);
    mongoTemplate.updateFirst(query, update, Artigo.class);
  }

  @Transactional
  @Override
  public void deleteById(String id) {
    artigoRepository.deleteById(id);
  }

  @Transactional
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

  @Override
  public List<Artigo> encontrarArtigosPorDataStatusTitulo(LocalDateTime data, Integer status,
      String titulo) {
    var criteria = gerarCriteriosDePesquisa(data, status, titulo);
    return realizarPesquisa(criteria);
  }

  @Override
  public Page<Artigo> listarArtigos(Pageable pageable) {
    var sort = Sort.by("titulo").ascending();
    var newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    return artigoRepository.findAll(newPageable);
  }

  @Override
  public List<Artigo> findByStatusOrderByTituloAsc(Integer status) {
    return artigoRepository.findByStatusOrderByTituloAsc(status);
  }

  @Override
  public List<Artigo> obterArtigosPorStatusComOrdenacao(Integer status) {
    return artigoRepository.obterArtigosPorStatusComOrdenacao(status);
  }

  @Override
  public List<Artigo> findByTexto(String texto) {
    var criteria = TextCriteria.forDefaultLanguage().matchingPhrase(texto);
    var query = TextQuery.queryText(criteria).sortByScore();
    return mongoTemplate.find(query, Artigo.class);
  }

  @Override
  public List<ArtigoStatusCount> contarArtigosPorStatus() {
    var aggregation = Aggregation.newAggregation(Artigo.class,
        Aggregation.group("status").count().as("quantidade"),
        Aggregation.project("quantidade").and("status").previousOperation());
    var result = mongoTemplate.aggregate(aggregation, ArtigoStatusCount.class);
    return result.getMappedResults();
  }

  @Override
  public List<ArtigoAutorCount> contarArtigosPorAutorPorPeriodo(LocalDate dataInicial,
      LocalDate dataFinal) {
    var criteria = new Criteria().and("data").gte(dataInicial.atStartOfDay())
        .lt(dataFinal.plusDays(1).atStartOfDay());
    var aggregation = Aggregation.newAggregation(Artigo.class,
        Aggregation.match(criteria),
        Aggregation.group("autor").count().as("quantidade"),
        Aggregation.project("quantidade").and("autor").previousOperation());
    var result = mongoTemplate.aggregate(aggregation, ArtigoAutorCount.class);
    return result.getMappedResults();
  }

  private List<Artigo> realizarPesquisa(Criteria criteria) {
    var query = new Query(criteria);
    return mongoTemplate.find(query, Artigo.class);
  }

  private Criteria gerarCriteriosDePesquisa(LocalDateTime data, Integer status, String titulo) {
    var criteria = new Criteria();
    criteria.and("data").gte(data);
    if (status != null) {
      criteria.and("status").is(status);
    }
    if (titulo != null && !titulo.isEmpty()) {
      criteria.and("titulo").regex(titulo, "i");
    }
    return criteria;
  }

  public Artigo findByIdRequired(String id) {
    return artigoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Artigo não encontrado! Id: " + id));
  }
}
