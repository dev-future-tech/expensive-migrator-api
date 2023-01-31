package com.example.expensivemigratorapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith({ContainerCallbackExtension.class, OutputCaptureExtension.class })
@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = RabbitMQTestConfiguration.class)
public class TestIncomingRequests {

    private final Logger log = LoggerFactory.getLogger(TestIncomingRequests.class);
    @Resource
    MessageSenderTestClient client;

    @Resource
    ObjectMapper mapper;

    @Container
    public static GenericContainer<?> rabbit = new GenericContainer<>("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", () -> rabbit.getMappedPort(5672));
        registry.add("spring.rabbitmq.host", () -> rabbit.getContainerIpAddress());
    }

    @Test
    public void testSendMessage(CapturedOutput output) throws Exception {
        PersistDataRequest request = new PersistDataRequest();

        request.setTableName("expenses");
        request.setUniqueId("expense_id");
        request.setUniqueIdClass("String.class");

        StringWriter stringWriter = new StringWriter();
        mapper.writeValue(stringWriter, request);

        String toSend = stringWriter.toString();

        log.debug("Sending message to queue...");
        client.sendMessage(toSend.toString());

        log.debug("Message sent!");

        String expected = String.format("Incoming table name: %s", request.getTableName());
        await("listener response").atMost(40, TimeUnit.SECONDS).untilAsserted(() -> assertThat(output).contains(expected));

    }

}
