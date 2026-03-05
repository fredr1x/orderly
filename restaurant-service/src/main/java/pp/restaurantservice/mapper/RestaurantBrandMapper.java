package pp.restaurantservice.mapper;

import org.mapstruct.Mapper;
import pp.restaurantservice.dto.RestaurantBrandDto;
import pp.restaurantservice.entity.RestaurantBrand;

@Mapper(componentModel = "spring")
public interface RestaurantBrandMapper {
    RestaurantBrandDto toRestaurantBrandDto(RestaurantBrand restaurantBrand);
}
