package app.dto;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
public class HealthRecordRequest {
 @NotNull private Long petId;
 @NotBlank @Size(max=50) private String recordType;
 @NotBlank @Size(max=160) private String title;
 @NotNull private LocalDate date;
 @Size(max=255) private String description;
 @Size(max=120) private String veterinarian;
 @Size(max=160) private String clinic;
 @Pattern(regexp="COMPLETED|UPCOMING|CANCELLED|ACTIVE|INACTIVE") private String status;
 @Size(max=255) private String notes;
 public HealthRecordRequest() { }
 public Long getPetId(){return petId;} public void setPetId(Long v){petId=v;}
 public String getRecordType(){return recordType;} public void setRecordType(String v){recordType=v;}
 public String getTitle(){return title;} public void setTitle(String v){title=v;}
 public LocalDate getDate(){return date;} public void setDate(LocalDate v){date=v;}
 public String getDescription(){return description;} public void setDescription(String v){description=v;}
 public String getVeterinarian(){return veterinarian;} public void setVeterinarian(String v){veterinarian=v;}
 public String getClinic(){return clinic;} public void setClinic(String v){clinic=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}

