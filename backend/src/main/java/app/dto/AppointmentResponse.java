package app.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentResponse {

    private final Long id;
    private final Long petId;
    private final String petName;
    private final LocalDate date;
    private final LocalTime time;
    private final String veterinarian;
    private final String clinic;
    private final String reason;
    private final String status;
    private final String notes;

    public AppointmentResponse(
            Long id,
            Long petId,
            String petName,
            LocalDate date,
            LocalTime time,
            String veterinarian,
            String clinic,
            String reason,
            String status,
            String notes) {
        this.id = id;
        this.petId = petId;
        this.petName = petName;
        this.date = date;
        this.time = time;
        this.veterinarian = veterinarian;
        this.clinic = clinic;
        this.reason = reason;
        this.status = status;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public Long getPetId() { return petId; }
    public String getPetName() { return petName; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getVeterinarian() { return veterinarian; }
    public String getClinic() { return clinic; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
}
