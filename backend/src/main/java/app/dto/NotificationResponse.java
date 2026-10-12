package app.dto;

import java.time.LocalDate;

public record NotificationResponse(String type, String title, String message, LocalDate date, boolean read) { }
