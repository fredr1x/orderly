package pp.restaurantservice.utils;

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

@UtilityClass
public class GeoUtils {

    public static final int DEFAULT_RADIUS_FOR_SEARCH = 5;

    private static final GeometryFactory GEOMETRY_FACTORY =
            new GeometryFactory(new PrecisionModel(), 4326);

    public static Point toPoint(double longitude, double latitude) {
        return GEOMETRY_FACTORY.createPoint(new Coordinate(longitude, latitude));
    }

    public static String toHex(Point point) {
        WKBWriter writer = new WKBWriter(2, true);
        return WKBWriter.toHex(writer.write(point));
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
}
