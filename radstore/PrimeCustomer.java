package radstore;

import java.util.*;

public class PrimeCustomer extends Customer {

    public PrimeCustomer(String username, String password, Admin admin, float balance, Cart cart) {
        super(username, password, admin);
        this.status = "prime";
        this.userTypeDiscount = 5;
        this.deliveryTime = "3-6 days";
        this.balance = balance;
        this.cart = cart;
    }

    public int checkout() {
        float total = 0;
        float totalWithDiscount = 0;
        float bestCoupon = (this.coupons.size() > 0)? Collections.max(this.coupons) : 0;
        boolean usedCoupon = false;

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
            int discountType = bestDiscount(product, bestCoupon);
            float priceWithDiscount = 0;
            if (discountType == 0) {
                priceWithDiscount = getDiscountedPrice(price, this.userTypeDiscount);
                System.out.println("Price after discount (user type) = " + priceWithDiscount + "\n");
            } else if (discountType == 1) {
                priceWithDiscount = getDiscountedPrice(price, product.primeDiscount);
                System.out.println("Price after discount (product specific) = " + priceWithDiscount + "\n");
            } else if (discountType == 2) {
                usedCoupon = true;
                priceWithDiscount = getDiscountedPrice(price, bestCoupon);
                System.out.println("Price after discount (coupon) = " + priceWithDiscount + "\n");
            }
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
            System.out.println("Combo deal price = " + deal.primePrice + "\n");
            totalWithDiscount += deal.primePrice;
        }

        if (cartProducts.size() == 0 && cartDeals.size() == 0) {
            return -3; // cart empty
        }

        System.out.println("Total without discount = " + total);
        float delivery = (float) (100 + (0.02 * total));
        System.out.println("Delivery charges = 100 + 2% of " + total + " = " + delivery);
        System.out.println("Total with discount = " + totalWithDiscount);
        float toBePaid = totalWithDiscount + delivery;
        System.out.println("Total to be paid = " + totalWithDiscount + " + " + delivery + " = " + toBePaid + "\n");

        if (toBePaid <= this.balance) {
            System.out.println("Order placed. It will be delivered in " + this.deliveryTime + "\n");
            if (toBePaid >= 5000) {
                this.generateCoupons();
            }
            this.balance -= toBePaid;
            if (usedCoupon) {
                this.coupons.remove(bestCoupon);
            }
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

    private int bestDiscount(Product product, float bestCoupon) {
        ArrayList<Float> discounts = new ArrayList<>();
        discounts.add(this.userTypeDiscount); discounts.add(product.primeDiscount); discounts.add(bestCoupon);
        if (this.userTypeDiscount == Collections.max(discounts)) {
            return 0; // user type discount
        } else if (product.primeDiscount == Collections.max(discounts)) {
            return 1; // product specific discount
        } else {
            return 2; // coupon
        }
    }

    private float getDiscountedPrice(float price, float percentDiscount) {
        return (1-(percentDiscount/100)) * price;
    }

    private void generateCoupons() {
        Random random = new Random();
        int howMany = 1 + random.nextInt(2);
        for (int i = 0; i < howMany; i++) {
            float coupon = (float)(5 + random.nextInt(11));
            System.out.println("You have won a coupon of " + coupon + "% discount");
            this.coupons.add(coupon);
        }
        System.out.print("\n");
    }
}
