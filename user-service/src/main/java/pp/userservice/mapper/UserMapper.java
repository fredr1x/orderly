package pp.userservice.mapper;

import org.mapstruct.Mapper;
import pp.userservice.dto.UserDto;
import pp.userservice.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
    User toUserEntity(UserDto userDto);
}
