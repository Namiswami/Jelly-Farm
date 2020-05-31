package aa.meijer.jellyfarm.listener;

import aa.meijer.jellyfarm.model.jelly.JellyOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderListener {

    @KafkaListener(topics = "${kafka.topic.order}")
    public void listenToOrders(@Payload JellyOrder order) {
        log.info("it works");
    }
}
