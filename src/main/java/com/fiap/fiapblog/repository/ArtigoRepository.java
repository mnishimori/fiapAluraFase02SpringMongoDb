package com.fiap.fiapblog.repository;

import com.fiap.fiapblog.model.Artigo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtigoRepository extends MongoRepository<Artigo, String> {

  void deleteById(String id);

}
