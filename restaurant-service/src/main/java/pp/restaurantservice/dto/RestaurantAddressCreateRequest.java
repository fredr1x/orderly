package pp.restaurantservice.dto;

public record RestaurantAddressCreateRequest(
        String formatted,
        String country,
        String city,
        String street,
        String house,
        String floor,
        String comment,
        double longitude,
        double latitude
)
{}
