package pp.restaurantservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pp.restaurantservice.dto.MenuItemDto;
import pp.restaurantservice.dto.MenuItemUpdateRequest;
import pp.restaurantservice.entity.MenuItem;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    MenuItemDto toMenuItemDto(MenuItem menuItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMenuItemFromRequest(MenuItemUpdateRequest request, @MappingTarget MenuItem menuItem);
}
