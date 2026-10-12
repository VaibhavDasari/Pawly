package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.HealthRecord;
import app.model.User;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

    List<HealthRecord> findByPetOwner(User owner);

    Optional<HealthRecord> findByIdAndPetOwner(Long id, User owner);
}
