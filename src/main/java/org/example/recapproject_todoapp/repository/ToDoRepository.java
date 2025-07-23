package org.example.recapproject_todoapp.repository;

import org.example.recapproject_todoapp.model.ToDo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends MongoRepository<ToDo,String> {
}
