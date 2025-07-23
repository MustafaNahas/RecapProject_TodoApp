package org.example.recapproject_todoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.recapproject_todoapp.dto.ToDo_DTO;
import org.example.recapproject_todoapp.model.ToDo;
import org.example.recapproject_todoapp.service.KanbanService;
import org.example.recapproject_todoapp.service.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KanbanController.class)
class KanbanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KanbanService kanbanService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFindAll_ReturnsListOfToDos() throws Exception {
        List<ToDo> toDos = List.of(new ToDo("1", "Test", Status.OPEN, "Beschreibung"));
        when(kanbanService.findAll()).thenReturn(toDos);

        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void testFindById_ReturnsToDo() throws Exception {
        ToDo toDo = new ToDo("1", "Titel", Status.OPEN, "Beschreibung");
        when(kanbanService.findById("1")).thenReturn(toDo);

        mockMvc.perform(get("/api/todo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Titel"));
    }

    @Test
    void testSave_CreatesNewToDo() throws Exception {
        ToDo_DTO dto = new ToDo_DTO("Titel", Status.OPEN, "Beschreibung");
        ToDo savedToDo = new ToDo("123", dto.title(), dto.status(), dto.description());

        when(kanbanService.save(any())).thenReturn(savedToDo);

        mockMvc.perform(post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.title").value("Titel"));
    }

    @Test
    void testUpdate_UpdatesAndReturnsToDo() throws Exception {
        ToDo updated = new ToDo("1", "Neu", Status.IN_PROGRESS, "Neue Beschreibung");

        when(kanbanService.update(eq("1"), any(ToDo.class))).thenReturn(updated);

        mockMvc.perform(put("/api/todo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Neu"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testDelete_DeletesToDo() throws Exception {
        when(kanbanService.delete("1")).thenReturn(true);

        mockMvc.perform(delete("/api/todo/1"))
                .andExpect(status().isOk());

        verify(kanbanService).delete("1");
    }
}