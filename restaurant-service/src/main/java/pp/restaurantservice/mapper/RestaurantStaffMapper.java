package pp.restaurantservice.mapper;

import org.mapstruct.Mapper;
import pp.restaurantservice.dto.RestaurantStaffDto;
import pp.restaurantservice.entity.RestaurantStaff;

@Mapper(componentModel = "spring")
public interface RestaurantStaffMapper {
    RestaurantStaffDto toRestaurantStaffDto(RestaurantStaff restaurantStaff);
}
