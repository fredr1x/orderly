package pp.userservice.mapper;

import org.mapstruct.Mapper;
import pp.userservice.dto.UserAddressDto;
import pp.userservice.entity.UserAddress;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {
    UserAddressDto toUserAddressDto(UserAddress userAddress);
}
