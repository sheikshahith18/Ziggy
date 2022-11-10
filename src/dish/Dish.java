package dish;

import java.util.Set;
import java.util.HashMap;

public class Dish {
	private static int id = 1004;
	private final int DISH_ID;
	private String name;
	private double price;
	private int quantity;
	private String category;

	private String sideDishName = "";
	private HashMap<String, Double> extras = new HashMap<>();

	private String temperature = "";

	public Dish(String name, double price, int quantity, String category) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.category = category;

		this.DISH_ID = id;
		id += 2;
	}

	public Dish(String name, double price, int quantity, String category, String sideDishName,
			HashMap<String, Double> extras) {

		this(name, price, quantity, category);

		this.sideDishName = sideDishName;
		this.extras.putAll(extras);
	}

	public Dish(String name, double price, int quantity, String category, String temperature) {
		this(name, price, quantity, category);
		this.temperature = temperature;
	}

	public Dish(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
		this.DISH_ID = 0;
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

	public String getSideDishName() {
		return sideDishName;
	}

	public HashMap<String, Double> getExtras() {
		return extras;
	}

	public void addExtras(String name, double price) {
		this.extras.put(name, price);
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public void print(boolean isHotel) {

		System.out.print(this.DISH_ID + "\t\t" + this.name + "\t\t" + this.price + "\t\t" + this.category + "\t\t");

		if (isHotel && this.temperature.equals(""))
			System.out.print(this.getQuantity() + "\t\t");
		if (this.temperature.equals(""))
			System.out.println();

		if (!this.sideDishName.equals("")) {
			System.out.println(sideDishName + " :");
			Set<String> set = this.extras.keySet();
			for (String extrasName : set) {
				System.out.println(extrasName + "\t\t" + this.extras.get(extrasName));
			}
		}

		if (!this.temperature.equals(""))
			System.out.print(this.temperature + "\t\t");

		if (isHotel && !this.temperature.equals(""))
			System.out.print(this.getQuantity());

		System.out.println();

	}

}
