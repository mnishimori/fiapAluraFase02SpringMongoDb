package com.fiap.fiapblog.repository;

import com.fiap.fiapblog.model.Autor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AutorRepository extends MongoRepository<Autor, String> {

}
