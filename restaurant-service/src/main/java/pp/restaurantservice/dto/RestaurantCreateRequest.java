package pp.restaurantservice.dto;

public record RestaurantCreateRequest(
    Long brandId,
    String name,
    String phoneNumber,
    String email,
    String instagramProfileLink
)
{}
