package pp.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import pp.restaurantservice.dto.MenuItemCreateRequest;
import pp.restaurantservice.dto.MenuItemDto;
import pp.restaurantservice.dto.MenuItemUpdateRequest;
import pp.restaurantservice.entity.MenuItem;
import pp.restaurantservice.mapper.MenuItemMapper;
import pp.restaurantservice.repository.MenuItemRepository;
import pp.restaurantservice.utils.MenuItemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    private final MinioService minioService;
    private final RestaurantMenuService restaurantMenuService;

    public Mono<MenuItemDto> findById(String currentUserId, Long menuItemId) {
        return findAccessibleMenuItem(currentUserId, menuItemId)
                .map(menuItemMapper::toMenuItemDto);
    }

    public Flux<MenuItemDto> findAllById(String currentUserId, Long menuId) {
        return restaurantMenuService.findAccessibleMenu(UUID.fromString(currentUserId), menuId)
                .thenMany(menuItemRepository.findAllByMenuId(menuId))
                .map(menuItemMapper::toMenuItemDto);
    }

    public Mono<MenuItemDto> createMenuItem(String currentUserId, MenuItemCreateRequest request, FilePart filePart) {
        return restaurantMenuService.findAccessibleMenu(UUID.fromString(currentUserId), request.getMenuId())
                .flatMap(menu -> minioService.uploadMenuItemPhoto(filePart, menu.getRestaurantId(), menu.getId())
                        .flatMap(imageUrl -> menuItemRepository.save(MenuItemUtils.buildMenuItem(request, imageUrl))
                                .map(menuItemMapper::toMenuItemDto)));
    }

    public Mono<MenuItemDto> updateMenuItem(String currentUserId, MenuItemUpdateRequest request) {
        return findAccessibleMenuItem(currentUserId, request.getId())
                .flatMap(menuItem -> {
                    menuItemMapper.updateMenuItemFromRequest(request, menuItem);
                    return menuItemRepository.save(menuItem)
                            .map(menuItemMapper::toMenuItemDto);
                });
    }

    public Mono<Void> updateItemImage(String currentUserId, Long menuItemId, FilePart file) {
        return findAccessibleMenuItem(currentUserId, menuItemId)
                .flatMap(menuItem -> minioService.deleteMenuItemPhoto(menuItem.getImageUrl())
                        .then(restaurantMenuService.findAccessibleMenu(UUID.fromString(currentUserId), menuItem.getMenuId()))
                        .flatMap(menu -> minioService.uploadMenuItemPhoto(file, menu.getRestaurantId(), menu.getId()))
                        .flatMap(newImageUrl -> {
                            menuItem.setImageUrl(newImageUrl);
                            return menuItemRepository.save(menuItem);
                        })
                )
                .then();
    }

    public Mono<MenuItemDto> changeAvailability(String currentUserId, Long menuItemId, boolean available) {
        return findAccessibleMenuItem(currentUserId, menuItemId)
                .flatMap(menuItem -> {
                    menuItem.setAvailable(available);
                    return menuItemRepository.save(menuItem);
                })
                .map(menuItemMapper::toMenuItemDto);
    }

    public Mono<Void> deleteItemImage(String currentUserId, Long menuItemId) {
        return findAccessibleMenuItem(currentUserId, menuItemId)
                .flatMap(menuItem -> minioService.deleteMenuItemPhoto(menuItem.getImageUrl()))
                .then();
    }

    public Mono<MenuItem> findAccessibleMenuItem(String currentUserId, Long menuItemId) {
        return menuItemRepository.findById(menuItemId)
                .switchIfEmpty(Mono.error(new RuntimeException("Menu item not found")))
                .flatMap(menuItem ->
                        restaurantMenuService.findAccessibleMenu(UUID.fromString(currentUserId), menuItem.getMenuId())
                                .thenReturn(menuItem)
                );
    }
}
