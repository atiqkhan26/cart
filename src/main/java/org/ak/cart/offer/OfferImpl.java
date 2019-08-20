package org.ak.cart.offer;

import org.ak.cart.ItemException;
import org.ak.cart.domain.Item;

import java.math.BigDecimal;
import java.util.List;

public class OfferImpl extends BaseOffer {

    @Override
    public BigDecimal filterPrice(Offer offer, List<Item> items) throws ItemException {
        super.filterItemsBeforeCalculatePrice(items);

        int size = items.size();
        if (size == 0) {
            return BigDecimal.ZERO;
        }

        LOGGER.info("{} to be calculated using {} subject to terms & conditions", items.get(0).getName(), offer.name());

        BigDecimal numberOfGroup = BigDecimal.valueOf(size / offer.getQuantity());
        BigDecimal numberRemain = BigDecimal.valueOf(size % offer.getQuantity());
        BigDecimal price = BigDecimal.valueOf(items.get(0).getPrice());

        BigDecimal result = numberOfGroup.multiply(price).multiply(BigDecimal.valueOf(offer.getBillingQuantity()))
                .add(numberRemain.multiply(price));
        return result;
    }

}
