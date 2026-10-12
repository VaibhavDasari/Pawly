package app.service;

import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import app.dto.PetRequest;
import app.dto.PetResponse;
import app.model.Pet;
import app.model.User;
import app.repository.PetRepository;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserService userService;

    public PetService(PetRepository petRepository, UserService userService) {
        this.petRepository = petRepository;
        this.userService = userService;
    }

    public PetResponse createPet(String email, PetRequest request) {
        User owner = userService.getUserByEmail(email);
        Pet pet = new Pet(
                request.getName(),
                request.getSpecies(),
                request.getBreed(),
                request.getAge(),
                request.getGender(),
                owner
        );
        return toResponse(petRepository.save(pet));
    }

    public List<PetResponse> getPets(String email) {
        return getPets(email, null);
    }

    public List<PetResponse> getPets(String email, String search) {
        User owner = userService.getUserByEmail(email);
        String term = search == null ? null : search.trim().toLowerCase(Locale.ROOT);
        return petRepository.findByOwner(owner).stream()
                .filter(pet -> term == null || term.isEmpty()
                        || contains(pet.getName(), term) || contains(pet.getBreed(), term))
                .map(this::toResponse)
                .toList();
    }

    private boolean contains(String value, String term) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(term);
    }

    public PetResponse getPet(String email, Long id) {
        User owner = userService.getUserByEmail(email);
        return toResponse(findOwnedPet(id, owner));
    }

    public PetResponse updatePet(String email, Long id, PetRequest request) {
        User owner = userService.getUserByEmail(email);
        Pet pet = findOwnedPet(id, owner);
        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        pet.setAge(request.getAge());
        pet.setGender(request.getGender());
        return toResponse(petRepository.save(pet));
    }

    public void deletePet(String email, Long id) {
        User owner = userService.getUserByEmail(email);
        petRepository.delete(findOwnedPet(id, owner));
    }

    private Pet findOwnedPet(Long id, User owner) {
        return petRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
    }

    private PetResponse toResponse(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getBreed(),
                pet.getAge(),
                pet.getGender()
        );
    }
}
