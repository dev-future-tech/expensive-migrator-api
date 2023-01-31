package com.example.expensivemigratorapi;

public class PersistDataRequest {

    private String tableName;

    private String uniqueId;

    private String uniqueIdClass;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUniqueIdClass() {
        return uniqueIdClass;
    }

    public void setUniqueIdClass(String uniqueIdClass) {
        this.uniqueIdClass = uniqueIdClass;
    }
}
