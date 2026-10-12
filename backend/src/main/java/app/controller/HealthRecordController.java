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

import app.dto.HealthRecordRequest;
import app.dto.HealthRecordResponse;
import app.service.HealthRecordService;

@RestController
@RequestMapping("/api/health-records")
public class HealthRecordController {

    private final HealthRecordService healthRecordService;

    public HealthRecordController(HealthRecordService healthRecordService) {
        this.healthRecordService = healthRecordService;
    }

    @PostMapping
    public ResponseEntity<HealthRecordResponse> createHealthRecord(
            Authentication authentication,
            @Valid @RequestBody HealthRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(healthRecordService.createHealthRecord(authentication.getName(), request));
    }

    @GetMapping
    public List<HealthRecordResponse> getHealthRecords(
            Authentication authentication,
            @RequestParam(required = false) Long petId,
            @RequestParam(required = false) String recordType,
            @RequestParam(required = false) String status) {
        return healthRecordService.getHealthRecords(authentication.getName(), petId, recordType, status);
    }

    @GetMapping("/{id}")
    public HealthRecordResponse getHealthRecord(Authentication authentication, @PathVariable Long id) {
        return healthRecordService.getHealthRecord(authentication.getName(), id);
    }

    @PutMapping("/{id}")
    public HealthRecordResponse updateHealthRecord(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody HealthRecordRequest request) {
        return healthRecordService.updateHealthRecord(authentication.getName(), id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHealthRecord(Authentication authentication, @PathVariable Long id) {
        healthRecordService.deleteHealthRecord(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
