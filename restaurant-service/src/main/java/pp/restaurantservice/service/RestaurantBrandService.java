package pp.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pp.restaurantservice.dto.RestaurantBrandDto;
import pp.restaurantservice.mapper.RestaurantBrandMapper;
import pp.restaurantservice.repository.RestaurantBrandRepository;
import pp.restaurantservice.utils.BrandUtils;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantBrandService {

    private final RestaurantBrandMapper restaurantBrandMapper;

    private final RestaurantBrandRepository restaurantBrandRepository;

    public Mono<RestaurantBrandDto> findById(Long id) {
        return restaurantBrandRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Brand not found")))
                .map(restaurantBrandMapper::toRestaurantBrandDto);
    }

    public Mono<RestaurantBrandDto> findByOwnerUserId(UUID ownerUserId) {
        System.out.println("OWNER USER ID: " + ownerUserId);
        return restaurantBrandRepository.findByOwnerUserId(ownerUserId)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Brand not found")))
                .map(restaurantBrandMapper::toRestaurantBrandDto);
    }

    public Mono<RestaurantBrandDto> createBrand(String currentUserId, String name, String description) {
        return existsByName(name)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(() -> new RuntimeException(
                                "Brand with name: " + name + " already exists"));
                    }

                    var restaurantBrand = BrandUtils.buildRestaurantBrand(UUID.fromString(currentUserId), name, description);
                    return restaurantBrandRepository.save(restaurantBrand)
                            .map(restaurantBrandMapper::toRestaurantBrandDto);
                });
    }

    public Mono<Void> validateRelatedBrand(Long brandId, UUID ownerUserId) {
        return findById(brandId)
                .flatMap(brand -> restaurantBrandRepository.validatedRelationship(brand.getId(), ownerUserId)
                        .hasElement()
                        .flatMap(exists -> exists
                                ? Mono.empty()
                                : Mono.error(() -> new RuntimeException("Not enough permissions to this brand"))
                        ));
    }

    private Mono<Boolean> existsByName(String name) {
        return restaurantBrandRepository.existsByName(name);
    }
}
