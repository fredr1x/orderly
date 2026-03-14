package pp.restaurantservice.configuration.initializer;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pp.restaurantservice.properties.MinioProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioInitializer implements ApplicationRunner {

    private final MinioProperties minioProperties;
    private final MinioClient minioClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var bucketName = minioProperties.getBucketName();
        var exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build());
            log.info("Created Minio bucket: {}", bucketName);
        }
    }
}
