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

import app.dto.AppointmentRequest;
import app.dto.AppointmentResponse;
import app.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            Authentication authentication,
            @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentService.createAppointment(authentication.getName(), request));
    }

    @GetMapping
    public List<AppointmentResponse> getAppointments(
            Authentication authentication,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long petId) {
        return appointmentService.getAppointments(authentication.getName(), status, petId);
    }

    @GetMapping("/{id}")
    public AppointmentResponse getAppointment(Authentication authentication, @PathVariable Long id) {
        return appointmentService.getAppointment(authentication.getName(), id);
    }

    @PutMapping("/{id}")
    public AppointmentResponse updateAppointment(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequest request) {
        return appointmentService.updateAppointment(authentication.getName(), id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(Authentication authentication, @PathVariable Long id) {
        appointmentService.deleteAppointment(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
