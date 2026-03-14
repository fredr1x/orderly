package pp.restaurantservice.utils;

import lombok.experimental.UtilityClass;
import pp.restaurantservice.dto.MenuItemCreateRequest;
import pp.restaurantservice.entity.MenuItem;

import java.time.LocalDateTime;

@UtilityClass
public class MenuItemUtils {

    public static MenuItem buildMenuItem(MenuItemCreateRequest request, String imageUrl) {
        return MenuItem.builder()
                .menuId(request.getMenuId())
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .price(request.getPrice())
                .weightGrams(request.getWeightGrams())
                .calories(request.getCalories())
                .preparationTimeMinutes(request.getPreparationTimeMinutes())
                .isAvailable(true)
                .imageUrl(imageUrl)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
