package org.example.recapproject_todoapp.model;

import lombok.With;
import org.example.recapproject_todoapp.service.Status;
@With
public record ToDo(String id, String title, Status status, String description) {
}
