
package app.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import app.dto.HealthRecordRequest;
import app.dto.HealthRecordResponse;
import app.model.HealthRecord;
import app.model.Pet;
import app.model.User;
import app.repository.HealthRecordRepository;
import app.repository.PetRepository;

@Service
public class HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;
    private final PetRepository petRepository;
    private final UserService userService;

    public HealthRecordService(
            HealthRecordRepository healthRecordRepository,
            PetRepository petRepository,
            UserService userService) {
        this.healthRecordRepository = healthRecordRepository;
        this.petRepository = petRepository;
        this.userService = userService;
    }

    public HealthRecordResponse createHealthRecord(String email, HealthRecordRequest request) {
        User owner = userService.getUserByEmail(email);
        Pet pet = findOwnedPet(request.getPetId(), owner);
        HealthRecord record = new HealthRecord();
        record.setPet(pet);
        applyRequest(record, request);
        return toResponse(healthRecordRepository.save(record));
    }

    public List<HealthRecordResponse> getHealthRecords(String email) {
        return getHealthRecords(email, null, null, null);
    }

    public List<HealthRecordResponse> getHealthRecords(String email, Long petId, String recordType, String status) {
        User owner = userService.getUserByEmail(email);
        if (petId != null) {
            findOwnedPet(petId, owner);
        }
        return healthRecordRepository.findByPetOwner(owner).stream()
                .filter(record -> petId == null || petId.equals(record.getPet().getId()))
                .filter(record -> recordType == null || recordType.isBlank()
                        || recordType.equalsIgnoreCase(record.getRecordType()))
                .filter(record -> status == null || status.isBlank()
                        || status.equalsIgnoreCase(record.getStatus()))
                .map(this::toResponse)
                .toList();
    }

    public HealthRecordResponse getHealthRecord(String email, Long id) {
        User owner = userService.getUserByEmail(email);
        return toResponse(findOwnedRecord(id, owner));
    }

    public HealthRecordResponse updateHealthRecord(String email, Long id, HealthRecordRequest request) {
        User owner = userService.getUserByEmail(email);
        HealthRecord record = findOwnedRecord(id, owner);
        Pet pet = findOwnedPet(request.getPetId(), owner);
        record.setPet(pet);
        applyRequest(record, request);
        return toResponse(healthRecordRepository.save(record));
    }

    public void deleteHealthRecord(String email, Long id) {
        User owner = userService.getUserByEmail(email);
        healthRecordRepository.delete(findOwnedRecord(id, owner));
    }

    private Pet findOwnedPet(Long petId, User owner) {
        return petRepository.findByIdAndOwner(petId, owner)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
    }

    private HealthRecord findOwnedRecord(Long id, User owner) {
        return healthRecordRepository.findByIdAndPetOwner(id, owner)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Health record not found"));
    }

    private void applyRequest(HealthRecord record, HealthRecordRequest request) {
        record.setRecordType(request.getRecordType());
        record.setTitle(request.getTitle());
        record.setDate(request.getDate());
        record.setDescription(request.getDescription());
        record.setVeterinarian(request.getVeterinarian());
        record.setClinic(request.getClinic());
        record.setStatus(request.getStatus());
        record.setNotes(request.getNotes());
    }

    private HealthRecordResponse toResponse(HealthRecord record) {
        Pet pet = record.getPet();
        return new HealthRecordResponse(
                record.getId(),
                pet.getId(),
                pet.getName(),
                record.getRecordType(),
                record.getTitle(),
                record.getDate(),
                record.getDescription(),
                record.getVeterinarian(),
                record.getClinic(),
                record.getStatus(),
                record.getNotes()
        );
    }
}
