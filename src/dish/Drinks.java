package dish;


public class Drinks extends Dish {
	private String temperature;

	Drinks(String name, Double price, int quantity, String category, String temperature) {
		super(name, price, quantity, category);
		this.temperature = temperature;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public void print() {
		if (this.getQuantity() > 0)
			System.out.println(
					this.getDishId() + "\t\t" + this.getName() + "\t\t" + this.getPrice() + "\t\t" + this.getTemperature());
	}
}

