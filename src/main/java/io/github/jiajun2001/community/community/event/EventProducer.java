package io.github.jiajun2001.community.community.event;

import com.alibaba.fastjson.JSONObject;
import io.github.jiajun2001.community.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    // Handle events
    public void fireEvent(Event event) {
        // Send the event to specific topic
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }

}
