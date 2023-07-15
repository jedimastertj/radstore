package radstore;

import java.util.*;

abstract public class Customer {
    protected final String username;
    protected String password;
    protected final Admin admin;
    protected float balance = 1000;
    protected String status;
    protected float userTypeDiscount;
    protected String deliveryTime;

    protected Cart cart = new Cart();
    protected ArrayList<Float> coupons = new ArrayList<>();

    protected Customer(String username, String password, Admin admin) {
        this.username = username.toLowerCase().trim();
        this.password = password.replaceAll("\\s", "");
        this.admin = admin;
    }

    public int requestStatusUpgrade(String newStatus) {
        return this.admin.upgradeStatus(this, newStatus);
    }

    public int addProductToCart(float pid, int quantity) {
        return this.admin.addProductToCart(this, pid, quantity);
    }

    public void emptyCart() {
        this.cart.cartProducts.clear();
        this.cart.productQuantities.clear();
        this.cart.cartDeals.clear();
    }

    public int addDealToCart(int dealId) {
        return this.admin.addDealToCart(this, dealId);
    }

    abstract public int checkout();

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getStatus() {
        return this.status;
    }

    public float getBalance() {
        return this.balance;
    }

    public ArrayList<Float> getCoupons() {
        return this.coupons;
    }

    public Cart getCart() {
        return this.cart;
    }
}
