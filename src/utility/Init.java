package utility;

import dish.Dish;
import hotel.Hotel;
import hotel.HotelDB;
import customer.Customer;
import customer.CustomerDB;
import java.util.HashMap;

public class Init {
	public static void initializeHotel() {

		HotelDB hDB = HotelDB.getInstance();

		Hotel h1 = new Hotel("Hotel MSS", "Sheik", "madurai", 625020, "6667776665", "hotel@gmail.com", false,
				"Qwerty@123");

		h1.addDish(new Dish("Meals", (double) 100, 30, "Veg"));

		HashMap<String, Double> extras = new HashMap<>();

		extras.put("Mushroom", (double) 20);
		extras.put("Olive", (double) 25);

		h1.addDish(new Dish("Veg Pizza", (double) 260, 20, "Veg", "Topping", extras));

		extras.clear();
		extras.put("Pepperoni", (double) 25);

		h1.addDish(new Dish("Chicken Pizza", (double) 360, 20, "Non-Veg", "Topping", extras));

		extras.clear();
		extras.put("Mayonise", (double) 20);
		extras.put("Green Chutney", (double) 20);

		h1.addDish(new Dish("Grill", (double) 450, 20, "Non-Veg", "SideDish", extras));
		h1.addDrink(new Dish("Lassi", (double) 80, 30, "Veg", "Cold"));

		hDB.addHotel(h1);
		hDB.addServiceablePincodes("625");

	}

	public static void initializeCustomer() {

		CustomerDB cDB = CustomerDB.getInstance();
		cDB.addCustomer(
				new Customer("customer1", "6667778889", "customer1@gmail.com", "madurai", 625020, "Qwerty@123"));
		cDB.addCustomer(
				new Customer("customer2", "6667778889", "customer2@gmail.com", "madurai", 625020, "Qwerty@123"));

	}
}
