package io.github.jiajun2001.community.community.event;

import com.alibaba.fastjson.JSONObject;
import io.github.jiajun2001.community.community.entity.Event;
import io.github.jiajun2001.community.community.entity.Message;
import io.github.jiajun2001.community.community.service.MessageService;
import io.github.jiajun2001.community.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW})
    public void handleEventMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("Message is empty!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("Message format is wrong!");
            return;
        }

        // Send message
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID); // We assume
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));

        // Store the message into database
        messageService.addMessage(message);
    }
}
