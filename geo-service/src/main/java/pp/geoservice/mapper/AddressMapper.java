package pp.geoservice.mapper;

import org.mapstruct.Mapper;
import pp.commonlib.domain.AddressDto;
import pp.geoservice.entity.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto toDto(Address address);
}
