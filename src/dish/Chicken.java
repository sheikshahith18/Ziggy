package dish;

import java.util.HashMap;
import java.util.Set;

public class Chicken extends Dish {
	HashMap<String, Double> extras = new HashMap<>();

	Chicken(String name, Double price, int quantity, String category, HashMap<String, Double> extras) {
		super(name, price, quantity, category);
		if (!extras.isEmpty())
			this.extras.putAll(extras);
	}

	@Override
	public void print() {
		if (this.getQuantity() > 0) {
			super.print();
			Set<String> set = extras.keySet();
			if (!set.isEmpty()) {
				System.out.println("Extras :");
				for (String str : set) {
					System.out.println(str + "\t\t" + extras.get(str));
				}
			}
		}
	}

	public HashMap<String, Double> getExtras() {
		return this.extras;
	}

	public void addExtras(String name, double price) {
		this.extras.put(name, price);
	}
}
