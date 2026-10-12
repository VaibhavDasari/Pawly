package app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dto.PetStatsResponse;
import app.service.PetStatsService;

@RestController
@RequestMapping("/api/pets")
public class PetStatsController {

    private final PetStatsService petStatsService;

    public PetStatsController(PetStatsService petStatsService) {
        this.petStatsService = petStatsService;
    }

    @GetMapping("/{id}/stats")
    public PetStatsResponse getStats(Authentication authentication, @PathVariable Long id) {
        return petStatsService.getStats(authentication.getName(), id);
    }
}
