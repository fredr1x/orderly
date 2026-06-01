package pp.paymentservice.configuration;

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
import pp.commonlib.domain.event.OrderRejectedEvent;
import pp.commonlib.domain.event.PaymentCompletedEvent;
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

    @Value("${kafka.order-topic:order-events}")
    private String orderTopic;

    @Bean
    public SenderOptions<Long, PaymentCompletedEvent> senderOptions() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return SenderOptions.create(props);
    }

    @Bean
    public KafkaSender<Long, PaymentCompletedEvent> paymentEventKafkaSender(SenderOptions<Long, PaymentCompletedEvent> senderOptions) {
        return KafkaSender.create(senderOptions);
    }

    @Bean
    public ReceiverOptions<Long, OrderRejectedEvent> orderRejectedEventReceiverOptions() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, OrderRejectedEvent.class.getName());

        return ReceiverOptions.<Long, OrderRejectedEvent>create(props)
                .subscription(List.of(orderTopic));
    }

    @Bean
    public KafkaReceiver<Long, OrderRejectedEvent> orderEventKafkaReceiver(ReceiverOptions<Long, OrderRejectedEvent> orderRejectedEventReceiverOptions) {
        return KafkaReceiver.create(orderRejectedEventReceiverOptions);
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
}
