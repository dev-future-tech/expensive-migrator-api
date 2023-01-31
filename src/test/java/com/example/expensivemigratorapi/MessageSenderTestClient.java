package com.example.expensivemigratorapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSenderTestClient {

    private final Logger log = LoggerFactory.getLogger(MessageSenderTestClient.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String strMessage) {
        log.debug("Sending message {}...", strMessage);
        Message message = MessageBuilder.withBody(strMessage.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
        this.rabbitTemplate.convertAndSend("test-data-upload", "data.requests.save", message);
        log.debug("Sent message.");
    }

}
