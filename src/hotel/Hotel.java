package hotel;

import utility.Data;
import utility.Order;

import java.util.Set;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.ArrayList;

public class Hotel extends Data {

	private static int hId = 1000;
	private final int HOTEL_ID;
	private String ownerName;
	private String category;
	private boolean isVeg;

	private HashMap<Integer, Dish> dishes = new LinkedHashMap<>(); // dishId,dish
	private HashMap<Integer, Dish> drinks = new LinkedHashMap<>(); // dishId,dish

	private HashMap<Integer, Order> pendingOrders = new HashMap<>(); // orderId,order
	private ArrayList<String> completedOrders = new ArrayList<>();

	public Hotel(String name, String ownerName, String address, int pincode, String phoneNo, String mailId,
			boolean isVeg, String password) {

		super(name, phoneNo, mailId, address, pincode, password);
		this.ownerName = ownerName;
		this.isVeg = isVeg;
		this.category = isVeg ? "Veg" : "Non-Veg";
		HOTEL_ID = (hId += 2);
	}

	public boolean isVeg() {
		return isVeg;
	}

	public void setVeg(boolean isVeg) {
		this.isVeg = isVeg;
		this.category = isVeg ? "Veg" : "Non-Veg";
	}

	public int getHotelId() {
		return this.HOTEL_ID;
	}

	public String getCategory() {
		return category;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public HashMap<Integer, Dish> getDishes() {
		return this.dishes;
	}

	public HashMap<Integer, Dish> getDrinks() {
		return this.drinks;
	}

	public void addDish(Dish dish) {
		this.dishes.put(dish.getDishId(), dish);
	}

	public void addDrink(Dish dish) {
		this.drinks.put(dish.getDishId(), dish);
	}

	public void newOrder(Order order) {
		this.pendingOrders.put(order.getOrderId(), order);
	}

	public HashMap<Integer, Order> getPendingOrders() {
		return pendingOrders;
	}

	public void addCompletedOrder(String str) {
		str = "--------------------------------------------------" + "\n" + str;
		str += "\n--------------------------------------------------";
		this.completedOrders.add(str);
	}

	public ArrayList<String> getCompletedOrders() {
		return this.completedOrders;
	}

	public void printHotel() {
		System.out.println(this.HOTEL_ID + "\t\t" + this.getName() + "\t\t" + this.getCategory());
	}

	public boolean isHotelServiceable(int pincode) {
		return Integer.toString(this.getPincode()).substring(0, 3).equals(Integer.toString(pincode).substring(0, 3));
	}

	public boolean isOrderAvailable(Order order) {
		Set<Integer> set;
		if (!order.getDishes().isEmpty()) {
			set = order.getDishes().keySet();
			for (Integer dishId : set) {

				if (this.dishes.get(dishId).getQuantity() < order.getDishes().get(dishId))
					return false;

			}
		}

		if (!order.getDrinks().isEmpty()) {
			set = order.getDrinks().keySet();
			for (Integer dishId : set) {

				if (drinks.get(dishId).getQuantity() < order.getDrinks().get(dishId))
					return false;

			}
		}
		return true;

	}

	public void deliverOrder(Order order) {
		Set<Integer> set;

		if (!order.getDishes().isEmpty()) {
			set = order.getDishes().keySet();
			for (Integer dishId : set) {
				this.dishes.get(dishId)
						.setQuantity(this.dishes.get(dishId).getQuantity() - order.getDishes().get(dishId));
			}
		}

		if (!order.getDrinks().isEmpty()) {
			set = order.getDrinks().keySet();
			for (Integer dishId : set) {
				this.drinks.get(dishId)
						.setQuantity(this.drinks.get(dishId).getQuantity() - order.getDrinks().get(dishId));
			}
		}

	}

	public void displayDishes(boolean isHotel) {
		Set<Integer> set;

		if (!this.dishes.isEmpty()) {
			set = dishes.keySet();
			System.out.println("\nFOOD VARIETY : \n");
			System.out.println("--------------------------------------------------------------------------------");
			System.out.print("Id\t\tName\t\tPrice\t\tCategory\t\t");
			if (isHotel)
				System.out.print("Quantity");
			System.out.println("\n--------------------------------------------------------------------------------");
			for (Integer dishId : set) {
				System.out.println();
				this.dishes.get(dishId).print(isHotel);
			}
		}

		if (!this.drinks.isEmpty()) {
			set = drinks.keySet();
			System.out.println("\nDRINKS VARIETY :\n");
			System.out.println("--------------------------------------------------------------------------------");
			System.out.print("Id\t\tName\t\tPrice\t\tCategory\t\tTemperature\t\t");
			if (isHotel)
				System.out.print("Quantity");
			System.out.println("\n--------------------------------------------------------------------------------");
			for (Integer dishId : set) {
				System.out.println();
				this.drinks.get(dishId).print(isHotel);
			}
		}
	}

}
