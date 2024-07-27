package ct553.backend.pet;

public enum PetBreed {
    
    DOG("Dog"),
    CAT("Cat"),
    HAMSTER("Hamster");

    public final String breed;

    PetBreed(String breed) {
        this.breed = breed;
    }


}
