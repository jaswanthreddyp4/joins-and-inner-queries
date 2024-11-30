package org.example.dto;

public record CountOfEmployeesInEachTeam(
        String teamName,
        Long count
) {
}
