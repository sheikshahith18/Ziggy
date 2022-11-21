package utility;

import java.util.HashMap;

import customer.Customer;
import customer.CustomerDB;

import hotel.Dish;
import hotel.Hotel;
import hotel.HotelDB;

public class Init {

    public static void initializeHotel() {

        HotelDB hDB = HotelDB.getInstance();

        Hotel h1 = new Hotel("Hotel MSS", "Sheik", "madurai", 625020, "9876543210", "hotel@gmail.com", false, "Qwerty@123");

        h1.addDish(new Dish("Meals", 100, 30, "Veg"));

        HashMap<String, Double> extras = new HashMap<>();

        extras.put("Mushroom", (double) 20);
        extras.put("Olive", (double) 25);

        h1.addDish(new Dish("Veg Pizza", 260, 20, "Veg", "Topping", extras));

        extras.clear();
        extras.put("Pepperoni", (double) 25);

        h1.addDish(new Dish("Chicken Pizza", 360, 20, "Non-Veg", "Topping", extras));

        extras.clear();
        extras.put("Mayonnaise", (double) 20);
        extras.put("Green Chutney", (double) 20);

        h1.addDish(new Dish("Grill", 450, 20, "Non-Veg", "SideDish", extras));
        h1.addDrink(new Dish("Lassi", 60, 30, "Veg", "Cold"));

        h1.addDrink(new Dish("Chicken Soup", 100, 30, "Non-Veg", "Hot"));

        hDB.addHotel(h1);
        hDB.addServiceablePincode(625020);

    }

    public static void initializeCustomer() {

        CustomerDB cDB = CustomerDB.getInstance();
        cDB.addCustomer(new Customer("customer1", "9876543210", "customer1@gmail.com", "madurai", 625020, "Qwerty@123"));
        cDB.addCustomer(new Customer("customer2", "9012345678", "customer2@gmail.com", "madurai", 625020, "Qwerty@123"));

    }
}
