package pp.restaurantservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pp.restaurantservice.dto.RestaurantRatingDto;
import pp.restaurantservice.dto.RestaurantRatingUpdateRequest;
import pp.restaurantservice.entity.RestaurantRating;

@Mapper(componentModel = "spring")
public interface RestaurantRatingMapper {

    RestaurantRatingDto toRestaurantRatingDto(RestaurantRating restaurantRating);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRestaurantRatingFromRequest(RestaurantRatingUpdateRequest request,
                                           @MappingTarget RestaurantRating restaurantRating);
}
