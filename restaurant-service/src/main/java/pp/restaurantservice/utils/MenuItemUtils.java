package pp.restaurantservice.utils;

import lombok.experimental.UtilityClass;
import pp.restaurantservice.dto.MenuItemCreateRequest;
import pp.restaurantservice.entity.MenuItem;

import java.time.LocalDateTime;

@UtilityClass
public class MenuItemUtils {

    public static MenuItem buildMenuItem(MenuItemCreateRequest request, String imageUrl) {
        return MenuItem.builder()
                .menuId(request.menuId())
                .name(request.name())
                .description(request.description())
                .type(request.type())
                .price(request.price())
                .weightGrams(request.weightGrams())
                .calories(request.calories())
                .preparationTimeMinutes(request.preparationTimeMinutes())
                .isAvailable(true)
                .imageUrl(imageUrl)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
