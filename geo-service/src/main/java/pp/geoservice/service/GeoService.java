package pp.geoservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pp.commonlib.domain.AddressDto;
import pp.geoservice.mapper.AddressMapper;
import pp.geoservice.utils.GeoUtils;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeoService {

    private final AddressMapper addressMapper;
    private final AddressService addressService;
    private final YandexGeoService yandexGeoService;

    @Transactional
    public Mono<AddressDto> reverseGeocoding(double longitude, double latitude) {
        return addressService
                .findByLocation(longitude, latitude)
                .map(GeoUtils::mapToAddressDto)
                .switchIfEmpty(
                        yandexGeoService.reverse(longitude, latitude)
                                .flatMap(response -> {
                                    var address = GeoUtils.mapToAddress(response, longitude, latitude);
                                    return addressService.saveAddressAndReturn(address)
                                            .map(addressMapper::toDto);
                                })
                );
    }
}
