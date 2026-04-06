package pp.restaurantservice.dto;

public record BecomePartnerRequest(
    String email,
    String password,
    String phoneNumber,
    String firstName,
    String lastName,

    String brandName,
    String brandDescription,
    String imageProfileLink
)
{}
