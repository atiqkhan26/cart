package org.ak.cart.domain;

import java.util.Objects;

public class Item {

    private String name;
    private double price;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Item(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }

        this.name = item.name;
        this.price = item.price;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Item item = (Item) object;
        return Double.compare(item.price, price) == 0 && name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

}
