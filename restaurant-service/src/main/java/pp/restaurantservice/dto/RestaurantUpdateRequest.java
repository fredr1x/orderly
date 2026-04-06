package pp.restaurantservice.dto;

public record RestaurantUpdateRequest(
    Long restaurantId,
    String name,
    String email,
    String phoneNumber,
    String instagramProfileLink
)
{}
