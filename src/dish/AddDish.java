package dish;

import java.util.HashMap;
import utility.Input;
import hotel.Hotel;
import hotel.HotelMenu;
import utility.Message;

public class AddDish {
	private String name;
	private double price;
	private int quantity;
	String category;
	HashMap<String, Double> toppings = new HashMap<>();
	HashMap<String, Double> extras = new HashMap<>();
	String temperature;

	public Dish addDish(boolean flag) {
		System.out.print("Enter Food Name : ");
		name = Input.getString(false);
		if (name == null)
			return null;
		System.out.print("Enter Price : ");
		price = Input.getDouble(false);
		if (price == -1)
			return null;
		System.out.print("Enter Quantity : ");
		quantity = Input.getInteger(true);
		if (quantity == -1)
			return null;
		System.out.print("Veg / Non-Veg : ");
		category = Input.getString(false);
		if (flag) {

			Dish d = new Dish(name, price, quantity, category);
			return d;
		}
		return null;

	}

	public Pizza addPizza() {
		addDish(false);
		System.out.println("Do you want to add toppings (press 1 for yes else skip) :");
		toppings.clear();
		int option = Input.getInteger(false);
		if (option == 1) {
			do {
				System.out.println("Enter toppings Name : ");
				String toppingName = Input.getString(false);
				System.out.println("Enter topping price : ");
				double toppingPrice = Input.getDouble(false);
				toppings.put(toppingName, toppingPrice);
				System.out.println("Press 1 to add more else skip : ");
				option = Input.getInteger(false);
			} while (option == 1);

		}
		Pizza p = new Pizza(name, price, quantity, category, toppings);

		return p;
	}

	public Chicken addChicken() {
		addDish(false);
		System.out.println("Do you want to add extras (press 1 for yes else skip) : ");
		extras.clear();
		int option = Input.getInteger(false);
		if (option == 1) {
			do {
				System.out.println("Enter Name : ");
				String extraName = Input.getString(false);
				System.out.println("Enter price : ");
				double extraPrice = Input.getDouble(false);
				extras.put(extraName, extraPrice);
				System.out.println("Press 1 to add more else skip : ");
				option = Input.getInteger(false);
			} while (option == 1);

		}
		Chicken c = new Chicken(name, price, quantity, category, extras);
		return c;
	}

	public Drinks addDrinks() {
		addDish(false);
		System.out.print("Add Temperature (cold/hot) :");
		temperature = Input.getString(true);

		Drinks d = new Drinks(name, price, quantity, category, temperature);
		return d;
	}

	public void updateDish(Hotel h, int dishId) {
		Dish d = h.getDishes().get(dishId);
		int choice;
		do {
			HotelMenu.updateDishMenu();
			System.out.println("4.Delete Dish");
			System.out.println("5.Go back");
			System.out.print("Enter Your Option : ");
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {
				System.out.println("Enter Name : ");
				String name = Input.getString(false);
				d.setName(name);
				System.out.println("Name Changed Successfully");
				break;
			}
			case 2: {
				System.out.print("Set Price : ");
				Double price = Input.getDouble(true);
				if (price == -1)
					break;
				d.setPrice(price);
				System.out.println("Price updated successfully");
				break;
			}
			case 3: {
				System.out.println("Enter Quantity : ");
				int quantity = Input.getInteger(true);
				if (quantity == -1)
					break;
				d.setQuantity(quantity);
				System.out.println("Quantity updated successfully");
				break;
			}
			case 4: {
				System.out.println("Do you really wish to remove the dish from menu ? Type 'YES' to confirm");
				String confirmation = Input.getString(true);
				if (confirmation.toLowerCase().equals("yes")) {
					h.getDishes().remove(dishId);
					System.out.println("Dish removed successfully");
				} else {
					System.out.println("Try again");
				}
				break;
			}
			case 5: {
				return;
			}
			default: {
				System.out.println(Message.INVALID_OPTION);
			}
			}

		} while (choice != 5);
	}

	public void updatePizza(Hotel h, int dishId) {

		Pizza p = h.getPizzas().get(dishId);
		int choice;
		do {
			HotelMenu.updatePizzaMenu();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {
				System.out.println("Enter Name : ");
				String name = Input.getString(false);
				p.setName(name);
				System.out.println("Name Changed Successfully");
				break;
			}
			case 2: {
				System.out.print("Set Price : ");
				Double price = Input.getDouble(false);
				p.setPrice(price);
				System.out.println("Price updated successfully");
				break;
			}
			case 3: {
				System.out.println("Enter Quantity : ");
				int quantity = Input.getInteger(false);
				p.setQuantity(quantity);
				System.out.println("Quantity updated successfully");
				break;
			}
			case 4: {
				System.out.print("Enter Toppings Name : ");
				String toppingName = Input.getString(false);
				System.out.print("Enter Topping Price : ");
				double toppingPrice=Input.getDouble(false);
				if(toppingPrice==-1)
					break;
				p.addToppings(toppingName, toppingPrice);
				
				break;
			}
			case 5: {
				System.out.println("Do you really wish to remove the dish from menu ? Type 'YES' to confirm");
				String confirmation = Input.getString(true);
				if (confirmation.toLowerCase().equals("yes")) {
					h.getPizzas().remove(dishId);
					System.out.println("Dish removed successfully");
				} else {
					System.out.println("Try again");
				}
				break;
			}
			case 6: {
				return;
			}
			default: {
				System.out.println(Message.INVALID_OPTION);
			}
			}

		} while (choice != 4);

	}

	public void updateChicken(Hotel h, int dishId) {

		Chicken c = h.getChicken().get(dishId);
		int choice;
		do {
			HotelMenu.updateChickenMenu();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {
				System.out.println("Enter Name : ");
				String name = Input.getString(false);
				c.setName(name);
				System.out.println("Name Changed Successfully");
				break;
			}
			case 2: {
				System.out.print("Set Price : ");
				Double price = Input.getDouble(false);
				c.setPrice(price);
				System.out.println("Price updated successfully");
				break;
			}
			case 3: {
				System.out.println("Enter Quantity : ");
				int quantity = Input.getInteger(false);
				c.setQuantity(quantity);
				System.out.println("Quantity updated successfully");
				break;
			}
			case 4: {
				System.out.print("Enter Extras Name : ");
				String name=Input.getString(false);
				System.out.print("Enter Extras Price : ");
				double price=Input.getDouble(false);
				if(price==-1)
					break;
				c.addExtras(name, price);
				break;
			}
			case 5: {
				System.out.println("Do you really wish to remove the dish from menu ? Type 'YES' to confirm");
				String confirmation = Input.getString(true);
				if (confirmation.toLowerCase().equals("yes")) {
					h.getChicken().remove(dishId);
					System.out.println("Dish removed successfully");
				} else {
					System.out.println("Try again");
				}
				break;
			}
			default: {
				System.out.println(Message.INVALID_OPTION);
			}
			}

		} while (choice != 4);
	}

	public void updateDrinks(Hotel h, int dishId) {
		Drinks d = h.getDrinks().get(dishId);
		int choice;
		do {
			HotelMenu.updateDrinksMenu();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {
				System.out.println("Enter Name : ");
				String name = Input.getString(false);
				d.setName(name);
				System.out.println("Name Changed Successfully");
				break;
			}
			case 2: {
				System.out.print("Set Price : ");
				Double price = Input.getDouble(false);
				d.setPrice(price);
				System.out.println("Price updated successfully");
				break;
			}
			case 3: {
				System.out.println("Enter Quantity : ");
				int quantity = Input.getInteger(false);
				d.setQuantity(quantity);
				System.out.println("Quantity updated successfully");
				break;
			}
			case 4: {
				System.out.print("Enter Temperature : ");
				String temperature = Input.getString(false);
				d.setTemperature(temperature);
				System.out.println("Temperature changed successfully");
				break;
			}
			case 5: {
				System.out.println("Do you really wish to remove the dish from menu ? Type 'YES' to confirm");
				String confirmation = Input.getString(true);
				if (confirmation.toLowerCase().equals("yes")) {
					h.getDrinks().remove(dishId);
					System.out.println("Dish removed successfully");
				} else {
					System.out.println("Try again");
				}
				break;
			}
			case 6: {
				break;
			}
			default: {
				System.out.println(Message.INVALID_OPTION);
			}
			}

		} while (choice != 6);

	}

}
