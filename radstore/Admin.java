package radstore;

import java.util.*;

public class Admin {
    private final String username;
    private String password;
    private int primeMembershipCost = 200;
    private int eliteMembershipCost = 300;

    private HashMap<String, Integer> categoryIdByName = new HashMap<>();
    private HashMap<Integer, ProductCategory> categoriesById = new HashMap<>();
    private HashMap<Float, Product> productsById = new HashMap<>();
    private HashMap<Integer, Deal> dealsById = new HashMap<>();
    private HashMap<String, Customer> customers = new HashMap<>();

    public Admin(String username, String password) {
        this.username = username.toLowerCase().trim();
        this.password = password.replaceAll("\\s", "");
    }

    public int addCategory(int cid, String name) {
        name = name.toLowerCase().trim();
        if (this.categoriesById.containsKey(cid)) {
            return -1; // category id already in use
        } else if (this.categoryIdByName.containsKey(name)) {
            return -2; // category name already in use
        } else {
            ProductCategory category = new ProductCategory(cid, name);
            this.categoryIdByName.put(name, cid);
            this.categoriesById.put(cid, category);
            return 0; // category added successfully
        }
    }

    public int addProduct(int cid, String name, float pid, float price, int quantity, ArrayList<String> specifications) {
        name = name.toLowerCase().trim();
        if (!this.categoriesById.containsKey(cid)) {
            return -1; // invalid category id
        } else if (this.productsById.containsKey(pid)) {
            return -2; // product id already in use
        } else {
            ProductCategory category = this.categoriesById.get(cid);
            Product product = new Product(name, pid, price, cid, quantity, specifications);
            this.productsById.put(pid, product);
            category.categoryProductsById.put(pid, product);
            return 0; // product added successfully
        }
    }

    public int deleteCategory(String name, int cid) {
        name = name.toLowerCase().trim();
        if (!this.categoriesById.containsKey(cid)) {
            return -1; // invalid category id
        } else if (!Integer.valueOf(cid).equals(this.categoryIdByName.get(name))) {
            return -2; // category name and id do not match
        } else {
            ProductCategory category = this.categoriesById.get(cid);
            this.categoriesById.remove(cid);
            this.categoryIdByName.remove(name);
            for (Map.Entry<Float, Product> entry: category.categoryProductsById.entrySet()) {
                float pid = entry.getKey();
                this.productsById.remove(pid);
                this.removeDealsWithGivenProduct(pid);
            }
            this.updateCarts();
            return 0; // category deleted successfully
        }
    }

    public int deleteProduct(String category, float pid) {
        category = category.toLowerCase().trim();
        if (!this.categoryIdByName.containsKey(category)) {
            return -1; // invalid category name
        } else {
            ProductCategory categoryObj = this.categoriesById.get(this.categoryIdByName.get(category));
            if (!categoryObj.categoryProductsById.containsKey(pid)) {
                return -2; // product with given id not present in category
            } else {
                categoryObj.categoryProductsById.remove(pid);
                this.productsById.remove(pid);
                this.removeDealsWithGivenProduct(pid);
                this.updateCarts();
                if (categoryObj.categoryProductsById.size() == 0) {
                    return -3; // category empty after product deleted
                } else {
                    return 0; // product deleted successfully
                }
            }
        }
    }

    public int addDeal(float pid1, float pid2, float normalPrice, float primePrice, float elitePrice) {
        if (!this.productsById.containsKey(pid1)) {
            return -1; // invalid product id 1
        } else if (!this.productsById.containsKey(pid2)) {
            return -2; // invalid product id 2
        } else {
            Product p1 = this.productsById.get(pid1);
            Product p2 = this.productsById.get(pid2);
            if (normalPrice > this.getDiscountedPrice(p1.price, p1.normalDiscount) + this.getDiscountedPrice(p2.price, p2.normalDiscount)) {
                return -3; // combined price needs to be less
            }
            if (primePrice > this.getDiscountedPrice(p1.price, p1.primeDiscount) + this.getDiscountedPrice(p2.price, p2.primeDiscount)) {
                return -3; // combined price needs to be less
            }
            if (elitePrice > this.getDiscountedPrice(p1.price, p1.eliteDiscount) + this.getDiscountedPrice(p2.price, p2.eliteDiscount)) {
                return -3; // combined price needs to be less
            }
            Deal deal = new Deal(pid1, pid2, normalPrice, primePrice, elitePrice);
            dealsById.put(deal.dealId, deal);
            return 0; // dead added successfully
        }
    }

    public int addDiscount(float pid, float eliteDiscount, float primeDiscount, float normalDiscount) {
        if (!this.productsById.containsKey(pid)) {
            return -1; // invalid product id
        } else {
            Product product = this.productsById.get(pid);
            product.eliteDiscount = eliteDiscount;
            product.primeDiscount = primeDiscount;
            product.normalDiscount = normalDiscount;
            return 0; // discount added successfully
        }
    }

    public int addCustomer(String username, String password) {
        username = username.toLowerCase().trim();
        password = password.replaceAll("\\s", "");
        if (this.customers.containsKey(username)) {
            return -1; // there is an existing customer with same username
        } else {
            NormalCustomer customer = new NormalCustomer(username, password, this);
            this.customers.put(username, customer);
            return 0; // customer added successfully
        }
    }

    public Customer loginCustomer(String username, String password) {
        username = username.toLowerCase().trim();
        password = password.replaceAll("\\s", "");
        Customer customer = this.customers.get(username);
        if (customer != null && password.equals(customer.password)) {
            return customer;
        }
        return null;
    }

    protected int upgradeStatus(Customer user, String newStatus) {
        newStatus = newStatus.toLowerCase().trim();
        String currentStatus = user.status;
        if (newStatus.equals(currentStatus)) {
            return -1; // current and new status are same
        } else if (currentStatus.equals("elite")) {
            return -2; // already elite customer
        } else if (newStatus.equals("prime")) {
            if (user.balance >= this.primeMembershipCost) {
                PrimeCustomer upgradedUser = new PrimeCustomer(user.username, user.password, user.admin, (user.balance - this.primeMembershipCost), user.cart);
                this.customers.replace(user.username, upgradedUser);
                return 0; // successfully upgraded
            } else {
                return -4; // insufficient balance
            }
        } else if (newStatus.equals("elite")) {
            if (user.balance >= this.eliteMembershipCost) {
                EliteCustomer upgradedUser = new EliteCustomer(user.username, user.password, user.admin, (user.balance - this.eliteMembershipCost), user.cart);
                this.customers.replace(user.username, upgradedUser);
                return 0; // successfully upgraded
            } else {
                return -4; // insufficient balance
            }
        } else {
          return -3; // invalid new status
        }
    }

    protected int addProductToCart(Customer user, float pid, int quantity) {
        if (!this.productsById.containsKey(pid)) {
            return -1; // invalid product id
        } else {
            Product product = this.productsById.get(pid);
            if (quantity > product.quantity) {
                return -2; // quantity demanded is more than available
            } else {
                user.cart.cartProducts.put(pid, product);
                user.cart.productQuantities.put(pid, quantity);
                return 0;  // product added to cart successfully
            }
        }
    }

    protected int addDealToCart(Customer user, int dealId) {
        if (!this.dealsById.containsKey(dealId)) {
            return -1; // invalid deal id
        } else {
            Deal deal = this.dealsById.get(dealId);
            Product p1 = this.productsById.get(deal.pid1);
            Product p2 = this.productsById.get(deal.pid2);
            if (p1.quantity == 0 || p2.quantity == 0) {
                return -2; // one or both products out of stock
            } else {
                user.cart.cartDeals.put(dealId, deal);
                return 0; // deal added successfully
            }
        }
    }

    private void removeDealsWithGivenProduct(float pid) {
        HashMap<Integer, Deal> dealsByIdCopy = (HashMap<Integer, Deal>) this.dealsById.clone();
        for (Map.Entry<Integer, Deal> set: dealsByIdCopy.entrySet()) {
            Deal deal = set.getValue();
            if (deal.pid1 == pid || deal.pid2 == pid) {
                this.dealsById.remove(deal.dealId);
            }
        }
    }

    private void updateCarts() {
        for (Map.Entry<String, Customer> set: this.customers.entrySet()) {
            Customer customer = set.getValue();
            HashMap<Float, Product> cartProductsCopy = (HashMap<Float, Product>) customer.cart.cartProducts.clone();
            for (Map.Entry<Float, Product> set1: cartProductsCopy.entrySet()) {
                Product product = set1.getValue();
                if (!this.checkProductPresence(product.pid)) {
                    System.out.println("For user - " + customer.username);
                    System.out.println("Product - " + product.name + " with product id - " + product.pid);
                    System.out.println("Removing product from cart as it has been removed by admin \n");
                    customer.cart.removeProduct(product.pid);
                }
            }
            HashMap<Integer, Deal> cartDealsCopy = (HashMap<Integer, Deal>) customer.cart.cartDeals.clone();
            for (Map.Entry<Integer, Deal> set2: cartDealsCopy.entrySet()) {
                Deal deal = set2.getValue();
                if (!this.checkDealPresence(deal.dealId)) {
                    System.out.println("For user - " + customer.username);
                    System.out.println("Deal id - " + deal.dealId);
                    System.out.println("Removing deal from cart as it has been removed by admin \n");
                    customer.cart.removeDeal(deal.dealId);
                }
            }
        }
    }

    public void addAmount(Customer user, float amount) {
        user.balance += amount;
    }

    public int getCategoryId(String category) {
        category = category.toLowerCase().trim();
        return this.categoryIdByName.get(category);
    }

    private float getDiscountedPrice(float price, float percentDiscount) {
        return ((1-(percentDiscount/100)) * price);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public HashMap<Integer, ProductCategory> getCategoriesById() {
        return this.categoriesById;
    }

    public HashMap<Integer, Deal> getDealsById() {
        return this.dealsById;
    }

    public Product getProduct(float pid) {
        return this.productsById.get(pid);
    }

    private boolean checkProductPresence(float pid) {
        return this.productsById.containsKey(pid);
    }

    private boolean checkDealPresence(int dealId) {
        return this.dealsById.containsKey(dealId);
    }

}
