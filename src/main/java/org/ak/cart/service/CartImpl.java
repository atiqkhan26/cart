package org.ak.cart.service;

import org.ak.cart.ItemException;
import org.ak.cart.domain.Inventory;
import org.ak.cart.domain.Item;
import org.ak.cart.offer.BaseOffer;
import org.ak.cart.offer.Offer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.ak.cart.offer.Offer.MARKED_PRICE;

public class CartImpl implements Cart {

    private static Logger LOGGER = LogManager.getLogger(CartImpl.class);

    private List<Item> items = Collections.synchronizedList(new ArrayList<>());

    private Inventory inventory;
    private Map<String, Offer> offers;
    private BaseOffer baseOffer;

    public CartImpl(Inventory inventory, Map<String, Offer> offers, BaseOffer baseOffer) {
        this.inventory = inventory;
        this.offers = offers;
        this.baseOffer = baseOffer;
    }

    public void empty() {
        LOGGER.info("Clearing items with size {}", items.size());
        this.items.clear();
    }

    public List<Item> getItems() {
        return items;
    }

    public Cart add(String name) {
        Double price = inventory.getListingItems().get(name);
        if (price == null) {
            LOGGER.warn("Item {} not found in inventory list", name);
        } else {
            items.add(new Item(name, price));
        }
        return this;
    }

    public Cart add(List<String> names) {
        if (names != null) {
            names.forEach((name) -> {
                add(name);
            });
            LOGGER.info("{} Items added to the cart, new total is {}", names.size(), items.size());
        }
        return this;
    }

    public BigDecimal calculateFinalPrice() throws ItemException {
        BigDecimal finalPrice = BigDecimal.ZERO;
        Map<String, List<Item>> itemsByName = items.stream().collect(Collectors.groupingByConcurrent(Item::getName));
        for (Map.Entry<String, List<Item>> entry : itemsByName.entrySet()) {
            String item = entry.getKey();
            Offer offer = offers.getOrDefault(item, MARKED_PRICE);
            finalPrice = finalPrice.add(baseOffer.filterPrice(offer, entry.getValue()));
        }

        LOGGER.info("Final bill of {} items is {}", items.size(), finalPrice);
        return finalPrice;
    }

}
