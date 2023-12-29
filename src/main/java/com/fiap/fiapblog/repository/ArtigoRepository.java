package com.fiap.fiapblog.repository;

import com.fiap.fiapblog.model.Artigo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ArtigoRepository extends MongoRepository<Artigo, String> {

  void deleteById(String id);

  List<Artigo> findByStatusAndDataIsGreaterThan(Integer status, LocalDateTime data);

  @Query("{$and: [{'data': {$gte: ?0}}, {'data': {$lte: ?1}}]}")
  List<Artigo> obterArtigosPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal);

  Page<Artigo> findAll(Pageable pageable);

  List<Artigo> findByStatusOrderByTituloAsc(Integer status);

  @Query(value = "{'status': {$eq: ?0}}", sort = "{'titulo': 1}")
  List<Artigo> obterArtigosPorStatusComOrdenacao(Integer status);

}
