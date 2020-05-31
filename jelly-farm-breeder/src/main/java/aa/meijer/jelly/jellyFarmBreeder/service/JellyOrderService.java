package aa.meijer.jelly.jellyFarmService.service;

import aa.meijer.jelly.jellyFarmService.model.jelly.JellyOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class JellyOrderService {

    @Value("${kafka.topic.order}")
    private String orderTopic;

    private KafkaTemplate<String, JellyOrder> kafkaTemplate;

    @Autowired
    public JellyOrderService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void testKafka() {
        JellyOrder order = new JellyOrder();
        order.setString("this is my string");

        kafkaTemplate.send(orderTopic, order);
    }
}
