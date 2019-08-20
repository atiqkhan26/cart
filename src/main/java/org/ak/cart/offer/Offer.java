package org.ak.cart.offer;

public enum Offer {

    MARKED_PRICE(1, 1),
    BUY_ONE_GET_ONE(2, 1),
    BUY_THREE_FOR_PRICE_OF_TWO(3, 2);

    Offer(int quantity, int billingQuantity) {
       this.quantity = quantity;
       this.billingQuantity = billingQuantity;
    }

    private int quantity;
    private int billingQuantity;

    public int getQuantity() {
        return quantity;
    }

    public int getBillingQuantity() {
        return billingQuantity;
    }

}
