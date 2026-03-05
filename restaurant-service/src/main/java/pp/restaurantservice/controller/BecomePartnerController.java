package pp.restaurantservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pp.restaurantservice.dto.BecomePartnerRequest;
import pp.restaurantservice.dto.BecomePartnerResponse;
import pp.restaurantservice.service.BecomePartnerService;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/become-partner")
public class BecomePartnerController {

    private final BecomePartnerService becomePartnerService;

    @PostMapping
    public Mono<BecomePartnerResponse> becomePartner(@RequestBody BecomePartnerRequest request) {
        return becomePartnerService.becomePartner(request);
    }
}
