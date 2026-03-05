package pp.userservice.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pp.commonlib.domain.RegisterUserRequest;
import pp.userservice.entity.enums.Value;
import pp.userservice.exception.KeycloakException;
import pp.userservice.property.KeycloakProperties;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;
    private final RestTemplate restTemplate;

    public String createUser(RegisterUserRequest requestDto) {
        var user = new UserRepresentation();
        user.setUsername(requestDto.getEmail());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setEmailVerified(true);
        user.setEnabled(true);

        try {
            var response = keycloak.realm(keycloakProperties.getRealm())
                    .users()
                    .create(user);

            if (response.getStatus() == 409) {
                throw new KeycloakException("User with this email already exists in Keycloak");
            }

            if (response.getStatus() != 201) {
                throw new KeycloakException("Unexpected Keycloak response: " + response.getStatus());
            }

            var keycloakId = response.getLocation().getPath()
                    .replaceAll(".*/([^/]+)$", "$1");

            setPassword(keycloakId, requestDto.getPassword());
            assignRealmRole(keycloakId, Value.ROLE_USER.name());
            return keycloakId;

        } catch (KeycloakException e) {
            throw e;
        } catch (Exception e) {
            throw new KeycloakException("Failed to create keycloak user: " + e.getMessage());
        }
    }

    public void authenticate(String username, String password) {

        String url = "http://keycloak:8080" +
                     "/realms/" + keycloakProperties.getRealm() +
                     "/protocol/openid-connect/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", keycloakProperties.getClientId());
        body.add("username", username);
        body.add("password", password);
        body.add("client_secret", keycloakProperties.getClientSecret());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST
                || e.getStatusCode() == HttpStatus.UNAUTHORIZED) {

                throw new BadCredentialsException("Invalid username or password");
            }
            throw e;
        }
    }

    public void assignRealmRole(String userId, String roleName) {

        var role = keycloak.realm(keycloakProperties.getRealm())
                .roles()
                .get(roleName)
                .toRepresentation();

        keycloak.realm(keycloakProperties.getRealm())
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(role));
    }

    public void disableUser(String userId) {
        var userResource = keycloak.realm(keycloakProperties.getRealm())
                .users()
                .get(userId);

        var user = userResource.toRepresentation();
        user.setEnabled(false);

        userResource.update(user);
    }

    private void setPassword(String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        keycloak.realm(keycloakProperties.getRealm())
                .users()
                .get(userId)
                .resetPassword(credential);
    }

    public void deleteUserOnFail(String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) return;

        try {
            keycloak.realm(keycloakProperties.getRealm())
                    .users()
                    .get(keycloakId)
                    .remove();
        } catch (Exception e) {
            throw new RuntimeException("");
        }
    }

    public void changePassword(String keycloakId, String newPassword) {
        var credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newPassword);
        credential.setTemporary(false);

        var userResource = keycloak
                .realm(keycloakProperties.getRealm())
                .users()
                .get(keycloakId);

        userResource.resetPassword(credential);
    }
}
