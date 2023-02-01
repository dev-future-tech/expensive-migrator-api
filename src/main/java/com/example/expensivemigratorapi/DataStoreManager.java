package com.example.expensivemigratorapi;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;

@Component
public class DataStoreManager {

    private final Logger log = LoggerFactory.getLogger(DataStoreManager.class);

    private final AmazonDynamoDB amazonDynamoDB;

    @Autowired
    public DataStoreManager(AmazonDynamoDB _amazonDynamoDB) {
        this.amazonDynamoDB = _amazonDynamoDB;
    }

    public void addExpense(String tableName, String name, String description) throws InsertDataException {
        HashMap<String, AttributeValue> item_values =
                new HashMap<>();
        item_values.put("expense_id", new AttributeValue(UUID.randomUUID().toString()));
        item_values.put("expense_name", new AttributeValue(name));
        item_values.put("expense_description", new AttributeValue(description));

        try {
            this.amazonDynamoDB.putItem(tableName, item_values);
        } catch (Exception e) {
            log.error("Error inserting into the expensive-things table", e);
            throw new InsertDataException("Error inserting into the expensive-things table", e);
        }
    }



}
