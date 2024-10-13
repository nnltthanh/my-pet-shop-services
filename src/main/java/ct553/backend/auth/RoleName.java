package ct553.backend.auth;

import java.util.List;

public class RoleName {
    
    public static final String RECEPTIONIST = "RECEPTIONIST";

    public static final String CUSTOMER = "CUSTOMER";

    public static final String SERVICE_STAFF = "SERVICE_STAFF";

    public static final String ADMIN = "ADMIN";

    public static final List<String> roles() {
        return List.of(RECEPTIONIST, CUSTOMER, SERVICE_STAFF, ADMIN);
    }

}
