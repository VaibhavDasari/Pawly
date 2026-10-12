package app.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import app.dto.DashboardResponse;
import app.model.Appointment;
import app.model.HealthRecord;
import app.model.Pet;
import app.model.User;
import app.repository.AppointmentRepository;
import app.repository.HealthRecordRepository;
import app.repository.PetRepository;

@Service
public class DashboardService {

    private final UserService userService;
    private final PetRepository petRepository;
    private final AppointmentRepository appointmentRepository;
    private final HealthRecordRepository healthRecordRepository;

    public DashboardService(
            UserService userService,
            PetRepository petRepository,
            AppointmentRepository appointmentRepository,
            HealthRecordRepository healthRecordRepository) {
        this.userService = userService;
        this.petRepository = petRepository;
        this.appointmentRepository = appointmentRepository;
        this.healthRecordRepository = healthRecordRepository;
    }

    public DashboardResponse getDashboard(String email) {
        User user = userService.getUserByEmail(email);
        List<Pet> pets = petRepository.findByOwner(user);
        List<Appointment> upcoming = appointmentRepository.findByPetOwner(user).stream()
                .filter(this::isUpcoming)
                .sorted(Comparator.comparing(Appointment::getDate)
                        .thenComparing(Appointment::getTime))
                .toList();
        List<HealthRecord> records = healthRecordRepository.findByPetOwner(user).stream()
                .sorted(Comparator.comparing(
                        HealthRecord::getDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        return new DashboardResponse(
                new DashboardResponse.UserSummary(user.getId(), user.getName()),
                pets.size(),
                upcoming.size(),
                records.size(),
                pets.stream().map(pet -> new DashboardResponse.PetSummary(
                        pet.getId(), pet.getName(), pet.getSpecies(), pet.getBreed(), pet.getAge(), pet.getGender())).toList(),
                upcoming.stream().map(this::toAppointmentSummary).toList(),
                records.stream().map(this::toHealthRecordSummary).toList());
    }

    private boolean isUpcoming(Appointment appointment) {
        return "UPCOMING".equalsIgnoreCase(appointment.getStatus())
                && appointment.getDate() != null
                && appointment.getTime() != null
                && !LocalDateTime.of(appointment.getDate(), appointment.getTime()).isBefore(LocalDateTime.now());
    }

    private DashboardResponse.AppointmentSummary toAppointmentSummary(Appointment appointment) {
        Pet pet = appointment.getPet();
        return new DashboardResponse.AppointmentSummary(
                appointment.getId(), pet.getId(), pet.getName(), appointment.getDate(), appointment.getTime(),
                appointment.getVeterinarian(), appointment.getClinic(), appointment.getReason(), appointment.getStatus());
    }

    private DashboardResponse.HealthRecordSummary toHealthRecordSummary(HealthRecord record) {
        Pet pet = record.getPet();
        return new DashboardResponse.HealthRecordSummary(
                record.getId(), pet.getId(), pet.getName(), record.getRecordType(), record.getTitle(), record.getDate(), record.getStatus());
    }
}
