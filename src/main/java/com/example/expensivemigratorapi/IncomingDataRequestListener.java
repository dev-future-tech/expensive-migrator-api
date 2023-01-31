package com.example.expensivemigratorapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class IncomingDataRequestListener {

    private final Logger log = LoggerFactory.getLogger(IncomingDataRequestListener.class);

    @RabbitListener(queues = {"data-upload-requests"})
    public void readDataRequest(PersistDataRequest request) {
        log.debug("Incoming table name: {}", request.getTableName());
    }
}
