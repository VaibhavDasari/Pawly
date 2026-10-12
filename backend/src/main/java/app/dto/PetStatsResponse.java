package app.dto;

public record PetStatsResponse(
        Long petId,
        String petName,
        long totalAppointments,
        long upcomingAppointments,
        long completedAppointments,
        long healthRecords,
        long vaccinations) { }
