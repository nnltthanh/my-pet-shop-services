package ct553.backend.auth;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import ct553.backend.user.UserDTO;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status.Family;

import java.util.Optional;

@Service
public class UserKeycloakSerivce {

    public void createUser(UserDTO user) {
        Response response = getKeycloak().realm("petshoprealm").users().create(this.userDtoToUserRepresentation(user));
        if (response != null && response.getStatusInfo().getFamily() == Family.SUCCESSFUL && user.getGroups() != null && !user.getGroups().isEmpty()) 
        {
            for (String group : user.getGroups()) {

                getKeycloak().realm("petshoprealm")
                            .users()
                            .get(CreatedResponseUtil.getCreatedId(response))
                            .joinGroup(GroupName.groupNameIdMap.get(group)); // use group id
            }
        }
    }

    public Optional<UserRepresentation> findByAccount(String account)  {
        List<UserRepresentation> users = getKeycloak().realm("petshoprealm").users().search(account);
        return CollectionUtils.emptyIfNull(users)
                    .stream()
                    .findFirst();
    }

    private UserRepresentation userDtoToUserRepresentation(UserDTO userDTO) {
        UserRepresentation userRepresentation = new UserRepresentation();

        List<CredentialRepresentation> credentials = new ArrayList<>();
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(userDTO.getPassword());
        credentialRepresentation.setTemporary(false);
        credentials.add(credentialRepresentation);

        userRepresentation.setUsername(userDTO.getAccount());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setFirstName(userDTO.getName());
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(credentials);

        return userRepresentation;
    }

    private Keycloak getKeycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8180")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm("petshoprealm")
                .clientId("pet-shop-services")
                .clientSecret("XupbwIxB6ROUmv1AEDPtO4Aq7LGJrrEz")
                .build();
    }

}
