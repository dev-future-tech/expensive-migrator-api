package com.example.expensivemigratorapi;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class DataStoreManagerTest {

    @Container
    static final GenericContainer dynamoDb = new GenericContainer("amazon/dynamodb-local:1.13.2")
            .withCommand("-jar DynamoDBLocal.jar -inMemory -sharedDb")
            .withExposedPorts(8000);

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("amazon.aws.endpoint", () -> String.format("http://%s:%s", dynamoDb.getContainerIpAddress(), dynamoDb.getFirstMappedPort()));
        registry.add("amazon.aws.region", () -> "us-east-1");
        registry.add("amazon.aws.accessKey", () -> "hello");
        registry.add("amazon.aws.secretKey", () -> "superman");
    }


}
