package pp.restaurantservice.service.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pp.commonlib.domain.RegisterUserRequest;
import pp.commonlib.domain.UserDto;
import reactor.core.publisher.Mono;

@Service
public class UserServiceClient {

    private final WebClient webClient;

    public UserServiceClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://user-service").build();
    }

    public Mono<UserDto> createUser(RegisterUserRequest request) {
        return webClient.post()
                .uri("/api/auth/register-user")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserDto.class);
    }

    public Mono<UserDto> createOwner(RegisterUserRequest request) {
        return webClient.post()
                .uri("/api/auth/register-owner")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserDto.class);
    }

    public Mono<UserDto> createManager(RegisterUserRequest request) {
        return webClient.post()
                .uri("/api/auth/register-manager")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserDto.class);
    }
}
