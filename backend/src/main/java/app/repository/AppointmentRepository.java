package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Appointment;
import app.model.User;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPetOwner(User owner);

    Optional<Appointment> findByIdAndPetOwner(Long id, User owner);
}
