package org.example.recapproject_todoapp.controller;

import org.example.recapproject_todoapp.dto.ToDo_DTO;
import org.example.recapproject_todoapp.model.ToDo;
import org.example.recapproject_todoapp.service.KanbanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/todo")
public class KanbanController {

    private KanbanService kanbanService;
    public KanbanController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;

    }
    @GetMapping
    public List<ToDo> findAll() {
       return kanbanService.findAll();
    }
    @GetMapping("/{id}")
    public ToDo findById( @PathVariable String id) {
        return kanbanService.findById(id);
    }
    @PostMapping
    public ResponseEntity<ToDo> save(@RequestBody ToDo_DTO toDo) {
        System.out.println(toDo.toString());
        ToDo savedToDo = kanbanService.save(toDo);

        // Baue die URI der neuen Ressource: /api/todo/{id}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedToDo.id())
                .toUri();

        return ResponseEntity
                .created(location) // HTTP 201 Created + Location-Header
                .body(savedToDo);
        //return kanbanService.save(toDo);
    }

    @PutMapping("/{id}")
    public ToDo update(@PathVariable String id, @RequestBody ToDo toDo )
    {
        return kanbanService.update(id, toDo);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id)
    {
        kanbanService.delete(id);
    }

}
