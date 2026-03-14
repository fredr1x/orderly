package pp.restaurantservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pp.restaurantservice.dto.MenuItemChangeAvailabilityRequest;
import pp.restaurantservice.dto.MenuItemCreateRequest;
import pp.restaurantservice.dto.MenuItemDto;
import pp.restaurantservice.dto.MenuItemUpdateRequest;
import pp.restaurantservice.service.MenuItemService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static pp.restaurantservice.utils.JwtUtils.extractSubject;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping("/{menuItemId}")
    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    public Mono<MenuItemDto> getById(@AuthenticationPrincipal Jwt jwt,
                                     @PathVariable Long menuItemId) {
        var currentUserId = extractSubject(jwt);
        return menuItemService.findById(currentUserId, menuItemId);
    }

    @GetMapping("/{menuId}")
    public Flux<MenuItemDto> getAllByMenuId(@AuthenticationPrincipal Jwt jwt,
                                            @PathVariable Long menuId) {
        var currentUserId = extractSubject(jwt);
        return menuItemService.findAllById(currentUserId, menuId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    public Mono<MenuItemDto> createMenuItem(@AuthenticationPrincipal Jwt jwt,
                                            @RequestPart("request") @Valid MenuItemCreateRequest request,
                                            @RequestPart("file") FilePart file) {
        var currentUserId = extractSubject(jwt);
        return menuItemService.createMenuItem(currentUserId, request, file);
    }

    @PatchMapping("/update")
    public Mono<MenuItemDto> updateMenuItem(@AuthenticationPrincipal Jwt jwt,
                                            @RequestBody MenuItemUpdateRequest request) {
        var currentUserId = extractSubject(jwt);
        return menuItemService.updateMenuItem(currentUserId, request);
    }

    @PatchMapping("/{menuItemId}/available")
    public Mono<MenuItemDto> changeAvailability(@AuthenticationPrincipal Jwt jwt,
                                                @PathVariable Long menuItemId,
                                                @RequestBody MenuItemChangeAvailabilityRequest request) {
        var currentUserId = extractSubject(jwt);
        return menuItemService.changeAvailability(currentUserId, menuItemId, request.isAvailable());
    }

    @PatchMapping(value = "/{menuItemId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    public Mono<Void> updateImage(@AuthenticationPrincipal Jwt jwt,
                                  @PathVariable Long menuItemId,
                                  @RequestParam("file") FilePart file) {
        var currentUserId = extractSubject(jwt);
        return menuItemService.updateItemImage(currentUserId, menuItemId, file);
    }

    @DeleteMapping("/{menuItemId}")
    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    public Mono<Void> deleteImage(@AuthenticationPrincipal Jwt jwt,
                                  @PathVariable Long menuItemId) {
        var currentUserId = extractSubject(jwt);
        return menuItemService.deleteItemImage(currentUserId, menuItemId);
    }
}
