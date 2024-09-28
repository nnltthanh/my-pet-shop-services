package ct553.backend;

public enum Gender {
    MALE,
    FEMALE,
    OTHER;

   public static Gender from(String value) {
        if (value.equalsIgnoreCase("Đực") || value.equalsIgnoreCase("Nam")) {
            return MALE;
        }
        if (value.equalsIgnoreCase("Cái") || value.equalsIgnoreCase("Nữ")) {
            return FEMALE;
        }
        return OTHER;
    
    }
}
