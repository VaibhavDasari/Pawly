package app.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
public class PetRequest {
 @NotBlank @Size(max=100) private String name;
 @NotBlank @Size(max=50) private String species;
 @Size(max=100) private String breed;
 @PositiveOrZero private Integer age;
 @Size(max=30) private String gender;
 public PetRequest() { }
 public String getName(){return name;} public void setName(String name){this.name=name;}
 public String getSpecies(){return species;} public void setSpecies(String species){this.species=species;}
 public String getBreed(){return breed;} public void setBreed(String breed){this.breed=breed;}
 public Integer getAge(){return age;} public void setAge(Integer age){this.age=age;}
 public String getGender(){return gender;} public void setGender(String gender){this.gender=gender;}
}
