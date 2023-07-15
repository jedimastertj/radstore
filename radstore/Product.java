package radstore;

import java.util.*;

public class Product {
    protected String name;
    protected final float pid;
    protected float price;
    protected final int cid;
    protected int quantity;
    protected float eliteDiscount = 0;
    protected float primeDiscount = 0;
    protected float normalDiscount = 0;

    protected ArrayList<String> specifications;

    public Product(String name, float pid, float price, int cid, int quantity, ArrayList<String> specifications) {
        this.name = name.toLowerCase().trim();
        this.pid = pid;
        this.price = price;
        this.cid = cid;
        this.quantity = quantity;
        this.specifications = specifications;
    }

    public String getName() {
        return this.name;
    }

    public int getCid() {
        return this.cid;
    }

    public float getPid() {
        return this.pid;
    }

    public float getPrice() {
        return this.price;
    }

    public ArrayList<String> getSpecifications() {
        return this.specifications;
    }
}
