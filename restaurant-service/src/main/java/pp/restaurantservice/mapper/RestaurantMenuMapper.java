package pp.restaurantservice.mapper;

import org.mapstruct.Mapper;
import pp.restaurantservice.dto.RestaurantMenuDto;
import pp.restaurantservice.entity.RestaurantMenu;

@Mapper(componentModel = "spring")
public interface RestaurantMenuMapper {

    RestaurantMenuDto toRestaurantMenuDto(RestaurantMenu restaurantMenu);
}
