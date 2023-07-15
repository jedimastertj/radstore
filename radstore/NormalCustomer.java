package radstore;

import java.util.*;

public class NormalCustomer extends Customer {

    public NormalCustomer(String username, String password, Admin admin) {
        super(username, password, admin);
        this.status = "normal";
        this.userTypeDiscount = 0;
        this.deliveryTime = "7-10 days";
    }

    public int checkout() {
        float total = 0;
        float totalWithDiscount = 0;

        HashMap<Float, Product> cartProducts = this.cart.getCartProducts();
        HashMap<Float, Integer> productQuantities = this.cart.getProductQuantities();
        HashMap<Integer, Deal> cartDeals = this.cart.getCartDeals();

        for (Map.Entry<Float, Product> set: cartProducts.entrySet()) {
            Product product = set.getValue();
            int quantity = productQuantities.get(product.pid);
            System.out.println("Product id = " + product.pid);
            System.out.println("Product name = " + product.name);
            System.out.println("Product quantity demanded = " + quantity);
            if (quantity > product.quantity) {
                return -1; // quantity demanded more than available
            }
            float price = quantity * product.price;
            total += price;
            System.out.println("Price without discount = " + price);
            float priceWithDiscount = getDiscountedPrice(price, product.normalDiscount);
            System.out.println("Price after discount (product specific) = " + priceWithDiscount + "\n");
            totalWithDiscount += priceWithDiscount;
        }

        for (Map.Entry<Integer, Deal> set: cartDeals.entrySet()) {
            Deal deal = set.getValue();
            Product p1 = admin.getProduct(deal.pid1);
            Product p2 = admin.getProduct(deal.pid2);
            System.out.println("Deal id = " + set.getKey());
            System.out.println("Product id 1 = " + p1.pid);
            System.out.println("Product name 1 = " + p1.name);
            System.out.println("Product id 2 = " + p2.pid);
            System.out.println("Product name 2 = " + p2.name);
            int q1 = (cartProducts.containsKey(p1.pid)) ? (1 + productQuantities.get(p1.pid)) : 1;
            int q2 = (cartProducts.containsKey(p2.pid)) ? (1 + productQuantities.get(p2.pid)) : 1;
            if (q1 > p1.quantity || q2 > p2.quantity) {
                return -1; // quantity demanded more than available
            }
            total += (p1.price + p2.price);
            System.out.println("Price without deal = " + (p1.price + p2.price));
            System.out.println("Combo deal price = " + deal.normalPrice + "\n");
            totalWithDiscount += deal.normalPrice;
        }

        if (cartProducts.size() == 0 && cartDeals.size() == 0) {
            return -3; // cart empty
        }

        System.out.println("Total without discount = " + total);
        float delivery = (float) (100 + (0.05 * total));
        System.out.println("Delivery charges = 100 + 5% of " + total + " = " + delivery);
        System.out.println("Total with discount = " + totalWithDiscount);
        float toBePaid = totalWithDiscount + delivery;
        System.out.println("Total to be paid = " + totalWithDiscount + " + " + delivery + " = " + toBePaid + "\n");

        if (toBePaid <= this.balance) {
            System.out.println("Order placed. It will be delivered in " + this.deliveryTime + "\n");
            this.balance -= toBePaid;
            for (Map.Entry<Float, Product> set: cartProducts.entrySet()) {
                Product product = set.getValue();
                int quantity = productQuantities.get(product.pid);
                product.quantity -= quantity;
            }
            for (Map.Entry<Integer, Deal> set: cartDeals.entrySet()) {
                Deal deal = set.getValue();
                Product p1 = admin.getProduct(deal.pid1);
                Product p2 = admin.getProduct(deal.pid2);
                p1.quantity -= 1;
                p2.quantity -= 1;
            }
            this.emptyCart();
            return 0; // checkout successful

        } else {
            return -2; // Insufficient balance
        }
    }

    private float getDiscountedPrice(float price, float percentDiscount) {
        return (1-(percentDiscount/100)) * price;
    }
}
