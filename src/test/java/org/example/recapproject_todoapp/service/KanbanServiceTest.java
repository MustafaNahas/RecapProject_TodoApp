package org.example.recapproject_todoapp.service;

import org.example.recapproject_todoapp.dto.ToDo_DTO;
import org.example.recapproject_todoapp.model.ToDo;
import org.example.recapproject_todoapp.repository.ToDoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KanbanServiceTest {

    @Mock
    private ToDoRepository toDoRepository;

    @Mock
    private IdService idService;

    @InjectMocks
    private KanbanService kanbanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll_ReturnsListOfToDos() {
        List<ToDo> expected = List.of(new ToDo("1", "Test", Status.OPEN, "Testbeschreibung"));
        when(toDoRepository.findAll()).thenReturn(expected);

        List<ToDo> result = kanbanService.findAll();

        assertEquals(expected, result);
    }

    @Test
    void testSave_CreatesNewToDo() {
        ToDo_DTO dto = new ToDo_DTO("Titel", Status.OPEN, "Beschreibung");
        ToDo saved = new ToDo("123", "Titel", Status.OPEN, "Beschreibung");

        when(idService.generateId()).thenReturn("123");
        when(toDoRepository.save(any(ToDo.class))).thenReturn(saved);

        ToDo result = kanbanService.save(dto);

        assertEquals("123", result.id());
        assertEquals("Titel", result.title());
        assertEquals(Status.OPEN, result.status());
        assertEquals("Beschreibung", result.description());
    }

    @Test
    void testDelete_WithValidId_DeletesAndReturnsTrue() {
        String id = "123";

        boolean result = kanbanService.delete(id);

        verify(toDoRepository).deleteById(id);
        assertTrue(result);
    }

    @Test
    void testDelete_WithNullId_ReturnsFalse() {
        boolean result = kanbanService.delete(null);

        verify(toDoRepository, never()).deleteById(any());
        assertFalse(result);
    }

    @Test
    void testFindById_Found_ReturnsToDo() {
        ToDo toDo = new ToDo("1", "Titel", Status.OPEN, "Beschreibung");
        when(toDoRepository.findById("1")).thenReturn(Optional.of(toDo));

        ToDo result = kanbanService.findById("1");

        assertEquals(toDo, result);
    }

    @Test
    void testFindById_NotFound_ReturnsNull() {
        when(toDoRepository.findById("1")).thenReturn(Optional.empty());

        ToDo result = kanbanService.findById("1");

        assertNull(result);
    }

    @Test
    void testUpdate_WithExistingToDo_UpdatesAndReturnsOldToDo() {
        ToDo existing = new ToDo("1", "Alt", Status.OPEN, "Alt");
        ToDo updated = new ToDo("1", "Neu", Status.IN_PROGRESS, "Neu");

        when(toDoRepository.findById("1")).thenReturn(Optional.of(existing));
        when(toDoRepository.save(any())).thenReturn(updated);

        ToDo result = kanbanService.update("1", updated);

        assertNotNull(result);
        verify(toDoRepository).save(any(ToDo.class));
        assertEquals("Alt", result.title()); // Rückgabe ist das alte ToDo-Objekt
    }

    @Test
    void testUpdate_WithNonExistingToDo_ReturnsNull() {
        ToDo updated = new ToDo("1", "Neu", Status.IN_PROGRESS, "Neu");
        when(toDoRepository.findById("1")).thenReturn(Optional.empty());

        ToDo result = kanbanService.update("1", updated);

        assertNull(result);
        verify(toDoRepository, never()).save(any());
    }
}