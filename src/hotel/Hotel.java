package hotel;

import dish.*;
import utility.Data;
import utility.Order;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

public class Hotel extends Data {
	private final int H_ID;
	private String ownerName;
	private String category;

	private HashMap<Integer, Dish> dishes = new HashMap<>();
	private HashMap<Integer, Pizza> pizzas = new HashMap<>();
	private HashMap<Integer, Chicken> chicken = new HashMap<>();
	private HashMap<Integer, Drinks> drinks = new HashMap<>();
	private HashMap<Integer, Order> pendingOrders = new HashMap<>();
	private ArrayList<String> completedOrders = new ArrayList<>();

	public Hotel(String name, String ownerName, String address, int pincode, String phoneNo, String mailId,
			String category, String password) {
		super(name, phoneNo, mailId, address, pincode, password);
		this.ownerName = ownerName;
		this.category = category;
		H_ID = hId();

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
		if (!order.getDish().isEmpty()) {
			set = order.getDish().keySet();
			for (Integer dishId : set) {
				price += (order.getDish().get(dishId) * dishes.get(dishId).getPrice());
			}
		}
		if (!order.getPizza().isEmpty()) {
			set = order.getPizza().keySet();
			for (Integer dishId : set) {
				price += (order.getPizza().get(dishId) * pizzas.get(dishId).getPrice());
				for (String s : order.getToppings()) {
					if (pizzas.get(dishId).getToppings().containsKey(s)) {
						price += (pizzas.get(dishId).getToppings().get(s));
					}
				}
			}
		}
		if (!order.getChicken().isEmpty()) {
			set = order.getChicken().keySet();
			for (Integer dishId : set) {
				price += (order.getChicken().get(dishId) * chicken.get(dishId).getPrice());
				for (String s : order.getExtras()) {
					if (chicken.get(dishId).getExtras().containsKey(s)) {
						price += (chicken.get(dishId).getExtras().get(s));
					}
				}
			}
		}
		if (!order.getDrinks().isEmpty()) {
			set = order.getDrinks().keySet();
			for (Integer dishId : set) {
				price += (order.getDrinks().get(dishId) * drinks.get(dishId).getPrice());
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
		if (!order.getDish().isEmpty()) {
			set = order.getDish().keySet();
			for (Integer dishId : set) {
				if (dishes.get(dishId).getQuantity() < order.getDish().get(dishId)) {
					System.out.println("Insufficient quantity");
					return false;
				}
			}
		}

		if (!order.getPizza().isEmpty()) {
			set = order.getPizza().keySet();
			for (Integer dishId : set) {
				if (pizzas.get(dishId).getQuantity() < order.getPizza().get(dishId)) {
					return false;
				}
			}
		}
		if (!order.getChicken().isEmpty()) {
			set = order.getChicken().keySet();
			for (Integer dishId : set) {
				if (chicken.get(dishId).getQuantity() < order.getChicken().get(dishId)) {
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
		if (!order.getDish().isEmpty()) {
			set = order.getDish().keySet();
			for (Integer dishId : set) {
				this.dishes.get(dishId)
						.setQuantity(this.dishes.get(dishId).getQuantity() - order.getDish().get(dishId));
			}
		}
		if (!order.getPizza().isEmpty()) {
			set = order.getPizza().keySet();
			for (Integer dishId : set) {
				this.pizzas.get(dishId)
						.setQuantity(this.pizzas.get(dishId).getQuantity() - order.getPizza().get(dishId));
			}
		}
		if (!order.getChicken().isEmpty()) {
			set = order.getChicken().keySet();
			for (Integer dishId : set) {
				this.chicken.get(dishId)
						.setQuantity(this.chicken.get(dishId).getQuantity() - order.getChicken().get(dishId));
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

	public HashMap<Integer, Pizza> getPizzas() {
		return pizzas;
	}

	public void addPizza(Pizza p) {
		this.pizzas.put(p.getDishId(), p);
	}

	public HashMap<Integer, Chicken> getChicken() {
		return chicken;
	}

	public void addChicken(Chicken c) {
		this.chicken.put(c.getDishId(), c);
	}

	public HashMap<Integer, Drinks> getDrinks() {
		return drinks;
	}

	public void addDrink(Drinks d) {
		this.drinks.put(d.getDishId(), d);
	}

	public boolean containsDishes(int dishId) {
		if (this.dishes.containsKey(dishId))
			return true;
		return false;
	}

	public boolean containsPizza(int dishId) {
		if (this.pizzas.containsKey(dishId))
			return true;
		return false;

	}

	public boolean containsChicken(int dishId) {
		if (this.chicken.containsKey(dishId))
			return true;
		return false;
	}

	public boolean containsDrinks(int dishId) {
		if (this.drinks.containsKey(dishId))
			return true;
		return false;
	}

	public int getH_ID() {
		return H_ID;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public HashMap<Integer, Dish> getDishes() {
		return dishes;
	}

	public void addDish(Dish dish) {
		this.dishes.put(dish.getDishId(), dish);
	}

	public void removeDish(int dishId) {
		this.dishes.remove(dishId);
	}

	public void setDishPrice(int dishId, double price) {
		this.dishes.get(dishId).setPrice(price);
	}

	public void displayDishes() {
		Set<Integer> set = new HashSet<>();
		if (!this.dishes.isEmpty()) {
			set = dishes.keySet();
			System.out.println("\nFood Variety : \n");
			System.out.println("---------------------------------------------------------");
			System.out.println("Id\t\tName\t\tPrice\t\tCategory");
			System.out.println("---------------------------------------------------------");
			for (Integer i : set) {
				dishes.get(i).print();
			}
		}

		if (!this.pizzas.isEmpty()) {
			set = pizzas.keySet();
			System.out.println("\nPizza Variety : \n");
			System.out.println("---------------------------------------------------------");
			System.out.println("Id\t\tName\t\tPrice\t\tCategory");
			System.out.println("---------------------------------------------------------");
			for (Integer i : set) {
				pizzas.get(i).print();
			}
		}

		if (!this.chicken.isEmpty()) {
			set = chicken.keySet();
			System.out.println("\nChicken Variety :\n");
			System.out.println("---------------------------------------------------------");
			System.out.println("Id\t\tName\t\tPrice\t\tCategory");
			System.out.println("---------------------------------------------------------");
			for (Integer i : set) {
				chicken.get(i).print();
			}
		}

		if (!this.drinks.isEmpty()) {
			set = drinks.keySet();
			System.out.println("\nDrinks Variety :\n");
			System.out.println("------------------------------------------------------------");
			System.out.println("Id\t\tName\t\tPrice\t\tTemperature");
			System.out.println("------------------------------------------------------------");
			for (Integer i : set) {
				drinks.get(i).print();
			}
		}
	}

	public void printHotel() {
		System.out.println(H_ID + "\t\t" + this.getName() + "\t\t" + this.getCategory());
	}

	public void print() {
		System.out.println("name:" + getName());
		System.out.println("ownername:" + getOwnerName());
		System.out.println("password:" + getPassword());
		System.out.println("mailId:" + getMailId());
		System.out.println("address:" + getAddress());
		System.out.println("pincode:" + getPincode());
		System.out.println("phone:" + getPhoneNo());
	}

}
