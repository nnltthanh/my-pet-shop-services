package ct553.backend.pet;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)

public enum PetBreed {
    DOG("Dog"),
    CAT("Cat"),
    HAMSTER("Hamster");

    @Getter
    public String breed;

    public String getValue() {
        return this.breed;
    }

    public static PetBreed from(String breed) {
        return Stream.of(PetBreed.values())
                .filter(p -> p.getBreed().equalsIgnoreCase(breed))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
