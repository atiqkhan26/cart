package org.ak.cart;

import org.ak.cart.domain.Inventory;
import org.ak.cart.domain.Item;
import org.ak.cart.offer.BaseOffer;
import org.ak.cart.offer.Offer;
import org.ak.cart.offer.OfferImpl;
import org.ak.cart.service.Cart;
import org.ak.cart.service.CartImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartImplTest {

    private Inventory inventory;
    private Cart cart;
    private BaseOffer baseOffer;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory(getPriceList());
        baseOffer = new OfferImpl();
    }

    @DisplayName("No items")
    @Test
    public void noItems() throws ItemException {
        cart = new CartImpl(inventory, getPromotions(), baseOffer);
        BigDecimal totalBill = cart.calculateFinalPrice();
        assertEquals(0.0, totalBill.doubleValue());
    }

    @DisplayName("Items not available in inventory")
    @Test
    public void invalidItems() throws ItemException {
        cart = new CartImpl(inventory, getPromotions(), baseOffer);
        cart.add("Orange");

        BigDecimal totalBill = cart.calculateFinalPrice();
        assertEquals(0.0, totalBill.doubleValue());
    }

    @DisplayName("One of each fruit")
    @Test
    public void givenCartApplied() throws ItemException {
        cart = new CartImpl(inventory, getPromotions(), baseOffer);
        List<String> items = Arrays.asList("Apple","Banana","Melon","Lime");
        cart.add(items);

        BigDecimal totalBill = cart.calculateFinalPrice();
        assertEquals(1.20, totalBill.doubleValue());

        cart.empty();
        totalBill = cart.calculateFinalPrice();
        assertEquals(0.0, totalBill.doubleValue());
    }

    @DisplayName("2 x Apple, 1 x Banana")
    @Test
    public void givenCartNoOfferApplied() throws ItemException {
        cart = new CartImpl(inventory, getPromotions(), baseOffer);
        cart.add("Apple")
                .add("Banana")
                .add("Apple");

        BigDecimal totalBill = cart.calculateFinalPrice();
        assertEquals(0.90, totalBill.doubleValue());
    }

    @DisplayName("2 x Apple, 1 x Banana, 4 x Lime")
    @Test
    public void givenCartOfferApplied() throws ItemException {
        List items = new ArrayList<>();
        addToList(items, "Apple", 2);
        addToList(items, "Banana", 1);
        addToList(items, "Lime", 4);

        cart = new CartImpl(inventory, getPromotions(), baseOffer);
        cart.add(items);

        BigDecimal totalBill = cart.calculateFinalPrice();
        assertEquals(1.35, totalBill.doubleValue());
    }

    @DisplayName("2 x Apple, 1 x Banana, 4 x Lime, 5 x Melon")
    @Test
    public void givenCartMoreOffersApplied() throws ItemException {
        List items = new ArrayList<>();
        addToList(items, "Apple", 2);
        addToList(items, "Banana", 1);
        addToList(items, "Lime", 4);
        addToList(items, "Melon", 5);

        cart = new CartImpl(inventory, getPromotions(), baseOffer);
        cart.add(items);

        BigDecimal totalBill = cart.calculateFinalPrice();
        assertEquals(2.85, totalBill.doubleValue());
    }

    @DisplayName("6 x Apple, 3 x Banana, 12 x Lime, 15 x Melon")
    @Test
    public void givenCartFurtherMoreOffersApplied() throws ItemException {
        List items = new ArrayList<>();
        addToList(items, "Apple", 6);
        addToList(items, "Banana", 3);
        addToList(items, "Lime", 12);
        addToList(items, "Melon", 15);

        cart = new CartImpl(inventory, getPromotions(), baseOffer);
        cart.add(items);

        BigDecimal totalBill = cart.calculateFinalPrice();
        assertEquals(7.90, totalBill.doubleValue());
    }

    @DisplayName("Repeating tests")
    @ParameterizedTest(name = "{index} => a={0}, b={1}, c={2}, d={3}, e={4}, result={5}")
    @CsvSource({"Apple, Banana, Melon, Lime, Lime, 1.35",
            "Melon, Melon, Melon, Melon, Melon, 1.50",
            "Lime, Lime, Lime, Lime, Lime, 0.60",
            "Apple, Lime, Lime, Lime, Melon, 1.15",
            "Lime, Lime, Lime, Melon, Melon, 0.80",
            "Apple, Banana, Lime, Melon, Melon, 1.20"
    })
    public void parameterized(String a, String b, String c, String d, String e, double result) throws ItemException {
        cart = new CartImpl(inventory, getPromotions(), baseOffer);
        cart.add(a)
                .add(b)
                .add(c)
                .add(d)
                .add(e);

        BigDecimal totalBill = cart.calculateFinalPrice();
        assertEquals(result, totalBill.doubleValue());
    }

    private void addToList(List<String> list, String item, int count) {
        for (int i = 0; i < count; i++) {
            list.add(item);
        }
    }

    private Map<String, Offer> getPromotions() {
        Map<String, Offer> offers = new HashMap<>();
        offers.put("Melon", Offer.BUY_ONE_GET_ONE);
        offers.put("Lime", Offer.BUY_THREE_FOR_PRICE_OF_TWO);
        return offers;
    }

    private List<Item> getPriceList() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Apple", 0.35));
        items.add(new Item("Banana", 0.20));
        items.add(new Item("Melon", 0.50));
        items.add(new Item("Lime", 0.15));
        return items;
    }

}
