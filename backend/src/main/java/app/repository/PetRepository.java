package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Pet;
import app.model.User;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByOwner(User owner);

    Optional<Pet> findByIdAndOwner(Long id, User owner);
}
