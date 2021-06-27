package com.example.book.client;

public class StoreDTO {
    private String name;
    private StoreType type;

    public StoreDTO(String name, StoreType type) {
        this.name = name;
        this.type = type;
    }

    public StoreDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StoreType getType() {
        return type;
    }

    public void setType(StoreType type) {
        this.type = type;
    }
}
