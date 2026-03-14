package pp.restaurantservice.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import pp.restaurantservice.properties.MinioProperties;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayInputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public Mono<String> uploadMenuItemPhoto(FilePart filePart, Long restaurantId, Long menuId) {
        String filename = restaurantId + "/" + menuId + "/" + UUID.randomUUID() + "_" + filePart.filename();

        return DataBufferUtils.join(filePart.content())
                .flatMap(dataBuffer -> Mono.fromCallable(() -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);

                    try (var is = new ByteArrayInputStream(bytes)) {
                        minioClient.putObject(PutObjectArgs.builder()
                                        .bucket(minioProperties.getBucketName())
                                        .object(filename)
                                        .stream(is, bytes.length, -1)
                                        .contentType(filePart.headers().getContentType() != null
                                                ? Objects.requireNonNull(filePart.headers().getContentType()).toString()
                                                : MediaType.APPLICATION_OCTET_STREAM_VALUE)
                                        .build());
                    }

                    return filename;
                })).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Void> deleteMenuItemPhoto(String filename) {
        return Mono.fromCallable(() -> {
           minioClient.removeObject(RemoveObjectArgs.builder()
                           .bucket(minioProperties.getBucketName())
                           .object(filename)
                           .build()
           );
           return null;
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}
