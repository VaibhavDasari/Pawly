package app.dto;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
public class AppointmentRequest {
 @NotNull private Long petId;
 @NotNull private LocalDate date;
 @NotNull private LocalTime time;
 @NotBlank @Size(max=120) private String veterinarian;
 @NotBlank @Size(max=160) private String clinic;
 @NotBlank @Size(max=160) private String reason;
 @Pattern(regexp="UPCOMING|COMPLETED|CANCELLED") private String status;
 @Size(max=255) private String notes;
 public AppointmentRequest() { }
 public Long getPetId(){return petId;} public void setPetId(Long v){petId=v;}
 public LocalDate getDate(){return date;} public void setDate(LocalDate v){date=v;}
 public LocalTime getTime(){return time;} public void setTime(LocalTime v){time=v;}
 public String getVeterinarian(){return veterinarian;} public void setVeterinarian(String v){veterinarian=v;}
 public String getClinic(){return clinic;} public void setClinic(String v){clinic=v;}
 public String getReason(){return reason;} public void setReason(String v){reason=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}

