package app.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import app.dto.AppointmentRequest;
import app.dto.AppointmentResponse;
import app.model.Appointment;
import app.model.Pet;
import app.model.User;
import app.repository.AppointmentRepository;
import app.repository.PetRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final UserService userService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PetRepository petRepository,
            UserService userService) {
        this.appointmentRepository = appointmentRepository;
        this.petRepository = petRepository;
        this.userService = userService;
    }

    public AppointmentResponse createAppointment(String email, AppointmentRequest request) {
        User owner = userService.getUserByEmail(email);
        Pet pet = findOwnedPet(request.getPetId(), owner);

        Appointment appointment = new Appointment();
        appointment.setPet(pet);
        applyRequest(appointment, request);
        return toResponse(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> getAppointments(String email) {
        return getAppointments(email, null, null);
    }

    public List<AppointmentResponse> getAppointments(String email, String status, Long petId) {
        User owner = userService.getUserByEmail(email);
        if (petId != null) {
            findOwnedPet(petId, owner);
        }
        return appointmentRepository.findByPetOwner(owner).stream()
                .filter(appointment -> status == null || status.isBlank()
                        || status.equalsIgnoreCase(appointment.getStatus()))
                .filter(appointment -> petId == null || petId.equals(appointment.getPet().getId()))
                .map(this::toResponse)
                .toList();
    }

    public AppointmentResponse getAppointment(String email, Long id) {
        User owner = userService.getUserByEmail(email);
        return toResponse(findOwnedAppointment(id, owner));
    }

    public AppointmentResponse updateAppointment(String email, Long id, AppointmentRequest request) {
        User owner = userService.getUserByEmail(email);
        Appointment appointment = findOwnedAppointment(id, owner);
        Pet pet = findOwnedPet(request.getPetId(), owner);
        appointment.setPet(pet);
        applyRequest(appointment, request);
        return toResponse(appointmentRepository.save(appointment));
    }

    public void deleteAppointment(String email, Long id) {
        User owner = userService.getUserByEmail(email);
        appointmentRepository.delete(findOwnedAppointment(id, owner));
    }

    private Pet findOwnedPet(Long petId, User owner) {
        return petRepository.findByIdAndOwner(petId, owner)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
    }

    private Appointment findOwnedAppointment(Long id, User owner) {
        return appointmentRepository.findByIdAndPetOwner(id, owner)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));
    }

    private void applyRequest(Appointment appointment, AppointmentRequest request) {
        appointment.setDate(request.getDate());
        appointment.setTime(request.getTime());
        appointment.setVeterinarian(request.getVeterinarian());
        appointment.setClinic(request.getClinic());
        appointment.setReason(request.getReason());
        appointment.setStatus(request.getStatus() == null ? "UPCOMING" : request.getStatus());
        appointment.setNotes(request.getNotes());
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        Pet pet = appointment.getPet();
        return new AppointmentResponse(
                appointment.getId(),
                pet.getId(),
                pet.getName(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getVeterinarian(),
                appointment.getClinic(),
                appointment.getReason(),
                appointment.getStatus(),
                appointment.getNotes()
        );
    }
}
