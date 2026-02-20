package pp.geoservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pp.geoservice.entity.Address;
import pp.geoservice.repository.AddressRepository;
import pp.geoservice.utils.GeoUtils;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Mono<Address> findByLocation(double longitude, double latitude) {
        return addressRepository.findByPointAndRadius(longitude, latitude, GeoUtils.DEFAULT_RADIUS_FOR_SEARCH)
                .switchIfEmpty(addressRepository.findNearest(longitude, latitude));
    }

    public Mono<Address> saveAddressAndReturn(Address address) {
        return addressRepository.findByFormatted(address.getFormatted())
                .switchIfEmpty(
                        addressRepository.saveAddressAndReturn(
                                address.getFormatted(),
                                GeoUtils.toHex(address.getLocation()),
                                address.getCountry(),
                                address.getProvince(),
                                address.getLocality(),
                                address.getStreet(),
                                address.getHouse()
                        )
                );
    }
}
