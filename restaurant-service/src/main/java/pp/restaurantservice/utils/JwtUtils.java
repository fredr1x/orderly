package pp.restaurantservice.utils;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class JwtUtils {

    public static Mono<List<String>> extractRoles() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> (JwtAuthenticationToken) ctx.getAuthentication())
                .map(AbstractOAuth2TokenAuthenticationToken::getToken)
                .map(jwt -> {
                    var realmAccess = jwt.getClaimAsMap("realm_access");
                    return (List<String>) realmAccess.get("roles");
                });
    }

    public static Mono<UUID> extractUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> (JwtAuthenticationToken) ctx.getAuthentication())
                .map(AbstractOAuth2TokenAuthenticationToken::getToken)
                .map(jwt -> UUID.fromString(jwt.getSubject()));
    }

    public static String extractSubject(Jwt jwt) {
        return jwt.getSubject();
    }
}