package com.example.expensivemigratorapi;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.model.*;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DynamoDBExtension implements BeforeEachCallback, AfterEachCallback, BeforeAllCallback {
    private DynamoDB dynamoDB;

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {

    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        ApplicationContext context = SpringExtension.getApplicationContext(extensionContext);
        AmazonDynamoDB amazonDynamoDB = (AmazonDynamoDB) context.getBean("amazonDynamoDB");

    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        ApplicationContext context = SpringExtension.getApplicationContext(extensionContext);
        AmazonDynamoDB amazonDynamoDB = (AmazonDynamoDB) context.getBean("amazonDynamoDB");

        this.dynamoDB = new DynamoDB(amazonDynamoDB);

        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("expense_id").withAttributeType("S"));

        List<KeySchemaElement> keySchema = new ArrayList<>();
        keySchema.add(new KeySchemaElement().withAttributeName("expense_id").withKeyType(KeyType.HASH));

        CreateTableRequest request = new CreateTableRequest().withTableName("expensive-things").withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions).withProvisionedThroughput(
                        new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(6L));

        try {
            Table table = dynamoDB.createTable(request);
            table.waitForActive();
            System.out.println("Table created!");
        } catch(Exception e) {
            System.out.println("Error creating the table");
            e.printStackTrace(System.out);
        }

    }

    private void truncateTable(String tableName, String hashKeyName) throws Exception {
        Table table = this.dynamoDB.getTable(tableName);
        ScanSpec spec = new ScanSpec();
        ItemCollection<ScanOutcome> items = table.scan(spec);
        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            String hashKey = item.getString(hashKeyName);
            PrimaryKey key = new PrimaryKey(hashKeyName, hashKey);
            table.deleteItem(key);
            System.out.printf("Deleted item with key: %s\n", hashKey);
        }
    }
}
