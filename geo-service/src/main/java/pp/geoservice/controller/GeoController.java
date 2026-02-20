package pp.geoservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pp.commonlib.domain.AddressDto;
import pp.geoservice.service.GeoService;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/geo")
public class GeoController {

    private final GeoService geoService;

    @GetMapping("/reverse")
    public Mono<AddressDto> reverseGeocoding(@RequestParam double longitude,
                                             @RequestParam double latitude) {

        return geoService.reverseGeocoding(longitude, latitude);
    }
}
