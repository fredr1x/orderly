package pp.restaurantservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pp.restaurantservice.dto.RestaurantAddressDto;
import pp.restaurantservice.dto.RestaurantAddressUpdateRequest;
import pp.restaurantservice.entity.RestaurantAddress;

@Mapper(componentModel = "spring")
public interface RestaurantAddressMapper {

    default RestaurantAddressDto toRestaurantAddressDto(RestaurantAddress restaurantAddress) {
        return RestaurantAddressDto.builder()
                .id(restaurantAddress.getId())
                .restaurantId(restaurantAddress.getRestaurantId())
                .formatted(restaurantAddress.getFormatted())
                .country(restaurantAddress.getCountry())
                .city(restaurantAddress.getCity())
                .street(restaurantAddress.getStreet())
                .house(restaurantAddress.getHouse())
                .floor(restaurantAddress.getFloor())
                .comment(restaurantAddress.getComment())
                .createdAt(restaurantAddress.getCreatedAt())
                .updatedAt(restaurantAddress.getUpdatedAt())
                .longitude(restaurantAddress.getLocation().getX())
                .latitude(restaurantAddress.getLocation().getY())
                .build();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurantId", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateRestaurantAddressFromRequest(RestaurantAddressUpdateRequest request,
                                            @MappingTarget RestaurantAddress restaurantAddress);
}
