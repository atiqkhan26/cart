package org.ak.cart.offer;

import org.ak.cart.ItemException;
import org.ak.cart.domain.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseOffer {

    protected static Logger LOGGER = LogManager.getLogger(BaseOffer.class);

    public abstract BigDecimal filterPrice(Offer offer, List<Item> items) throws ItemException;

    public void filterItemsBeforeCalculatePrice(List<Item> items) throws ItemException {
        Set<String> itemNames = items.stream()
                .map(Item::getName)
                .collect(Collectors.toCollection(HashSet::new));

        if (itemNames.size() > 1) {
            LOGGER.warn("Unexpected item in the list, they all should be of same type");
            throw new ItemException("Items are expected to be same within this list");
        }
        LOGGER.info("{} items of {} found", items.size(), items.get(0).getName());
    }

}
