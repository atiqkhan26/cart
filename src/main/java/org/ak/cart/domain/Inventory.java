package org.ak.cart.domain;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.IntStream;

public class Inventory {

    private ConcurrentMap<String, Double> listingItems = new ConcurrentHashMap<>();

    public Inventory(List<Item> items) {
        if (items != null) {
            IntStream.range(0, items.size()).forEach(i -> {
                Item item = items.get(i);
                listingItems.put(item.getName(), item.getPrice());
            });
        }
    }

    public void addItem(Item item) {
        if (item != null) {
            listingItems.put(item.getName(), item.getPrice());
        }
    }

    public void removeItem(String item) {
        if (item != null) {
            listingItems.remove(item);
        }
    }

    public ConcurrentMap<String, Double> getListingItems() {
        return listingItems;
    }

}
