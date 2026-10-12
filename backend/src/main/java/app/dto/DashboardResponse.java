package app.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record DashboardResponse(
        UserSummary user,
        long totalPets,
        long upcomingAppointments,
        long recentHealthRecords,
        List<PetSummary> pets,
        List<AppointmentSummary> appointments,
        List<HealthRecordSummary> healthRecords) {

    public record UserSummary(Long id, String name) { }

    public record PetSummary(Long id, String name, String species, String breed, Integer age, String gender) { }

    public record AppointmentSummary(
            Long id,
            Long petId,
            String petName,
            LocalDate date,
            LocalTime time,
            String veterinarian,
            String clinic,
            String reason,
            String status) { }

    public record HealthRecordSummary(
            Long id,
            Long petId,
            String petName,
            String recordType,
            String title,
            LocalDate date,
            String status) { }
}
