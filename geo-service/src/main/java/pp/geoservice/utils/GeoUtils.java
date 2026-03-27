package pp.geoservice.utils;

import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import pp.commonlib.domain.AddressDto;
import pp.geoservice.dto.YandexGeoResponse;
import pp.geoservice.entity.Address;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@UtilityClass
public class GeoUtils {

    public static final int DEFAULT_RADIUS_FOR_SEARCH = 5;

    private static final String UNKNOWN_ADDRESS = "UNKNOWN";
    private static final String KIND_COUNTRY = "country";
    private static final String KIND_PROVINCE = "province";
    private static final String KIND_LOCALITY = "locality";
    private static final String KIND_STREET = "street";
    private static final String KIND_HOUSE = "house";

    private static final GeometryFactory GEOMETRY_FACTORY =
            new GeometryFactory(new PrecisionModel(), 4326);

    public static Point toPoint(double longitude, double latitude) {
        return GEOMETRY_FACTORY.createPoint(new Coordinate(longitude, latitude));
    }

    public static Address mapToAddress(YandexGeoResponse response, double longitude, double latitude) {
        validateResponse(response);

        var addressData = extractAddressData(response);
        return Address.builder()
                .formatted(addressData.getFormatted())
                .location(toPoint(longitude, latitude))
                .country(getComponentByKind(addressData.getComponents(), KIND_COUNTRY).orElse(UNKNOWN_ADDRESS))
                .province(getComponentByKind(addressData.getComponents(), KIND_PROVINCE).orElse(UNKNOWN_ADDRESS))
                .locality(getComponentByKind(addressData.getComponents(), KIND_LOCALITY).orElse(UNKNOWN_ADDRESS))
                .street(getComponentByKind(addressData.getComponents(), KIND_STREET).orElse(UNKNOWN_ADDRESS))
                .house(getComponentByKind(addressData.getComponents(), KIND_HOUSE).orElse(UNKNOWN_ADDRESS))
                .build();
    }

    public static AddressDto mapToAddressDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .formatted(address.getFormatted())
                .country(address.getCountry())
                .province(address.getProvince())
                .locality(address.getLocality())
                .street(address.getStreet())
                .house(address.getHouse())
                .longitude(address.getLocation().getX())
                .latitude(address.getLocation().getY())
                .build();
    }

    private static void validateResponse(YandexGeoResponse response) {
        if (response == null
            || response.getResponse() == null
            || response.getResponse().getGeoObjectCollection() == null
            || response.getResponse().getGeoObjectCollection().getFeatureMember() == null
            || response.getResponse().getGeoObjectCollection().getFeatureMember().isEmpty()) {
            throw new RuntimeException("Invalid or empty response from Yandex Geocoding API");
        }
    }

    private static YandexGeoResponse.Address extractAddressData(YandexGeoResponse response) {
        return response.getResponse()
                .getGeoObjectCollection()
                .getFeatureMember()
                .get(0)
                .getGeoObject()
                .getMetaDataProperty()
                .getGeocoderMetaData()
                .getAddress();
    }

    private static Optional<String> getComponentByKind(List<YandexGeoResponse.Components> components, String kind) {
        if (components == null) {
            return Optional.empty();
        }

        return components.stream()
                .filter(c -> Objects.equals(c.getKind(), kind))
                .findFirst()
                .map(YandexGeoResponse.Components::getName);
    }

    @ReadingConverter
    public static class PointReadConverter implements Converter<String, Point> {
        @Override
        public Point convert(String wkbHex) {
            try {
                WKBReader reader = new WKBReader(GEOMETRY_FACTORY);
                return (Point) reader.read(WKBReader.hexToBytes(wkbHex));
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse WKB hex: " + wkbHex, e);
            }
        }
    }

    @WritingConverter
    public static class PointWriteConverter implements Converter<Point, String> {
        @Override
        public String convert(Point point) {
            WKBWriter writer = new WKBWriter(2, true);
            byte[] bytes = writer.write(point);
            return WKBWriter.toHex(bytes);
        }
    }

    public static String toHex(Point point) {
        WKBWriter writer = new WKBWriter(2, true);
        return WKBWriter.toHex(writer.write(point));
    }
}
