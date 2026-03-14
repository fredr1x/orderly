package pp.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pp.restaurantservice.dto.RestaurantMenuChangeStatusRequest;
import pp.restaurantservice.dto.RestaurantMenuCreateRequest;
import pp.restaurantservice.dto.RestaurantMenuDto;
import pp.restaurantservice.dto.RestaurantMenuTypeUpdatedRequest;
import pp.restaurantservice.entity.RestaurantMenu;
import pp.restaurantservice.mapper.RestaurantMenuMapper;
import pp.restaurantservice.repository.RestaurantMenuRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.UUID.fromString;
import static pp.restaurantservice.utils.RestaurantMenuUtils.buildRestaurant;

@Service
@RequiredArgsConstructor
public class RestaurantMenuService {

    private final RestaurantMenuRepository restaurantMenuRepository;

    private final RestaurantMenuMapper restaurantMenuMapper;

    private final RestaurantStaffService restaurantStaffService;
    private final RestaurantService restaurantService;

    public Mono<RestaurantMenuDto> getById(String currentUserId, Long menuId) {
        return findAccessibleMenu(fromString(currentUserId), menuId)
                .then(restaurantMenuRepository.findById(menuId)
                        .map(restaurantMenuMapper::toRestaurantMenuDto));
    }

    public Flux<RestaurantMenuDto> getAllByRestaurantId(String currentUserId, Long restaurantId) {
        return restaurantService.validateStaffAndRestaurant(fromString(currentUserId), restaurantId)
                .thenMany(restaurantMenuRepository.findAllByRestaurantId(restaurantId))
                .map(restaurantMenuMapper::toRestaurantMenuDto);
    }

    public Mono<RestaurantMenuDto> createMenu(String currentUserId, RestaurantMenuCreateRequest request) {
        return restaurantStaffService.validateManager(currentUserId, request.getRestaurantId())
                .then(restaurantMenuRepository.save(buildRestaurant(request)))
                .map(restaurantMenuMapper::toRestaurantMenuDto);
    }

    public Mono<RestaurantMenuDto> updateMenuType(String currentUserId, RestaurantMenuTypeUpdatedRequest request) {
        return findAccessibleMenu(fromString(currentUserId), request.getMenuId())
                .flatMap(menu -> {
                    menu.setType(request.getMenuType());
                    menu.setUpdatedAt(LocalDateTime.now());
                    return restaurantMenuRepository.save(menu);
                })
                .map(restaurantMenuMapper::toRestaurantMenuDto);
    }

    public Mono<RestaurantMenuDto> changeMenuStatus(String currentUserId, RestaurantMenuChangeStatusRequest request) {
        return findAccessibleMenu(fromString(currentUserId), request.getMenuId())
                .flatMap(menu -> {
                    menu.setActive(request.isActive());
                    menu.setUpdatedAt(LocalDateTime.now());
                    return restaurantMenuRepository.save(menu);
                })
                .map(restaurantMenuMapper::toRestaurantMenuDto);
    }

    public Mono<Void> deleteMenu(String currentUserId, Long menuId) {
        return findAccessibleMenu(fromString(currentUserId), menuId)
                .then(restaurantMenuRepository.deleteById(menuId));
    }

    public Mono<RestaurantMenu> findAccessibleMenu(UUID currentUserId, Long menuId) {
        return restaurantMenuRepository.findById(menuId)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("Menu not found")))
                .flatMap(menu -> restaurantStaffService
                        .validateManager(currentUserId.toString(), menu.getRestaurantId())
                        .thenReturn(menu));
    }
}
