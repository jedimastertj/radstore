package radstore;

import java.util.*;

public class Cart {
    protected HashMap<Float, Product> cartProducts = new HashMap<>();
    protected HashMap<Float, Integer> productQuantities = new HashMap<>();
    protected HashMap<Integer, Deal> cartDeals = new HashMap<>();

    protected void removeProduct(float pid) {
        this.cartProducts.remove(pid);
        this.productQuantities.remove(pid);
    }

    protected void removeDeal(int dealId) {
        this.cartDeals.remove(dealId);
    }

    public HashMap<Float, Product> getCartProducts() {
        return this.cartProducts;
    }

    public HashMap<Float, Integer> getProductQuantities() {
        return this.productQuantities;
    }

    public HashMap<Integer, Deal> getCartDeals() {
        return this.cartDeals;
    }
}
