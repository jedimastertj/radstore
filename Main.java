import radstore.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice, output;
        final Admin admin = new Admin("Tanishq", "2021294");
        Customer user;

        while (true) {
            System.out.println("Welcome to Radstore");
            System.out.println("1. Enter as admin");
            System.out.println("2. Explore product catalog");
            System.out.println("3. Show available deals");
            System.out.println("4. Enter as customer");
            System.out.println("5. Exit the application");

            System.out.print("\nEnter choice: ");
            choice = sc.nextInt(); sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter username: ");
                String username = sc.nextLine().toLowerCase().trim();
                System.out.print("Enter password: ");
                String password = sc.nextLine().replaceAll("\\s", "");

                if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
                    System.out.println("Welcome, " + admin.getUsername());

                    while (true) {
                        System.out.println("Please choose any one of the following actions: ");
                        System.out.println("1. Add category");
                        System.out.println("2. Delete category");
                        System.out.println("3. Add product");
                        System.out.println("4. Delete product");
                        System.out.println("5. Set discount on product");
                        System.out.println("6. Add giveaway deal");
                        System.out.println("7. Back");

                        System.out.print("\nEnter choice: ");
                        choice = sc.nextInt(); sc.nextLine();

                        if (choice == 1) {
                            System.out.print("Enter category id: ");
                            int cid = sc.nextInt(); sc.nextLine();
                            System.out.print("Enter category name: ");
                            String name = sc.nextLine().toLowerCase().trim();

                            output = admin.addCategory(cid, name);
                            if (output == 0) {
                                System.out.println("Category added successfully \n");
                                addProduct(sc, admin, cid);
                            } else if (output == -1) {
                                System.out.println("Category id already in use \n");
                            } else if (output == -2) {
                                System.out.println("Category name already in use \n");
                            }

                        } else if (choice == 2) {
                            System.out.print("Enter category name: ");
                            String name = sc.nextLine().toLowerCase().trim();
                            System.out.print("Enter category id: ");
                            int cid = sc.nextInt(); sc.nextLine();

                            output = admin.deleteCategory(name, cid);
                            if (output == 0) {
                                System.out.println("Category deleted successfully \n");
                            } else if (output == -1) {
                                System.out.println("Incorrect category id \n");
                            } else if (output == -2) {
                                System.out.println("Category id and name do not match \n");
                            }

                        } else if (choice == 3) {
                            System.out.print("Enter category id: ");
                            int cid = sc.nextInt(); sc.nextLine();
                            addProduct(sc, admin, cid);

                        } else if (choice == 4) {
                            System.out.print("Enter category name: ");
                            String category = sc.nextLine().toLowerCase().trim();
                            System.out.print("Enter product id: ");
                            float pid = sc.nextFloat(); sc.nextLine();

                            output = admin.deleteProduct(category, pid);
                            if (output == 0) {
                                System.out.println("Product deleted successfully \n");
                            } else if (output == -1) {
                                System.out.println("Invalid category name \n");
                            } else if (output == -2) {
                                System.out.println("Product with given id not present in category \n");
                            } else if (output == -3) {
                                System.out.println("Category is empty after deletion of product \n");
                                int cid = admin.getCategoryId(category);

                                while (true) {
                                    System.out.println("Choose one of the following actions: ");
                                    System.out.println("1. Add a product");
                                    System.out.println("2. Delete the category");

                                    System.out.print("\nEnter choice: ");
                                    choice = sc.nextInt(); sc.nextLine();
                                    if (choice == 1) {
                                        addProduct(sc, admin, cid);
                                        break;
                                    } else if (choice == 2) {
                                        admin.deleteCategory(category, cid);
                                        System.out.println("Category deleted successfully \n");
                                        break;
                                    } else {
                                        System.out.println("Invalid input! \n");
                                    }
                                }
                            }

                        } else if (choice == 5) {
                            System.out.print("Enter product id: ");
                            float pid = sc.nextFloat(); sc.nextLine();
                            System.out.println("Enter space separated % discounts for elite, prime and normal customers: ");
                            float eliteDiscount = sc.nextFloat();
                            float primeDiscount = sc.nextFloat();
                            float normalDiscount = sc.nextFloat(); sc.nextLine();

                            output = admin.addDiscount(pid, eliteDiscount, primeDiscount, normalDiscount);
                            if (output == 0) {
                                System.out.println("Discount added successfully \n");
                            } else if (output == -1) {
                                System.out.println("Invalid product id \n");
                            }

                        } else if (choice == 6) {
                            System.out.print("Enter first product id: ");
                            float pid1 = sc.nextFloat(); sc.nextLine();
                            System.out.print("Enter second product id: ");
                            float pid2 = sc.nextFloat(); sc.nextLine();
                            System.out.println("Enter space separated combined price for Normal, Prime and Elite: ");
                            float normalPrice = sc.nextFloat();
                            float primePrice = sc.nextFloat();
                            float elitePrice = sc.nextFloat(); sc.nextLine();

                            output = admin.addDeal(pid1, pid2, normalPrice, primePrice, elitePrice);
                            if (output == 0) {
                                System.out.println("Deal added successfully \n");
                            } else if (output == -1) {
                                System.out.println("Invalid first product id \n");
                            } else if (output == -2) {
                                System.out.println("Invalid second product id \n");
                            } else if (output == -3) {
                                System.out.println("Combined price needs to be less than original total \n");
                            }

                        } else if (choice == 7) {
                            break;

                        } else {
                            System.out.println("Invalid input! \n");
                        }
                    }

                } else {
                    System.out.println("Invalid username/password \n");
                }

            } else if (choice == 2) {
                showProducts(admin);

            } else if (choice == 3) {
                showDeals(admin);

            } else if (choice == 4) {
                System.out.println("Choose one of the following actions: ");
                System.out.println("1. Sign up");
                System.out.println("2. Log in");
                System.out.println("3. Back");

                System.out.print("\nEnter choice: ");
                choice = sc.nextInt(); sc.nextLine();
                if (choice == 1) {
                    System.out.print("Enter username: ");
                    String username = sc.nextLine().toLowerCase().trim();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine().replaceAll("\\s", "");

                    output = admin.addCustomer(username, password);
                    if (output == 0) {
                        System.out.println("Customer added successfully \n");
                    } else if (output == -1) {
                        System.out.println("There is an existing customer with same username \n");
                    }

                } else if (choice == 2) {
                    System.out.print("Enter username: ");
                    String username = sc.nextLine().toLowerCase().trim();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine().replaceAll("\\s", "");
                    user = admin.loginCustomer(username, password);

                    if (user == null) {
                        System.out.println("Invalid username/password \n");
                    } else {
                        System.out.println("Welcome, " + user.getUsername());

                        while (true) {
                            System.out.println("Choose one of the following actions: ");
                            System.out.println("1. Browse products");
                            System.out.println("2. Browse deals");
                            System.out.println("3. Add a product to cart");
                            System.out.println("4. Add products in deal to cart");
                            System.out.println("5. View coupons");
                            System.out.println("6. Check account balance");
                            System.out.println("7. View cart");
                            System.out.println("8. Empty cart");
                            System.out.println("9. Checkout cart");
                            System.out.println("10. Upgrade customer status");
                            System.out.println("11. Add amount to wallet");
                            System.out.println("12. Back");

                            System.out.print("\nEnter choice: ");
                            choice = sc.nextInt(); sc.nextLine();
                            if (choice == 1) {
                                showProducts(admin);

                            } else if (choice == 2) {
                                showDeals(admin);

                            } else if (choice == 3) {
                                System.out.print("Enter product id: ");
                                float pid = sc.nextFloat(); sc.nextLine();
                                System.out.print("Enter quantity: ");
                                int quantity = sc.nextInt(); sc.nextLine();

                                output = user.addProductToCart(pid, quantity);
                                if (output == 0) {
                                    System.out.println("Product added to cart successfully \n");
                                } else if (output == -1) {
                                    System.out.println("Invalid product id \n");
                                } else if (output == -2) {
                                    System.out.println("Quantity demanded is more than available \n");
                                }

                            } else if (choice == 4) {
                                System.out.print("Enter deal id: ");
                                int dealId = sc.nextInt(); sc.nextLine();

                                output = user.addDealToCart(dealId);
                                if (output == 0) {
                                    System.out.println("Deal added successfully \n");
                                } else if (output == -1) {
                                    System.out.println("Invalid deal id \n");
                                } else if (output == -2) {
                                    System.out.println("One or both products in deal out of stock \n");
                                }

                            } else if (choice == 5) {
                                if (user instanceof NormalCustomer) {
                                    System.out.println("No coupons for normal customer \n");
                                } else {
                                    ArrayList<Float> coupons = user.getCoupons();
                                    coupons.sort(Comparator.naturalOrder());
                                    System.out.println("Following coupons are available: ");
                                    for (Float coupon: coupons) {
                                        System.out.print(coupon + "% ");
                                    }
                                    System.out.println("\n");
                                }

                            } else if (choice == 6) {
                                System.out.println("Current account balance = " + user.getBalance() + "\n");

                            } else if (choice == 7) {
                                Cart cart = user.getCart();
                                HashMap<Float, Product> cartProducts = cart.getCartProducts();
                                HashMap<Float, Integer> productQuantities = cart.getProductQuantities();
                                HashMap<Integer, Deal> cartDeals = cart.getCartDeals();
                                if (cartProducts.size() == 0) {
                                    System.out.println("There are no products in the cart \n");
                                } else {
                                    System.out.println("There are following products in cart: \n");
                                    for (Map.Entry<Float, Product> set: cartProducts.entrySet()) {
                                        System.out.println("Product id = " + set.getKey());
                                        System.out.println("Product name = " + set.getValue().getName());
                                        System.out.println("Category id = " + set.getValue().getCid());
                                        System.out.println("Quantity added = " + productQuantities.get(set.getKey()) + "\n");
                                    }
                                }

                                if (cartDeals.size() == 0) {
                                    System.out.println("There are no deals in the cart \n");
                                } else {
                                    System.out.println("There are following deals in cart: \n");
                                    for (Map.Entry<Integer, Deal> set: cartDeals.entrySet()) {
                                        Deal deal = set.getValue();
                                        Product p1 = admin.getProduct(deal.getPid1());
                                        Product p2 = admin.getProduct(deal.getPid2());
                                        System.out.println("Deal id = " + set.getKey());
                                        System.out.println("Product id 1 = " + p1.getPid());
                                        System.out.println("Product name 1 = " + p1.getName());
                                        System.out.println("Product id 2 = " + p2.getPid());
                                        System.out.println("Product name 2 = " + p2.getName() + "\n");
                                    }
                                }

                            } else if (choice == 8) {
                                user.emptyCart();
                                System.out.println("Cart emptied successfully \n");

                            } else if (choice == 9) {
                                output = user.checkout();
                                if (output == 0) {
                                    System.out.println("Checkout successful \n");
                                } else if (output == -1) {
                                    System.out.println("Quantity demanded more than available \n");
                                } else if (output == -2) {
                                    System.out.println("Insufficient balance! \n");
                                } else if (output == -3) {
                                    System.out.println("Cart is empty \n");
                                }

                            } else if (choice == 10) {
                                System.out.println("Current status: " + user.getStatus());
                                System.out.print("Choose new status: ");
                                String newStatus = sc.nextLine().toLowerCase().trim();

                                output = user.requestStatusUpgrade(newStatus);
                                user = admin.loginCustomer(user.getUsername(), user.getPassword());
                                if (output == 0) {
                                    System.out.println("Status upgraded to " + newStatus + " successfully \n");
                                } else if (output == -1) {
                                    System.out.println("Current and new status are the same \n");
                                } else if (output == -2) {
                                    System.out.println("User is already an elite customer \n");
                                } else if (output == -3) {
                                    System.out.println("Invalid new status \n");
                                } else if (output == -4) {
                                    System.out.println("Insufficient balance! \n");
                                }

                            } else if (choice == 11) {
                                System.out.print("Enter amount to add: ");
                                float amount = sc.nextFloat(); sc.nextLine();
                                admin.addAmount(user, amount);
                                System.out.println("Amount added successfully \n");

                            } else if (choice == 12) {
                                System.out.println("Have a good day, " + user.getUsername() + "\n");
                                user = null;
                                break;

                            } else {
                                System.out.println("Invalid input! \n");
                            }
                        }
                    }

                } else if (choice == 3) {
                    break;

                } else {
                    System.out.println("Invalid input! \n");
                }

            } else if (choice == 5) {
                break;

            } else {
                System.out.println("Invalid input! \n");
            }
        }

        sc.close();
    }

    public static void addProduct(Scanner sc, Admin admin, int cid) {
        System.out.print("Enter product name: ");
        String name = sc.nextLine().toLowerCase().trim();
        System.out.print("Enter product id: ");
        float pid = sc.nextFloat(); sc.nextLine();
        System.out.print("Enter price: ");
        float price = sc.nextFloat(); sc.nextLine();
        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt(); sc.nextLine();
        System.out.print("How many specifications: ");
        int n = sc.nextInt(); sc.nextLine();
        ArrayList<String> specifications = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.println("Enter specification: ");
            String spec = sc.nextLine().toLowerCase().trim();
            specifications.add(spec);
        }
        int output = admin.addProduct(cid, name, pid, price, quantity, specifications);
        if (output == 0) {
            System.out.println("Product added successfully \n");
        } else if (output == -1) {
            System.out.println("Invalid category id \n");
        } else if (output == -2) {
            System.out.println("Product id already in use \n");
        }
    }

    public static void showProducts(Admin admin) {
        HashMap<Integer, ProductCategory> categoriesById = admin.getCategoriesById();
        if (categoriesById.size() == 0) {
            System.out.println("No categories present \n");
        } else {
            for (Map.Entry<Integer, ProductCategory> set1: categoriesById.entrySet()) {
                ProductCategory category = set1.getValue();
                System.out.println("Category id = " + category.getCid());
                System.out.println("Category name = " + category.getName() + "\n");
                for (Map.Entry<Float, Product> set2: category.getCategoryProductsById().entrySet()) {
                    Product product = set2.getValue();
                    System.out.println("Product id = " + product.getPid());
                    System.out.println("Product name = " + product.getName());
                    System.out.println("Product price = " + product.getPrice());
                    System.out.println("Specifications: ");
                    for (String spec: product.getSpecifications()) {
                        System.out.println(spec);
                    }
                    System.out.print("\n");
                }
            }
        }
    }

    public static void showDeals(Admin admin) {
        HashMap<Integer, Deal> dealsById = admin.getDealsById();
        if (dealsById.size() == 0) {
            System.out.println("No deals present \n");
        } else {
            for (Map.Entry<Integer, Deal> set: dealsById.entrySet()) {
                Deal deal = set.getValue();
                Product p1 = admin.getProduct(deal.getPid1());
                Product p2 = admin.getProduct(deal.getPid2());
                System.out.println("Deal id = " + set.getKey());
                System.out.println("Product id 1 = " + p1.getPid());
                System.out.println("Product name 1 = " + p1.getName());
                System.out.println("Product id 2 = " + p2.getPid());
                System.out.println("Product name 2 = " + p2.getName());
                System.out.println("Combined normal price = " + deal.getNormalPrice());
                System.out.println("Combined prime price = " + deal.getPrimePrice());
                System.out.println("Combined elite price = " + deal.getElitePrice() + "\n");
            }
        }
    }

}