package app.dto;

import java.time.LocalDate;

public class HealthRecordResponse {

    private final Long id;
    private final Long petId;
    private final String petName;
    private final String recordType;
    private final String title;
    private final LocalDate date;
    private final String description;
    private final String veterinarian;
    private final String clinic;
    private final String status;
    private final String notes;

    public HealthRecordResponse(
            Long id,
            Long petId,
            String petName,
            String recordType,
            String title,
            LocalDate date,
            String description,
            String veterinarian,
            String clinic,
            String status,
            String notes) {
        this.id = id;
        this.petId = petId;
        this.petName = petName;
        this.recordType = recordType;
        this.title = title;
        this.date = date;
        this.description = description;
        this.veterinarian = veterinarian;
        this.clinic = clinic;
        this.status = status;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public Long getPetId() { return petId; }
    public String getPetName() { return petName; }
    public String getRecordType() { return recordType; }
    public String getTitle() { return title; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public String getVeterinarian() { return veterinarian; }
    public String getClinic() { return clinic; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
}
