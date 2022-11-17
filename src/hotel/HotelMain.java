package hotel;

import java.util.HashMap;
import java.util.Set;

import utility.Input;
import utility.Order;
import utility.Register;

import customer.CustomerDB;

public class HotelMain extends Register {

	private static final HotelDB HOTEL_DB = HotelDB.getInstance();

	public void hotelMainMenu() {
		int choice;
		do {
			hotelHomeScreen();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {
				String mailId = super.signup(true);
				successfulLogin(mailId);
				break;
			}
			case 2: {
				String mailId = super.login(true);
				if (!mailId.equals(""))
					successfulLogin(mailId);
				break;
			}
			case 3: {
				return;
			}
			default: {
				System.out.println("\nInvalid Option...Try again....");
			}
			}

		} while (true);
	}

	private void successfulLogin(String mailId) {
		Hotel h = HOTEL_DB.getHotelList().get(mailId);
		System.out.println("\n....Welcome Mr." + h.getOwnerName() + "....\n");
		int choice;
		do {

			hotelLoginScreen();
			choice = Input.getInteger(false);
			switch (choice) {

			case 1: {
				addDish(h);
				break;
			}
			case 2: {
				updateDish(h);
				break;
			}
			case 3: {
				removeDish(h);
				break;
			}
			case 4: {
				viewOrders(h);
				break;
			}
			case 5: {
				orderHistory(h);
				break;
			}
			case 6: {
				editAccount(h);
				break;
			}
			case 7: {
				return;
			}
			default: {
				System.out.println("\nInvalid Option...Try again....");
			}

			}

		} while (true);
	}

	private void addDish(Hotel h) {

		int choice;

		do {
			System.out.println("1.Dish");
			System.out.println("2.Drinks");
			System.out.println("3.Go back");
			System.out.print("Enter your option : ");

			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {
				Dish d = addDish(true, h.isVeg());
				h.addDish(d);
				System.out.println("\n\u001b[36m" + "Dish Added Successfully..." + "\u001b[0m\n");
				break;
			}

			case 2: {
				Dish d = addDish(false, h.isVeg());
				h.addDrink(d);
				System.out.println("\n\u001b[36m" + "Drink Added Successfully..." + "\u001b[0m\n");
				break;
			}
			case 3: {
				return;
			}
			default: {

				System.out.println("\nInvalid Option...Try again....\n");

			}
			}

		} while (true);
	}

	private Dish addDish(boolean isDish, boolean isVeg) {

		System.out.print("Enter Food Name : ");
		String dishName = Input.getString(false, false);

		System.out.print("Enter Price : ");
		double dishPrice = Input.getDouble(false);

		System.out.print("Enter Quantity : ");
		int dishQuantity = Input.getInteger(false);

		String dishCategory = "Veg";

		if (!isVeg) {
			System.out.print("Enter Category ( 1.Veg / 2.Non-Veg ) : ");
			int choice = Input.getInteger(1, 2, false);
			if (choice == 2)
				dishCategory = "Non-Veg";
		}

		if (isDish) {

			System.out.print("Do you want to add sidedish for this dish (1-Y/2-N) : ");
			int option = Input.getInteger(1, 2, false);
			HashMap<String, Double> extras = new HashMap<>();

			String sideDishName = "";

			if (option == 1) {
				System.out.print("1-Toppings/2-Sidedish : ");
				option = Input.getInteger(1, 2, false);
				if (option == 1)
					sideDishName = "Topping";
				else
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
				return new Dish(dishName, dishPrice, dishQuantity, dishCategory, sideDishName, extras);

			} else {
				return new Dish(dishName, dishPrice, dishQuantity, dishCategory);

			}
		}

		else {
			System.out.print("Enter Temperature (1-Cold/2-Hot) : ");

			int temperature = Input.getInteger(1, 2, false);

			return new Dish(dishName, dishPrice, dishQuantity, dishCategory, temperature == 1 ? "Cold" : "Hot");
		}
	}

	private void updateDish(Hotel h) {

		if (h.getDishes().isEmpty() && h.getDrinks().isEmpty()) {
			System.out.println("\nNo Dishes were added...Add some before updating\n");
			return;
		}

		int option;

		do {
			h.displayDishes(true);
			System.out.print("\nEnter Dish ID : ");
			int dishId = Input.getInteger(true);
			if (dishId == -1)
				return;

			if (h.getDishes().containsKey(dishId)) {
				updateDish(h, dishId, true, false);
			} else if (h.getDrinks().containsKey(dishId)) {
				updateDish(h, dishId, false, true);
			} else {
				System.out.println("\nInvalid Dish Id");
			}
			System.out.print("Press 1 to update again or 2 to exit : ");
			option = Input.getInteger(1, 2, true);
		} while (option == 1);

	}

	private void updateDish(Hotel h, int dishId, boolean isDish, boolean isDrink) {
		Dish d;
		if (isDish)
			d = h.getDishes().get(dishId);
		else
			d = h.getDrinks().get(dishId);

		int choice;
		do {
			boolean isSideDishPresent = !d.getExtras().isEmpty();
			updateDishMenu(d.getSideDishName(), isSideDishPresent, isDrink);
			choice = Input.getInteger(false);
			switch (choice) {

			case 1: {
				System.out.print("\nEnter Name : ");
				String name = Input.getString(false, true);
				if (name.equals(""))
					break;
				System.out.println("\n\u001b[36m" + "Name Changed Successfully" + "\u001b[0m");
				d.setName(name);
				break;
			}
			case 2: {
				System.out.print("\nEnter Price : ");
				double price = Input.getDouble(true);
				if (price == -1)
					break;
				System.out.println("\n\u001b[36m" + "Price Updated Successfully" + "\u001b[0m");
				d.setPrice(price);
				break;
			}
			case 3: {
				System.out.print("\nEnter Quantity : ");
				int quantity = Input.getInteger(true);
				if (quantity == -1)
					break;
				System.out.println("\n\u001b[36m" + "Quantity Updated Successfully" + "\u001b[0m");
				d.setQuantity(quantity);
				break;
			}
			case 4: {
				if (isDrink) {
					System.out.print("Set Temperature (1-Cold/2-Hot) : ");
					int temperature = Input.getInteger(1, 2, true);
					if (temperature == -1)
						break;
					if (temperature == 1)
						d.setTemperature("cold");
					else
						d.setTemperature("hot");
					System.out.println("\u001b[36m" + "Temperature updated successfully" + "\u001b[0m");
				} else if (isSideDishPresent) {
					System.out.print("Enter " + d.getSideDishName() + "'s name : ");
					String name = Input.getString(false, false);
					System.out.print("Enter " + d.getSideDishName() + "'s price : ");
					double price = Input.getDouble(false);
					if (price == -1)
						break;
					d.addExtras(name, price);
					System.out.println("\u001b[36m" + d.getSideDishName() + " added successfully" + "\u001b[0m");
					break;
				} else
					return;

				break;
			}
			case 5: {
				if (isDrink)
					return;

				if (!isSideDishPresent) {
					System.out.println("\nInvalid Option...Try again....");
					break;
				}

				System.out.print("Enter " + d.getSideDishName() + "'s name : ");
				String name = Input.getString(false, false);
				if (d.getExtras().containsKey(name)) {
					d.getExtras().remove(name);
					System.out.println("\u001b[36m" + d.getSideDishName() + " removed successfully" + "\u001b[0m");
				} else {
					System.out.println(d.getSideDishName() + " not found");
				}
				break;
			}
			case 6: {
				if (isSideDishPresent)
					return;
			}
			default: {
				System.out.println("\nInvalid Option...Try again....");
			}
			}

		} while (true);
	}

	private void removeDish(Hotel h) {

		if (h.getDishes().isEmpty() && h.getDrinks().isEmpty()) {
			System.out.println("\nNo Dishes were added...Add some before removing\n");
			return;
		}

		h.displayDishes(true);

		int dishId;
		System.out.print("\nEnter Dish Id : ");
		dishId = Input.getInteger(true);

		boolean isDish;

		if ((isDish = h.getDishes().containsKey(dishId)) || h.getDrinks().containsKey(dishId)) {
			System.out.print("Do you really wish to remove the dish (1-Y/2-N) : ");
			int option = Input.getInteger(1, 2, true);
			if (option == 2 || option == -1)
				return;
			if (isDish)
				h.getDishes().remove(dishId);
			else
				h.getDrinks().remove(dishId);
			System.out.println("\u001b[36m" + "\nDish removed successfully....\n" + "\u001b[0m");
		} else {
			System.out.println("\nInvalid Dish Id\n");
		}
	}

	private void viewOrders(Hotel h) {

		if (h.getPendingOrders().isEmpty()) {
			System.out.println("\nNo orders available...try again later");
			return;
		}

		Set<Integer> s = h.getPendingOrders().keySet();
		System.out.println();

		Set<Integer> set = h.getPendingOrders().keySet();
		for (int orderID : set) {
			System.out.println("----------------------------------------");
			System.out.println(h.getPendingOrders().get(orderID).getOrderDetails(true));
			System.out.println("----------------------------------------");
		}

		System.out.print("Enter Order Id : ");
		Order order;
		int orderId;
		int attempt = 1;
		do {
			orderId = Input.getInteger(true);
			if (!s.contains(orderId)) {
				if (Input.isAttemptExceeded(true, attempt++) || orderId == -1)
					return;
				System.out.print("Invalid order Id....Enter again : ");
			} else {
				order = h.getPendingOrders().get(orderId);
				break;
			}
		} while (true);

		System.out.println("\n1.Deliver Order");
		System.out.println("2.Cancel Order");
		System.out.println("3.Go Back");
		System.out.print("Enter Option : ");

		int option = Input.getInteger(1, 3, false);

		if (option == 3)
			return;

		String orderDetail = "";
		boolean isOrderAvailable = true;

		if (option == 1 && (isOrderAvailable = h.isOrderAvailable(order))) {
			h.deliverOrder(order);
			orderDetail = order.getOrderDetails(true) + "\nOrder Status : Delivered Successfully\n"
					+ Input.getDateTime();
			System.out.println("\nOrder delivered Successfully");
		} else if (option == 2) {
			orderDetail = order.getOrderDetails(true) + "\nOrder Status : Cancelled by hotel\n" + Input.getDateTime();

			System.out.println("\nOrder Cancelled");

		} else if (!isOrderAvailable) {
			orderDetail = order.getOrderDetails(true) + "\nOrder Status : Refunded\n" + Input.getDateTime();
			System.out.println("\nInsufficient Products");
		}

		removeOrder(h, order, orderDetail);

		if (option == 2 || !isOrderAvailable) {
			CustomerDB.getInstance().getCustomerList().get(order.getCustomerMail()).getUPI()
					.refundMoney(order.getPrice());
			System.out.println("Amount Refunded to Customer\n");
		}
	}

	private void removeOrder(Hotel h, Order order, String orderDetails) {
		h.addCompletedOrder(orderDetails);

		CustomerDB.getInstance().getCustomerList().get(order.getCustomerMail()).addCompletedOrder(order.getOrderId(),
				orderDetails);

		h.getPendingOrders().remove(order.getOrderId());
	}

	private void orderHistory(Hotel h) {
		if (h.getCompletedOrders().isEmpty()) {
			System.out.println("\nNo Delivery were made...Try again later");
			return;
		}

		for (String str : h.getCompletedOrders()) {
			System.out.println(str + "\n");
		}
	}

	private void editAccount(Hotel h) {
		int choice;
		do {

			accountSettingsScreen();
			choice = Input.getInteger(false);
			switch (choice) {
			case 1: {
				String name = super.newName(true, false, true);
				if (name.equals(""))
					break;
				h.setName(name);
				System.out.println("\n\u001b[36m" + "Name Changed Successfully" + "\u001b[0m");
				break;
			}
			case 2: {
				String ownerName = super.newName(true, true, true);
				if (ownerName.equals(""))
					break;

				h.setOwnerName(ownerName);
				System.out.println("\n\u001b[36m" + "Owner's Name Changed Successfully" + "\u001b[0m");
				break;
			}
			case 3: {
				String address = super.newAddress(true);
				if (address.equals(""))
					break;
				h.setAddress(address);
				System.out.println("\n\u001b[36m" + "Address Changed Successfully" + "\u001b[0m");
				break;
			}
			case 4: {
				int pincode = super.newPincode(true);
				if (pincode == -1)
					break;

				HOTEL_DB.removeServiceablePincode(h.getPincode());
				h.setPincode(pincode);
				HOTEL_DB.addServiceablePincode(pincode);
				System.out.println("\n\u001b[36m" + "Pincode Changed Successfully" + "\u001b[0m");

				break;
			}
			case 5: {
				String mailId = super.newMailId(true, true);
				if (mailId.equals(""))
					break;
				HOTEL_DB.removeHotel(h.getMailId());
				h.setMailId(mailId);
				HOTEL_DB.addHotel(h);
				System.out.println("\n\u001b[36m" + "Mail Id Changed Successfully" + "\u001b[0m");
				break;
			}
			case 6: {
				String phoneNo = super.newPhoneNo(true);
				if (phoneNo.equals(""))
					break;

				h.setPhoneNo(phoneNo);
				System.out.println("\n\u001b[36m" + "Phone No Changed Successfully" + "\u001b[0m");
				break;
			}
			case 7: {

				System.out.print("Enter Category ( 1.Veg / 2.Non-Veg ) : ");
				int option = Input.getInteger(1, 2, true);
				if (option == -1)
					break;
				boolean isVeg = false;
				if (option == 1)
					isVeg = true;

				h.setVeg(isVeg);

				System.out.println("\n\u001b[36m" + "Category Changed Successfully" + "\u001b[0m");
				break;
			}
			case 8: {

				System.out.print("Enter Old Password : ");
				String password = Input.getString(false, false);
				if (password.equals(h.getPassword())) {

					String newPassword;
					if (!(newPassword = super.newPassword(true)).equals("")) {
						h.setPassword(newPassword);
						System.out.println("\n\u001b[36m" + "Password Changed Successfully" + "\u001b[0m");
					}

				} else {
					System.out.println("\nIncorrect Password");
				}

				break;
			}
			case 9: {
				return;
			}
			default: {
				System.out.println("\nInvalid Option...Try again....\n");
			}
			}
		} while (true);
	}

	private void hotelHomeScreen() {
		System.out.println("\t\t\t ---------------------------------------------");
		System.out.println("\t\t\t|                     Hotel                   |");
		System.out.println("\t\t\t ---------------------------------------------");

		System.out.println("\n1.Register");
		System.out.println("2.Login");
		System.out.println("3.Go Back");
		System.out.print("\nEnter Your Option : ");
	}

	private void hotelLoginScreen() {

		System.out.println("\n1.Add Dish");
		System.out.println("2.Update Dish");
		System.out.println("3.Remove Dish");
		System.out.println("4.View Orders");
		System.out.println("5.Order History");
		System.out.println("6.Account Settings");
		System.out.println("7.Logout");
		System.out.print("\nEnter Your Option : ");
	}

	private void accountSettingsScreen() {
		System.out.println("\n1.Change Hotel Name");
		System.out.println("2.Change Owner Name");
		System.out.println("3.Change Address");
		System.out.println("4.Change Pincode");
		System.out.println("5.Change Mail Id");
		System.out.println("6.Change Phone No");
		System.out.println("7.Change Category");
		System.out.println("8.Change Password");
		System.out.println("9.Go Back");
		System.out.print("\nEnter Your Option : ");
	}

	private void updateDishMenu(String sideDishName, boolean isSideDishPresent, boolean isDrink) {
		System.out.println("\n1.Change Name");
		System.out.println("2.Change Price");
		System.out.println("3.Change Quantity");
		if (isSideDishPresent) {
			System.out.println("4.Add " + sideDishName);
			System.out.println("5.Remove " + sideDishName);
			System.out.println("6.Go back");
		} else if (isDrink) {
			System.out.println("4.Change Temperature");
			System.out.println("5.Go Back");
		} else {
			System.out.println("4.Go back");
		}
		System.out.print("\nEnter your option : ");
	}

}
