package dish;

import java.util.HashMap;
import java.util.Set;

public class Dish {
	private static int id = 1004;
	private final int DISH_ID;
	private String name;
	private double price;
	private int quantity;
	String category;

	Dish(String name, Double price, int quantity, String category) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.category = category;

		this.DISH_ID = id;
		id += 2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getCategory() {
		return this.category;
	}

	public int getDishId() {
		return DISH_ID;
	}

	public void print() {
		if (quantity > 0)
			System.out.println(
					this.getDishId() + "\t" + this.getName() + "\t" + this.getPrice() + "\t" + this.getCategory());
	}

}
