package pp.userservice.configuration;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pp.userservice.property.KeycloakProperties;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfiguration {

    private final KeycloakProperties keycloakProperties;
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .username(keycloakProperties.getUsername())
                .password(keycloakProperties.getPassword())
                .grantType(OAuth2Constants.PASSWORD)
                .serverUrl(keycloakProperties.getServerUrl())
                .realm(keycloakProperties.getRealm())
                .clientId(keycloakProperties.getClientId())
                .clientSecret(keycloakProperties.getClientSecret())
                .build();
    }
}
