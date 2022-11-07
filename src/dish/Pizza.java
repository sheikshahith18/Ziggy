package dish;

import java.util.Set;
import java.util.HashMap;

public class Pizza extends Dish {
	private HashMap<String, Double> toppings = new HashMap<>();

	public Pizza(String name, Double price, int quantity, String category, HashMap<String, Double> toppings) {
		super(name, price, quantity, category);
		if (!toppings.isEmpty())
			this.toppings.putAll(toppings);
	}

	@Override
	public void print() {
		super.print();
		Set<String> set = toppings.keySet();
		if (!set.isEmpty()) {
			System.out.println("Toppings : ");
			for (String str : set) {
				System.out.println(str + "\t\t" + toppings.get(str));
			}
		}

	}

	public HashMap<String, Double> getToppings() {
		return this.toppings;
	}

	public void addToppings(String toppingName, double toppingPrice) {
		this.toppings.put(toppingName, toppingPrice);
	}
}
