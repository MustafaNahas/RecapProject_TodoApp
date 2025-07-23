package org.example.recapproject_todoapp.dto;

import org.example.recapproject_todoapp.service.Status;

public record ToDo_DTO(String title, Status status, String description) {
}
