package app.dto;

public class PetResponse {

    private final Long id;
    private final String name;
    private final String species;
    private final String breed;
    private final Integer age;
    private final String gender;

    public PetResponse(Long id, String name, String species, String breed, Integer age, String gender) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getBreed() {
        return breed;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }
}
