package pp.geoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableDiscoveryClient
@SpringBootApplication
public class GeoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoServiceApplication.class, args);
	}
}
