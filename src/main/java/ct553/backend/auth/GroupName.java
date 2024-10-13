package ct553.backend.auth;

import java.util.*;

public class GroupName {
    
    public static final String RECEPTIONIST_STAFF = "34b977a8-9854-4388-9d62-d51a5e89db70";

    public static final String CUSTOMER = "f4c676da-1fd4-447b-874c-ee7220f53b50";

    public static final String SERVICE_STAFF = "9e953bdd-f182-49a7-b506-5a3ca356a2fd";

    public static final String ADMIN = "3d4f765a-a998-4600-ae0d-70a0c83b09bb";

    public static Map<String, String> groupNameIdMap = new HashMap<>();

    static {
        groupNameIdMap.put("RECEPTIONIST_STAFF", RECEPTIONIST_STAFF);
        groupNameIdMap.put("CUSTOMER", CUSTOMER);
        groupNameIdMap.put("SERVICE_STAFF", SERVICE_STAFF);
        groupNameIdMap.put("ADMIN", ADMIN);
    }
    
}
