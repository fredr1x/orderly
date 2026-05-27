package pp.restaurantservice.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import pp.commonlib.domain.event.OrderPaidEvent;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers:kafka:9092}")
    private String bootstrapServers;

    @Value("${kafka.order-topic:order-events}")
    private String orderTopic;

    @Bean
    public ReceiverOptions<Long, OrderPaidEvent> orderPaidEventReceiverOptions() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "restaurant-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");


        return ReceiverOptions.<Long, OrderPaidEvent>create(props)
                .subscription(List.of(orderTopic));
    }

    @Bean
    public KafkaReceiver<Long, OrderPaidEvent> orderPaidEventKafkaReceiver(ReceiverOptions<Long, OrderPaidEvent> orderPaidEventReceiverOptions) {
        return KafkaReceiver.create(orderPaidEventReceiverOptions);
    }
}
