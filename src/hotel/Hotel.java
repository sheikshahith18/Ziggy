package hotel;

import dish.*;
import utility.Data;
import utility.Order;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.ArrayList;

public class Hotel extends Data {

	private static int hId = 1000;
	private final int H_ID;
	private String ownerName;
	private String category;
	private boolean isVeg;

	private HashMap<Integer, Dish> dishes = new LinkedHashMap<>();
	private HashMap<Integer, Dish> drinks = new LinkedHashMap<>();

	private HashMap<Integer, Order> pendingOrders = new HashMap<>();
	private ArrayList<String> completedOrders = new ArrayList<>();

	public Hotel(String name, String ownerName, String address, int pincode, String phoneNo, String mailId,
			boolean isVeg, String password) {
		super(name, phoneNo, mailId, address, pincode, password);
		this.ownerName = ownerName;
		this.isVeg = isVeg;
		this.category = isVeg ? "Veg" : "Non-Veg";
		H_ID = (hId+=2);

	}
	
	public boolean isVeg() {
		return isVeg;
	}

	public void setVeg(boolean isVeg) {
		this.isVeg = isVeg;
		this.category = isVeg ? "Veg" : "Non-Veg";
	}

	public int getH_ID() {
		return H_ID;
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

	public double calculatePrice(Order order) {
		double price = 0;
		Set<Integer> set;

		if (!order.getDishes().isEmpty()) {
			set = order.getDishes().keySet();
			for (Integer dishId : set) {
				price += order.getDishes().get(dishId) * this.dishes.get(dishId).getPrice();
				if (order.getExtras().containsKey(dishId)) {
					Set<String> extraSet = order.getExtras().get(dishId).keySet();
					for (String extrasName : extraSet) {
						System.out.println(extrasName);
						price += order.getExtras().get(dishId).get(extrasName);
					}
				}
			}
		}

		if (!order.getDrinks().isEmpty()) {
			set = order.getDrinks().keySet();
			for (Integer dishId : set) {
				price += order.getDrinks().get(dishId) * this.drinks.get(dishId).getPrice();
			}
		}

		double tax = (price * 5) / 100;
		System.out.println("Price : " + price);
		System.out.println("Tax : " + tax);
		price += tax;

		return price;

	}

	public boolean isOrderAvailable(Order order) {
		Set<Integer> set;
		if (!order.getDishes().isEmpty()) {
			set = order.getDishes().keySet();
			for (Integer dishId : set) {
				if (this.dishes.get(dishId).getQuantity() < order.getDishes().get(dishId)) {
					System.out.println("Insufficient Quantity");
					return false;
				}
			}
		}

		if (!order.getDrinks().isEmpty()) {
			set = order.getDrinks().keySet();
			for (Integer dishId : set) {
				if (drinks.get(dishId).getQuantity() < order.getDrinks().get(dishId)) {
					return false;
				}
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
		Set<Integer> set = new HashSet<>();

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
				dishes.get(dishId).print(isHotel);
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
				drinks.get(dishId).print(isHotel);
			}
		}
	}

	public void printHotel() {
		System.out.println(H_ID + "\t\t" + this.getName() + "\t\t" + this.getCategory());
	}

}
