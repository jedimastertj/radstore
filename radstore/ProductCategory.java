package radstore;

import java.util.*;

public class ProductCategory {
    protected final int cid;
    protected final String name;

    protected HashMap<Float, Product> categoryProductsById = new HashMap<>();

    public ProductCategory(int cid, String name) {
        this.cid = cid;
        this.name = name.toLowerCase().trim();
    }

    public String getName() {
        return this.name;
    }

    public int getCid() {
        return this.cid;
    }

    public HashMap<Float, Product> getCategoryProductsById() {
        return this.categoryProductsById;
    }
}
