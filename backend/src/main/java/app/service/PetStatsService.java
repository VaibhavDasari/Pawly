package app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import app.dto.PetStatsResponse;
import app.model.Appointment;
import app.model.HealthRecord;
import app.model.Pet;
import app.model.User;
import app.repository.AppointmentRepository;
import app.repository.HealthRecordRepository;
import app.repository.PetRepository;

@Service
public class PetStatsService {

    private final UserService userService;
    private final PetRepository petRepository;
    private final AppointmentRepository appointmentRepository;
    private final HealthRecordRepository healthRecordRepository;

    public PetStatsService(
            UserService userService,
            PetRepository petRepository,
            AppointmentRepository appointmentRepository,
            HealthRecordRepository healthRecordRepository) {
        this.userService = userService;
        this.petRepository = petRepository;
        this.appointmentRepository = appointmentRepository;
        this.healthRecordRepository = healthRecordRepository;
    }

    public PetStatsResponse getStats(String email, Long petId) {
        User owner = userService.getUserByEmail(email);
        Pet pet = petRepository.findByIdAndOwner(petId, owner)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
        List<Appointment> appointments = appointmentRepository.findByPetOwner(owner).stream()
                .filter(appointment -> appointment.getPet().getId().equals(petId))
                .toList();
        List<HealthRecord> records = healthRecordRepository.findByPetOwner(owner).stream()
                .filter(record -> record.getPet().getId().equals(petId))
                .toList();
        LocalDateTime now = LocalDateTime.now();
        long upcoming = appointments.stream().filter(appointment ->
                "UPCOMING".equalsIgnoreCase(appointment.getStatus())
                        && appointment.getDate() != null && appointment.getTime() != null
                        && !LocalDateTime.of(appointment.getDate(), appointment.getTime()).isBefore(now)).count();
        long completed = appointments.stream()
                .filter(appointment -> "COMPLETED".equalsIgnoreCase(appointment.getStatus())).count();
        long vaccinations = records.stream()
                .filter(record -> "VACCINATION".equalsIgnoreCase(record.getRecordType())).count();

        return new PetStatsResponse(pet.getId(), pet.getName(), appointments.size(), upcoming, completed,
                records.size(), vaccinations);
    }
}
