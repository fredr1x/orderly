package pp.userservice.feign;

import pp.commonlib.domain.AddressDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "geo-service")
public interface GeoServiceClient {

    @GetMapping("/api/geo/reverse")
    AddressDto reverseGeocoding(@RequestParam("longitude") double longitude,
                             @RequestParam("latitude") double latitude);
}
