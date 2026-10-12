package app.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.dto.PetRequest;
import app.dto.PetResponse;
import app.service.PetService;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<PetResponse> createPet(
            Authentication authentication,
            @Valid @RequestBody PetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(petService.createPet(authentication.getName(), request));
    }

    @GetMapping
    public List<PetResponse> getPets(
            Authentication authentication,
            @RequestParam(required = false) String search) {
        return petService.getPets(authentication.getName(), search);
    }

    @GetMapping("/{id}")
    public PetResponse getPet(Authentication authentication, @PathVariable Long id) {
        return petService.getPet(authentication.getName(), id);
    }

    @PutMapping("/{id}")
    public PetResponse updatePet(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody PetRequest request) {
        return petService.updatePet(authentication.getName(), id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(Authentication authentication, @PathVariable Long id) {
        petService.deletePet(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
