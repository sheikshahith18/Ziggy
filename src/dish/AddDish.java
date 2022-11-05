package dish;

import hotel.Hotel;
import hotel.HotelMenu;
import utility.Input;
import utility.Message;
import java.util.HashMap;

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
		name = Input.getString(false, false);
		if (name == null)
			return null;
		System.out.print("Enter Price : ");
		price = Input.getDouble(false);
		if (price == -1)
			return null;
		System.out.print("Enter Quantity : ");
		quantity = Input.getInteger(false);
		if (quantity == -1)
			return null;
		System.out.print("Veg / Non-Veg : ");
		category = Input.getString(false, false);
		if (flag) {

			Dish d = new Dish(name, price, quantity, category);
			return d;
		}
		return null;

	}

	public Pizza addPizza() {
		addDish(false);
		System.out.print("Do you want to add toppings (press 1 for yes else skip) :");
		toppings.clear();
		int option = Input.getInteger(false);
		if (option == 1) {
			do {
				System.out.print("Enter toppings Name : ");
				String toppingName = Input.getString(false, false);
				System.out.print("Enter topping price : ");
				double toppingPrice = Input.getDouble(false);
				toppings.put(toppingName, toppingPrice);
				System.out.print("Press 1 to add more or 2 to skip : ");
				option = Input.getInteger(1, 2, false);
			} while (option == 1);

		}
		Pizza p = new Pizza(name, price, quantity, category, toppings);

		return p;
	}

	public Chicken addChicken() {
		addDish(false);
		System.out.print("Do you want to add sidedish (press 1 for yes else skip) : ");
		extras.clear();
		int option = Input.getInteger(false);
		if (option == 1) {
			do {
				System.out.print("Enter Name : ");
				String extraName = Input.getString(false, false);
				System.out.print("Enter price : ");
				double extraPrice = Input.getDouble(false);
				extras.put(extraName, extraPrice);
				System.out.print("Press 1 to add more or 2 to skip : ");
				option = Input.getInteger(1, 2, false);
			} while (option == 1);

		}
		Chicken c = new Chicken(name, price, quantity, category, extras);
		return c;
	}

	public Drinks addDrinks() {
		addDish(false);
		System.out.print("Enter Temperature (cold/hot) :");
		temperature = Input.getString(true, false);

		Drinks d = new Drinks(name, price, quantity, category, temperature);
		return d;
	}

	public String changeName() {
		System.out.print("Enter Name : ");
		String name = Input.getString(false, false);
		if (name != null)
			System.out.println("\u001b[32m" + "Name Changed Successfully" + "\u001b[0m");
		return name;
	}

	public double changePrice() {
		System.out.print("Enter Price : ");
		double price = Input.getDouble(false);
		if (price != -1)
			System.out.println("\u001b[32m" + "Price Updated Successfully" + "\u001b[0m");
		return price;
	}

	public int changeQuantity() {
		System.out.print("Enter Quantity : ");
		int quantity = Input.getInteger(false);
		if (quantity != -1)
			System.out.println("\u001b[32m" + "Quantity Updated Successfully" + "\u001b[0m");
		return quantity;
	}

	public boolean removeDish() {
		System.out.print("Do you really wish to remove the dish from menu ? Type 'YES' to confirm : ");
		String confirmation = Input.getString(true, false);
		if (confirmation.toLowerCase().equals("yes")) {
			System.out.println("\u001b[32m" + "Dish Removed Successfully" + "\u001b[0m");
			return true;
		} else {
			System.out.println("Try again");
			return false;
		}
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
				String name = changeName();
				if (name == null)
					break;

				d.setName(name);
				break;
			}
			case 2: {
				Double price = changePrice();
				if (price == -1)
					break;
				d.setPrice(price);
				break;
			}
			case 3: {
				int quantity = changeQuantity();
				if (quantity == -1)
					break;
				d.setQuantity(quantity);
				break;
			}
			case 4: {
				boolean removeDish = removeDish();
				if (removeDish) {
					h.getDishes().remove(dishId);
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
				String name = changeName();
				if (name == null)
					break;
				p.setName(name);
				break;
			}
			case 2: {
				Double price = changePrice();
				if (price == -1)
					break;
				p.setPrice(price);
				break;

			}
			case 3: {
				int quantity = changeQuantity();
				if (quantity == -1)
					break;
				p.setQuantity(quantity);
				break;

			}
			case 4: {
				System.out.print("Enter Toppings Name : ");
				String toppingName = Input.getString(false, false);
				System.out.print("Enter Topping Price : ");
				double toppingPrice = Input.getDouble(false);
				if (toppingPrice == -1)
					break;
				p.addToppings(toppingName, toppingPrice);
				System.out.println("\u001b[32m" + "Toppings Added Successfully" + "\u001b[0m");

				break;
			}
			case 5: {
				System.out.print("Enter Toppings Name : ");
				String toppingName = Input.getString(false, false);
				if (p.getToppings().containsKey(toppingName)) {
					p.getToppings().remove(toppingName);
					System.out.println("\u001b[32m" + "Topping Removed Successfully" + "\u001b[0m");
				} else {
					System.err.println("Topping not found....try again");
				}
				break;
			}
			case 6: {
				boolean removeDish = removeDish();

				if (removeDish)
					h.getPizzas().remove(dishId);

				break;
			}
			case 7: {
				return;
			}
			default: {
				System.out.println(Message.INVALID_OPTION);
			}
			}

		} while (choice != 7);

	}

	public void updateChicken(Hotel h, int dishId) {

		Chicken c = h.getChicken().get(dishId);
		int choice;
		do {
			HotelMenu.updateChickenMenu();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {
				String name = changeName();
				if (name == null)
					break;
				c.setName(name);
				break;
			}
			case 2: {
				Double price = changePrice();
				if (price == -1)
					break;
				c.setPrice(price);
				break;

			}
			case 3: {
				int quantity = changeQuantity();
				if (quantity == -1)
					break;
				c.setQuantity(quantity);
				break;

			}
			case 4: {
				System.out.print("Enter Sidedish Name : ");
				String name = Input.getString(false, false);
				System.out.print("Enter Sidedish Price : ");
				double price = Input.getDouble(false);
				if (price == -1)
					break;
				c.addExtras(name, price);
				System.out.println("\u001b[32m" + "Sidedish Added Successfully" + "\u001b[0m");
				break;
			}

			case 5: {
				System.out.println("Enter Sidedish's Name : ");
				String name = Input.getString(false, false);
				if (c.getExtras().containsKey(name)) {
					c.getExtras().remove(name);
					System.out.println("\u001b[32m" + "Sidedish Removed Successfully" + "\u001b[0m");
				} else {
					System.err.println("Sidedish not found");
				}
				break;
			}
			case 6: {
				boolean removeDish = removeDish();

				if (removeDish)
					h.getChicken().remove(dishId);

				break;
			}
			case 7: {
				return;
			}
			default: {
				System.out.println(Message.INVALID_OPTION);
			}
			}

		} while (choice != 7);
	}

	public void updateDrinks(Hotel h, int dishId) {
		Drinks d = h.getDrinks().get(dishId);
		int choice;
		do {
			HotelMenu.updateDrinksMenu();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {
				String name = changeName();
				if (name == null)
					break;

				d.setName(name);
				break;
			}
			case 2: {
				Double price = changePrice();
				if (price == -1)
					break;
				d.setPrice(price);
				break;

			}
			case 3: {
				int quantity = changeQuantity();
				if (quantity == -1)
					break;
				d.setQuantity(quantity);
				break;

			}
			case 4: {
				System.out.print("Enter Temperature : ");
				String temperature = Input.getString(true, false);
				d.setTemperature(temperature);
				System.out.println("\u001b[32m" + "Temperature Changed Successfully" + "\u001b[0m");
				break;
			}
			case 5: {
				boolean removeDish = removeDish();

				if (removeDish)
					h.getDrinks().remove(dishId);

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
