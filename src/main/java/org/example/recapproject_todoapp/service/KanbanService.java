package org.example.recapproject_todoapp.service;

import org.example.recapproject_todoapp.dto.ToDo_DTO;
import org.example.recapproject_todoapp.model.ToDo;

import org.example.recapproject_todoapp.repository.ToDoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KanbanService {
    private final ToDoRepository toDoRepository;
     private final IdService idService;

     public KanbanService(ToDoRepository toDoRepository, IdService idService) {
         this.toDoRepository = toDoRepository;
         this.idService = idService;

     }
     public List<ToDo> findAll() {
         return toDoRepository.findAll();
     }
     public ToDo save(ToDo_DTO toDo) {
         ToDo newToDo=new ToDo(idService.generateId(), toDo.title(),toDo.status(),toDo.description());
         return toDoRepository.save(newToDo);

     }

     public boolean delete(String id) {
         if (id != null ) {
             toDoRepository.deleteById(id);
         }
         else {
             return false;
         }
        return true;

     }

     public ToDo findById(String id) {
         return toDoRepository.findById(id).orElse(null);

     }
     public ToDo update(String id, ToDo newtoDo)
     {
         ToDo oldToDo=findById(id);
         if(oldToDo!=null)
         {
             toDoRepository.save(oldToDo.withStatus(newtoDo.status()).withDescription(newtoDo.description()).withTitle(newtoDo.title()));
         }
         return oldToDo;
     }
}
