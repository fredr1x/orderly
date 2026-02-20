package pp.geoservice.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class YandexGeoResponse {
    private Response response;

    @Data
    public static class Response {
        @JsonProperty("GeoObjectCollection")
        private GeoObjectCollection GeoObjectCollection;
    }

    @Data
    public static class GeoObjectCollection {
        private List<FeatureMember> featureMember;
    }

    @Data
    public static class FeatureMember {
        @JsonProperty("GeoObject")
        private GeoObject GeoObject;
    }

    @Data
    public static class GeoObject {
        private MetaDataProperty metaDataProperty;
    }

    @Data
    public static class MetaDataProperty {
        @JsonProperty("GeocoderMetaData")
        private GeocoderMetaData GeocoderMetaData;
    }

    @Data
    public static class GeocoderMetaData {
        private String precision;
        private String text;
        private String kind;
        @JsonProperty("Address")
        private Address Address;
    }

    @Data
    public static class Address {
        private String formatted;
        @JsonProperty("country_code")
        private String countryCode;
        @JsonProperty("Components")
        private List<Components> components;
    }

    @Data
    public static class Components {
        private String kind;
        private String name;
    }
}
