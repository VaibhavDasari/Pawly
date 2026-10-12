package app.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import app.dto.NotificationResponse;
import app.model.Appointment;
import app.model.Pet;
import app.model.User;
import app.repository.AppointmentRepository;

@Service
public class NotificationService {

    private final UserService userService;
    private final AppointmentRepository appointmentRepository;

    public NotificationService(UserService userService, AppointmentRepository appointmentRepository) {
        this.userService = userService;
        this.appointmentRepository = appointmentRepository;
    }

    public List<NotificationResponse> getNotifications(String email) {
        User user = userService.getUserByEmail(email);
        LocalDateTime now = LocalDateTime.now();
        return appointmentRepository.findByPetOwner(user).stream()
                .filter(appointment -> "UPCOMING".equalsIgnoreCase(appointment.getStatus()))
                .filter(appointment -> appointment.getDate() != null && appointment.getTime() != null)
                .filter(appointment -> !LocalDateTime.of(appointment.getDate(), appointment.getTime()).isBefore(now))
                .sorted(Comparator.comparing(Appointment::getDate).thenComparing(Appointment::getTime))
                .map(this::toNotification)
                .toList();
    }

    private NotificationResponse toNotification(Appointment appointment) {
        Pet pet = appointment.getPet();
        String message = pet.getName() + " has an appointment with " + appointment.getVeterinarian();
        return new NotificationResponse("APPOINTMENT", "Upcoming appointment", message, appointment.getDate(), false);
    }
}
