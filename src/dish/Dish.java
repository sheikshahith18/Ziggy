package dish;

public class Dish {
	private static int id = 1004;
	private final int DISH_ID;
	private String name;
	private double price;
	private int quantity;
	private String category;

	public Dish(String name, Double price, int quantity, String category) {
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
		System.out.println(
				this.getDishId() + "\t\t" + this.getName() + "\t\t" + this.getPrice() + "\t\t" + this.getCategory());
	}

}
