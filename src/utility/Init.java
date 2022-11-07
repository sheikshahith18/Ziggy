package utility;

import dish.*;
import hotel.Hotel;
import hotel.HotelDB;
import customer.Customer;
import customer.CustomerDB;
import java.util.HashMap;

public class Init {
	public static void initializeHotel() {

		HotelDB hDB = HotelDB.getInstance();
		Hotel h1 = new Hotel("Hotel MSS", "Sheik", "madurai", 625020, "6667776665", "hotel@gmail.com", "Non-Veg, Chinese",
				"Qwerty@123");

		h1.addDish(new Dish("Meals", (double) 100, 30, "Veg"));

		HashMap<String, Double> toppings = new HashMap<>();
		toppings.put("Mushroom", (double) 25);
		toppings.put("Pepperoni", (double) 30);
		h1.addPizza(new Pizza("Veg Pizza", (double) 260, 20, "Veg", toppings));

		HashMap<String, Double> sideDish = new HashMap<>();
		sideDish.put("Mayonise", (double) 20);
		sideDish.put("Green Chutney", (double) 20);
		h1.addChicken(new Chicken("Grill", (double) 450, 20, "Non-Veg", sideDish));

		h1.addDrink(new Drinks("Lassi", (double) 80, 30, "Veg", "Cold"));
		hDB.addHotel(h1);

	}

	public static void initializeCustomer() {

		CustomerDB cDB = CustomerDB.getInstance();
		cDB.addCustomer(new Customer("customer1", "6667778889", "customer1@gmail.com", "madurai", 625020, "Qwerty@123",
				1234, 1234));
		cDB.addCustomer(new Customer("customer2", "6667778889", "customer2@gmail.com", "madurai", 625020, "Qwerty@123",
				1234, 1234));

	}
}
