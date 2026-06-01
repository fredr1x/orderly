package pp.orderservice.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pp.commonlib.domain.event.OrderPaidEvent;
import pp.commonlib.domain.event.OrderRejectedEvent;
import pp.commonlib.domain.event.PaymentCompletedEvent;
import pp.commonlib.domain.event.RestaurantDecisionEvent;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers:kafka:9092}")
    private String bootstrapServers;

    @Value("${kafka.payment-topic:payment-events}")
    private String paymentEventsTopic;

    @Value("${kafka.restaurant-topic:restaurant-events}")
    private String restaurantEventsTopic;

    @Bean
    public SenderOptions<Long, OrderPaidEvent> orderPaidEventSenderOptions() {
        Map<String, Object> props = senderProperties();

        return SenderOptions.create(props);
    }

    @Bean
    public KafkaSender<Long, OrderPaidEvent> orderPaidEventSender(SenderOptions<Long, OrderPaidEvent> orderPaidEventSenderOptions) {
        return KafkaSender.create(orderPaidEventSenderOptions);
    }

    @Bean
    public SenderOptions<Long, OrderRejectedEvent> orderRejectedEventSenderOptions() {
        Map<String, Object> props = senderProperties();

        return SenderOptions.create(props);
    }

    @Bean
    public KafkaSender<Long, OrderRejectedEvent> orderRejectedEventKafkaSender(SenderOptions<Long, OrderRejectedEvent> orderRejectedEventSenderOptions) {
        return KafkaSender.create(orderRejectedEventSenderOptions);
    }

    @Bean
    public SenderOptions<Long, String> outboxSenderOptions() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Long.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return SenderOptions.create(props);
    }

    @Bean
    public KafkaSender<Long, String> outboxKafkaSender(SenderOptions<Long, String> outboxSenderOptions) {
        return KafkaSender.create(outboxSenderOptions);
    }

    @Bean
    public ReceiverOptions<Long, PaymentCompletedEvent> paymentCompletedEventReceiverOptions() {
        Map<String, Object> props = receiverProperties();

        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, PaymentCompletedEvent.class.getName());
        return ReceiverOptions.<Long, PaymentCompletedEvent>create(props)
                .subscription(List.of(paymentEventsTopic));
    }

    @Bean
    public KafkaReceiver<Long, PaymentCompletedEvent> paymentEventKafkaReceiver(ReceiverOptions<Long, PaymentCompletedEvent> paymentCompletedEventReceiverOptions) {
        return KafkaReceiver.create(paymentCompletedEventReceiverOptions);
    }

    @Bean
    public ReceiverOptions<Long, RestaurantDecisionEvent> restaurantDecisionEventReceiverOptions() {
        Map<String, Object> props = receiverProperties();

        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, RestaurantDecisionEvent.class.getName());
        return ReceiverOptions.<Long, RestaurantDecisionEvent>create(props)
                .subscription(List.of(restaurantEventsTopic));
    }

    @Bean
    public KafkaReceiver<Long, RestaurantDecisionEvent> restaurantDecisionEventKafkaReceiver(ReceiverOptions<Long, RestaurantDecisionEvent> restaurantDecisionEventReceiverOptions) {
        return KafkaReceiver.create(restaurantDecisionEventReceiverOptions);
    }

    private Map<String, Object> senderProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return props;
    }

    private Map<String, Object> receiverProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }
}
