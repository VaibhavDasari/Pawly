package app.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dto.NotificationResponse;
import app.service.NotificationService;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/api/notifications")
    public List<NotificationResponse> getNotifications(Authentication authentication) {
        return notificationService.getNotifications(authentication.getName());
    }
}
