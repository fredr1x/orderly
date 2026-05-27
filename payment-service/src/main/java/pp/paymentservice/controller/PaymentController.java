package pp.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pp.paymentservice.dto.PaymentRequest;
import pp.paymentservice.dto.PaymentResponse;
import pp.paymentservice.service.PaymentService;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public Mono<PaymentResponse> processPayment(@AuthenticationPrincipal Jwt jwt,
                                                @RequestBody PaymentRequest request) {
        return paymentService.processPayment(jwt.getSubject(), request);
    }
}
