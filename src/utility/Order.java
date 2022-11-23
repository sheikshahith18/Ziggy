package utility;

import customer.Customer;
import customer.CustomerDB;

import hotel.Hotel;
import hotel.HotelDB;

import java.util.Set;
import java.util.HashMap;

public class Order {
    private static int id = 1004;
    private final int ORDER_ID;
    private String hotelMail;
    private String customerMail;
    private double price;

    private HashMap<Integer, Integer> dishes;
    private HashMap<Integer, Integer> drinks;

    private HashMap<Integer, HashMap<String, Double>> extras = new HashMap<>();

    public Order(HashMap<Integer, Integer> dishes, HashMap<Integer, Integer> drinks, HashMap<Integer, HashMap<String, Double>> extras, String customerMail, String hotelMail) {
        this.dishes = dishes;
        this.drinks = drinks;
        this.customerMail = customerMail;
        this.hotelMail = hotelMail;
        this.extras.putAll(extras);

        this.ORDER_ID = id;
        id += 2;
    }

    public HashMap<Integer, Integer> getDishes() {
        return this.dishes;
    }

    public HashMap<Integer, Integer> getDrinks() {
        return this.drinks;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getHotelMail() {
        return this.hotelMail;
    }

    public String getCustomerMail() {
        return this.customerMail;
    }

    public void setHotelMail(String hotelMail) {
        this.hotelMail = hotelMail;
    }

    public void setCustomerMail(String customerMail) {
        this.customerMail = customerMail;
    }

    public int getOrderId() {
        return this.ORDER_ID;
    }

    public double calculatePrice() {

        Hotel h = HotelDB.getInstance().getHotelList().get(this.hotelMail);
        double price = 0;
        Set<Integer> set;

        if (!this.dishes.isEmpty()) {
            set = this.dishes.keySet();
            for (Integer dishId : set) {
                price += this.dishes.get(dishId) * h.getDishes().get(dishId).getPrice();
                if (this.extras.containsKey(dishId)) {
                    Set<String> extraSet = this.extras.get(dishId).keySet();
                    for (String extrasName : extraSet) {
                        price += this.extras.get(dishId).get(extrasName);
                    }
                }
            }
        }

        if (!this.drinks.isEmpty()) {
            set = this.drinks.keySet();
            for (Integer dishId : set) {
                price += this.drinks.get(dishId) * h.getDrinks().get(dishId).getPrice();
            }
        }

        double tax = price * ((double) 5 / 100);
        System.out.println("\nPrice : " + price);
        System.out.println("Tax : " + tax);
        price += tax;

        return price;

    }

    public String getOrderDetails() {
        Set<Integer> set;
        String orderDetails = "ORDER ID : " + this.ORDER_ID + "\n";

        Hotel hotel = HotelDB.getInstance().getHotelList().get(this.hotelMail);

        if (!this.dishes.isEmpty()) {
            orderDetails += "\nFOOD :\n";
            set = this.dishes.keySet();
            for (Integer dishId : set) {
                orderDetails += "\n" + dishId + "\t" + hotel.getDishes().get(dishId).getName() + "\t" + hotel.getDishes().get(dishId).getPrice() + "\t" + this.dishes.get(dishId) + "\n";
                if (this.extras.containsKey(dishId)) {
                    Set<String> extraSet = this.extras.get(dishId).keySet();
                    String extrasDetails = "";
                    for (String extrasName : extraSet) {
                        extrasDetails += extrasName + "\t" + this.extras.get(dishId).get(extrasName) + "\n";
                    }
                    orderDetails = orderDetails + extrasDetails;
                }
            }
        }

        if (!this.drinks.isEmpty()) {
            orderDetails += "\nDRINKS :\n";
            set = this.drinks.keySet();
            for (Integer dishId : set) {
                orderDetails += "\n" + dishId + "\t" + hotel.getDrinks().get(dishId).getName() + "\t" + hotel.getDrinks().get(dishId).getPrice() + "\t" + this.drinks.get(dishId) + "\n";
            }
        }

        return orderDetails;
    }

    public String getOrderDetailsForHotel() {
        String orderDetails = this.getOrderDetails();
        Customer customer = CustomerDB.getInstance().getCustomerList().get(this.customerMail);
        orderDetails += "\nCustomer Name : " + customer.getName();
        orderDetails += "\nCustomer Address : " + customer.getAddress();

        return orderDetails;
    }

    public String getOrderDetailsForCustomer() {

        String orderDetails = this.getOrderDetails();
        Hotel hotel = HotelDB.getInstance().getHotelList().get(this.hotelMail);
        orderDetails += "\nHotel Name : " + hotel.getName();
        orderDetails += "\nHotel PhoneNo : " + hotel.getPhoneNo() + "\n";

        return orderDetails;
    }

}
