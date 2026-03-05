package pp.restaurantservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pp.restaurantservice.entity.Restaurant;
import pp.restaurantservice.dto.*;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    RestaurantDto toRestaurantDto(Restaurant restaurant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRestaurantFromRequest(RestaurantUpdateRequest request, @MappingTarget Restaurant restaurant);
}
