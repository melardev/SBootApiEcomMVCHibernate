package com.melardev.spring.shoppingcartweb.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {

    private Map<Long, CartItem> cartItems;
    private List<OnCartChangedListener> listeners;

    Logger logger = LoggerFactory.getLogger(Cart.class);

    public Cart() {
        listeners = new ArrayList<>();
        cartItems = new HashMap<>();
    }

    /**
     * Adds or updates a cart item
     *
     * @param product  product to be added to the cart
     * @param quantity quantity of product to be added to the cart
     * @param addOnly  if true, the quantity will be an absolute value. if false then will be treated as relative, so,
     *                 if the product is in cart and addOnly is false, the quantity will be the sum(old quantity + quantity);
     *                 whereas if product is present and addOnly is true, then the new quantity is always the quantity
     *                 provided in the argument no matter what. In short words, it determines either create or update
     *                 operation
     */
    public void addProduct(Product product, int quantity, boolean addOnly) {
        logger.debug("addProduct(%d, %d, %b)", product.getSlug(), quantity, addOnly);
        CartItem cartItem = cartItems.get(product.getId());
        if (cartItem != null && !addOnly)
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        else {
            cartItems.put(product.getId(), new CartItem(product, quantity));
        }

        notifyListeners();
    }


    public double getTotalAmount() {
        double sum = cartItems.values().stream().mapToDouble(CartItem::getTotalAmount).sum();
        //double sum = cartItems.values().stream().map(CartItem::getTotalAmount).collect(Collectors.summingDouble(item -> item));
        return sum;
    }

    public boolean isCartEmpty() {
        return cartItems.size() == 0;
    }

    public Map<Long, CartItem> getCartItems() {
        return cartItems;
    }

    public ArrayList<CartItem> getCartItemsAsList() {
        return new ArrayList<>(cartItems.values());
    }

    public int getSizeDistinct() {
        return cartItems.size();
    }

    public int getSizeIncludingDuplicates() {
        return cartItems.values().stream().mapToInt(CartItem::getQuantity).sum();
        //return cartItems.values().stream().mapToInt(item -> item.getStock()).sum();
    }

    public void updateCartItem(Product product, int quantity) {
        addProduct(product, quantity, true);
    }

    public void clear() {
        this.cartItems.clear();
        notifyListeners();
    }

    /**
     * Notify the listeners so they know how to handle that,
     * if the listeners are session based they have to update the cart on session
     * if they are entity based then they have to persist the cart
     */
    private void notifyListeners() {
        listeners.forEach(OnCartChangedListener::onCartStateChanged);
    }


    public void addCartStateListener(OnCartChangedListener listener) {
        this.listeners.add(listener);
    }

    public boolean isProductInCart(Product product) {
        return isProductInCart(product.id);
    }

    public boolean isProductInCart(Long id) {
        return cartItems.containsKey(id);
    }

    public CartItem getCartItem(Long id) {
        return cartItems.get(id);
    }

    public interface OnCartChangedListener {
        public void onCartStateChanged();
    }
}
