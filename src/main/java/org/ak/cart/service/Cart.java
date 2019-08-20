package org.ak.cart.service;

import org.ak.cart.ItemException;
import org.ak.cart.domain.Item;

import java.math.BigDecimal;
import java.util.List;

public interface Cart {

    List<Item> getItems();

    void empty();

    Cart add(String itemName);

    Cart add(List<String> itemNames);

    BigDecimal calculateFinalPrice() throws ItemException;

}
