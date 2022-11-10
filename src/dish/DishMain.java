package dish;

import hotel.Hotel;
import utility.Input;
import java.util.HashMap;

public class DishMain {

	public Dish addDish(boolean isDish, boolean isVeg, boolean isDrink) {
		System.out.print("Enter Food Name : ");
		String dishName = Input.getString(false, false);

		System.out.print("Enter Price : ");
		double dishPrice = Input.getDouble(false);

		System.out.print("Enter Quantity : ");
		int dishQuantity = Input.getInteger(false);
		String dishCategory = "";
		if (isVeg)
			dishCategory = "Veg";
		else {
			System.out.print("Enter Category ( 1.Veg / 2.Non-Veg ) : ");
			int choice = Input.getInteger(1, 2, false);
			if (choice == 1)
				dishCategory = "Veg";
			else if (choice == 2)
				dishCategory = "Non-Veg";
		}

		if (isDish) {

			System.out.print("Do you want to add sidedish for this dish (1-Y/2-N) : ");
			int option = Input.getInteger(1, 2, false);
			HashMap<String, Double> extras = new HashMap<>();

			String sideDishName = "";
			boolean isExtraAdded = false;
			if (option == 1) {
				isExtraAdded = true;
				System.out.print("1-Toppings/2-Sidedish : ");
				option = Input.getInteger(1, 2, false);
				if (option == 1)
					sideDishName = "Topping";
				else if (option == 2)
					sideDishName = "Sidedish";

				do {
					System.out.print("Enter " + sideDishName + "'s name : ");
					String name = Input.getString(false, false);
					System.out.print("Enter " + sideDishName + "'s price : ");
					double sideDishPrice = Input.getDouble(false);
					extras.put(name, sideDishPrice);
					System.out.print("Do you want to add more " + sideDishName + " (1-Y/2-N) : ");
					option = Input.getInteger(1, 2, false);
				} while (option == 1);
			}

			if (isExtraAdded) {
				Dish d = new Dish(dishName, dishPrice, dishQuantity, dishCategory, sideDishName, extras);
				return d;
			} else {
				Dish d = new Dish(dishName, dishPrice, dishQuantity, dishCategory);
				return d;
			}
		}

		if (isDrink) {
			System.out.print("Enter Temperature (1-Cold/2-Hot) : ");

			int temperature = Input.getInteger(1, 2, false);
			String dishTemperature;
			if (temperature == 1)
				dishTemperature = "Cold";
			else
				dishTemperature = "Hot";

			Dish d = new Dish(dishName, dishPrice, dishQuantity, dishCategory, dishTemperature);
			return d;
		}

		return null;

	}

	public boolean removeDish() {
		System.out.print("\nDo you really wish to remove the dish from menu ? Press 1-Y or 2-N : ");
		int confirmation = Input.getInteger(1, 2, true);
		if (confirmation == -1)
			return false;
		if (confirmation == 1) {
			System.out.println("\n\u001b[32m" + "Dish Removed Successfully" + "\u001b[0m");
			return true;
		} else if (confirmation == 2)
			return false;
		else {
			System.out.println("\nTry again later...");
			return false;
		}
	}

	public void updateDish(Hotel h, int dishId, boolean isDish, boolean isDrink) {
		Dish d = null;
		if (isDish)
			d = h.getDishes().get(dishId);
		else if (isDrink)
			d = h.getDrinks().get(dishId);
		boolean isSideDishPresent = d.getSideDishName().equals("") ? false : true;

		int choice;
		do {
			updateDishMenu(d, isSideDishPresent, isDrink);
			choice = Input.getInteger(false);
			switch (choice) {

			case 1: {
				System.out.print("\nEnter Name : ");
				String name = Input.getString(false, true);
				if (name == null)
					break;

				System.out.println("\n\u001b[32m" + "Name Changed Successfully" + "\u001b[0m");
				d.setName(name);
				break;
			}
			case 2: {
				System.out.print("\nEnter Price : ");
				Double price = Input.getDouble(true);
				if (price == -1)
					break;
				System.out.println("\n\u001b[32m" + "Price Updated Successfully" + "\u001b[0m");
				d.setPrice(price);
				break;
			}
			case 3: {
				System.out.print("\nEnter Quantity : ");
				int quantity = Input.getInteger(true);
				if (quantity == -1)
					break;
				System.out.println("\n\u001b[32m" + "Quantity Updated Successfully" + "\u001b[0m");
				d.setQuantity(quantity);
				break;
			}
			case 4: {
				if (!isSideDishPresent && !isDrink)
					return;

				if (isSideDishPresent) {
					System.out.print("Enter " + d.getSideDishName() + "'s name : ");
					String name = Input.getString(false, false);
					System.out.print("Enter " + d.getSideDishName() + "'s price : ");
					double price = Input.getDouble(false);
					if (price == -1)
						break;
					d.addExtras(name, price);
					System.out.println("\u001b[32m" + d.getSideDishName() + " added successfully" + "\u001b[0m");
					break;
				}
				if (isDrink) {
					System.out.print("Set Temperature (1-Cold/2-Hot) : ");
					int temp = Input.getInteger(1, 2, true);
					if (temp == -1)
						break;
					if (temp == 1)
						d.setTemperature("cold");
					else
						d.setTemperature("hot");
				}
				break;
			}
			case 5: {
				if (isDrink)
					return;

				if (!isSideDishPresent && !isDrink) {
					System.out.println("\nInvalid Option...Try again....");
					break;
				}

				System.out.print("Enter " + d.getSideDishName() + "'s name : ");
				String name = Input.getString(false, false);
				if (d.getExtras().containsKey(name)) {
					d.getExtras().remove(name);
					System.out.println("\u001b[32m" + d.getSideDishName() + " removed successfully" + "\u001b[0m");
				} else {
					System.out.println(d.getSideDishName() + " not found");
				}
				break;
			}
			case 6: {
				if (!isSideDishPresent && !isDrink) {
					System.out.println("\nInvalid Option...Try again....");
					break;
				}
				return;
			}
			default: {
				System.out.println("\nInvalid Option...Try again....");
			}
			}

		} while (choice != 6);
	}

	public void updateDishMenu(Dish d, boolean isSideDishPresent, boolean isDrink) {
		System.out.println("\n1.Change Name");
		System.out.println("2.Change Price");
		System.out.println("3.Change Quantity");
		if (!isSideDishPresent && !isDrink) {
			System.out.println("4.Go back");
		}
		if (isSideDishPresent) {
			System.out.println("4.Add " + d.getSideDishName());
			System.out.println("5.Remove " + d.getSideDishName());
			System.out.println("6.Go back");
		}
		if (isDrink) {
			System.out.println("4.Change Temperature");
			System.out.println("5.Go Back");
		}
		System.out.print("\nEnter your option : ");
	}

	public void addDishMenu() {

	}
}
