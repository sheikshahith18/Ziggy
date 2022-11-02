package customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import utility.Register;
import hotel.HotelDB;
import utility.Input;
import utility.Message;
import hotel.Hotel;
import utility.Order;

public class CustomerMain extends Register {

	CustomerDB cDB = CustomerDB.getInstance();
	HotelDB hDB = HotelDB.getInstance();

	@Override
	public void login() {
		super.login();
		login(this.getMailId(), this.getPassword());
	}

	void login(String mailId, String password) {
		if (!cDB.getCustomerList().containsKey(mailId)) {
			System.out.print("User Not Found...");
			Input.keepScreen();
			return;
		}

		int attempt = 1;
		do {
			if (!cDB.getCustomerList().get(mailId).getPassword().equals(this.getPassword())) {
				System.out.println(Message.INCORRECT_PASSWORD);
				super.enterPassword(false);

				if (this.getPassword() == null)
					return;
			} else
				break;
		} while (attempt++ < 3);

		if (attempt > 3) {
			System.out.print(Message.TOO_MANY_ATTEMPTS);
			Input.keepScreen();
			return;
		}

		successfulLogin(mailId);
	}

	void successfulLogin(String mailId) {

		Customer c = cDB.getCustomerList().get(mailId);
		System.out.println("\n....Welcome Mr." + c.getName() + "....\n");
		int choice;
		do {

			CustomerMenu.customerLoginMenu();
			choice = Input.getInteger(false);
			switch (choice) {
			case 1: {
				orderFood(c);
				break;
			}
			case 2: {
				cart(c);
				break;
			}
			case 3: {
				trackOrder(c);
				break;
			}
			case 4: {
				viewRecentOrders(c);
				break;
			}
			case 5: {
				editAccount(c);
				break;
			}
			case 6: {
				return;
			}
			}

		} while (choice != 6);
	}

	void orderFood(Customer c) {
		Set<String> set = hDB.getHotelList().keySet();
		boolean flag = true;
		System.out.println();
		for (String s : set) {
			if (hDB.getHotelList().get(s).getPin().equals(c.getPin())) {
				hDB.getHotelList().get(s).printHotel();
				flag = false;
			}
		}
		if (flag) {
			System.out.print("No Hotels Were found near your area.....sorry");
			Input.keepScreen();
			Input.keepScreen();
			return;
		}

		System.out.print("Enter Hotel Id : ");

		int hId = Input.getInteger(true);
		Hotel h = null;
		flag = true;
		for (String s : set) {
			if (hDB.getHotelList().get(s).getPin().equals(c.getPin()) && hDB.getHotelList().get(s).getH_ID() == hId) {
				h = hDB.getHotelList().get(s);
				flag = false;
				break;
			}
		}
		if (flag) {
			System.out.println("Hotel Id Not Found.....Try again");
			Input.keepScreen();
			return;
		}

		h.displayDishes();

		HashMap<Integer, Integer> dish = new HashMap<>();
		HashMap<Integer, Integer> pizza = new HashMap<>();
		HashMap<Integer, Integer> chicken = new HashMap<>();
		HashMap<Integer, Integer> drinks = new HashMap<>();
		ArrayList<String> toppings = new ArrayList<>();
		ArrayList<String> extras = new ArrayList<>();
		int option;
		HashMap<Integer, String> orderList = new HashMap<>();
		String orders = "";
		do {
			System.out.print("\nEnter dish ID : ");
			int dishId = Input.getInteger(1004, 9999);

			if (h.containsDishes(dishId)) {
				System.out.print("Enter Quantity : ");
				int quantity = Input.getInteger(1, h.getDishes().get(dishId).getQuantity());
				dish.put(dishId, quantity);
				orders = h.getDishes().get(dishId).getName() + "\t" + quantity + "\n";
				orderList.put(dishId, orders);

			} else if (h.containsPizza(dishId)) {
				System.out.print("Enter Quantity : ");
				int quantity = Input.getInteger(1, h.getPizzas().get(dishId).getQuantity());
				orders = h.getPizzas().get(dishId).getName() + "\t" + quantity + "\n";
				Set<String> s = h.getPizzas().get(dishId).getToppings().keySet();
				for (String str : s) {
					System.out.println(str + "\t" + h.getPizzas().get(dishId).getToppings().get(str));
					System.out.println("Do you want this topping (press 1 for yes 2 for no) :");
					option = Input.getInteger(1, 2);
					if (option == 1) {
						toppings.add(str);
						orders += str + "\t" + h.getPizzas().get(dishId).getToppings().get(str) + "\n";
					}
				}
				orderList.put(dishId, orders);
				pizza.put(dishId, quantity);
			} else if (h.containsChicken(dishId)) {
				System.out.print("Enter Quantity : ");
				int quantity = Input.getInteger(1, h.getChicken().get(dishId).getQuantity());
				orders = h.getChicken().get(dishId).getName() + "\t" + quantity + "\n";
				Set<String> s = h.getChicken().get(dishId).getExtras().keySet();
				for (String str : s) {
					System.out.println(str + "\t" + h.getChicken().get(dishId).getExtras().get(str));
					System.out.println("Do you want this sidedish (press 1 for yes 2 for no) :");
					option = Input.getInteger(1, 2);
					if (option == 1) {
						extras.add(str);
						orders += str + "\t" + h.getChicken().get(dishId).getExtras().get(str) + "\n";
					}
				}
				orderList.put(dishId, orders);
				chicken.put(dishId, quantity);
			} else if (h.containsDrinks(dishId)) {
				System.out.print("Enter Quantity : ");
				int quantity = Input.getInteger(1, h.getDrinks().get(dishId).getQuantity());
				orders = h.getDrinks().get(dishId).getName() + "\t" + quantity + "\n";
				orderList.put(dishId, orders);
				drinks.put(dishId, quantity);
			} else {
				System.out.print("Invalid dish Id.....Try again");
				Input.keepScreen();
				Input.keepScreen();
			}

			System.out.println("Do u want to order more (Press 1 for yes or 2 for exit): ");
			option = Input.getInteger(1, 2);
		} while (option == 1);

		if (!dish.isEmpty() || !pizza.isEmpty() || !chicken.isEmpty() || !drinks.isEmpty()) {
			Order order = new Order(dish, pizza, chicken, drinks, toppings, extras, orderList, c.getC_ID(), h.getH_ID(),
					h.getMailId(), c.getMailId());
			c.newOrder(order);
			System.out.println("Food Added to Cart");
			cart(c);
		}

	}

	void cart(Customer c) {

		if (c.getPendingOrder().isEmpty()) {
			System.out.println("Cart is Empty....Try again later...");
			return;
		}

		System.out.println("\t\t\t ---------------------------------------------");
		System.out.println("\t\t\t|                     Cart                    |");
		System.out.println("\t\t\t ---------------------------------------------");

		Set<Integer> s = c.getPendingOrder().keySet();

		for (Integer orderId : s) {
			System.out.println("Order Id : " + orderId);
			Set<Integer> set = c.getPendingOrder().get(orderId).getOrderList().keySet();
			for (Integer dishId : set) {
				System.out.println(dishId + "\t" + c.getPendingOrder().get(orderId).getOrderList().get(dishId));
			}
		}
		System.out.println("Enter Order Id : ");
		int orderId, cnt = 0;
		do {
			orderId = Input.getInteger(1000, 9999);
			if (!s.contains(orderId)) {
				System.out.println("Invalid order id....try again");
			}
		} while (!s.contains(orderId) && cnt++ < 4);
		if (cnt > 4) {
			System.out.println(Message.TOO_MANY_ATTEMPTS);
			return;
		}

		System.out.println("1.Confirm Order");
		System.out.println("2.Cancel Order");
		System.out.print("Enter Option : ");

		int option = Input.getInteger(1, 2);
		if (option == -1)
			return;
		if (option == 2) {
			String str = c.getPendingOrder().get(orderId).toString() + "\nOrder Status : Cancelled\n"
					+ Input.getDateTime();
			c.addCompletedOrder(orderId,str);
			c.getPendingOrder().remove(orderId);
			System.out.println("Order cancelled successfully");
			return;
		}
		if (option == 1) {
			String hMail = c.getPendingOrder().get(orderId).getHotelMail();
			double price = hDB.getHotel(hMail).calculatePrice(c.getPendingOrder().get(orderId));
			c.getPendingOrder().get(orderId).setPrice(price);
			System.out.println("Total Price : Rs." + price);
			System.out.print("Enter UPI Id : ");
			int upiId = Input.getInteger(false);
			System.out.print("Enter UPI PIN : ");
			int upiPin = Input.getInteger(false);
			if (c.getUPI().getUPI_ID() == upiId && c.getUPI().getUpiPin() == upiPin) {
				hDB.getHotel(hMail).newOrder(c.getPendingOrder().get(orderId));
				c.getUPI().payMoney(price);
				String str = c.getPendingOrder().get(orderId).toString() + "\nOrder Status : Paid\n"
						+ Input.getDateTime();
				c.addCompletedOrder(orderId,str);
				c.getPendingOrder().remove(orderId);
				System.out.println("Paid successfully...");
				Input.keepScreen();
			} else {
				System.out.println("Invalid Credentials...");
				Input.keepScreen();
			}
		}

	}

	void trackOrder(Customer c) {
		System.out.print("Enter Order Id : ");
		int orderId = Input.getInteger(1000, 9999);
		if (!c.getPendingOrder().containsKey(orderId) && !c.getCompletedOrders().containsKey(orderId)) {
			System.out.println("Invalid order id");
			return;
		}
		if(c.getPendingOrder().containsKey(orderId))
			System.out.print(c.getPendingOrder().get(orderId));
		else
			System.out.print(c.getCompletedOrders().get(orderId));
		Input.keepScreen();
		System.out.println("Press 1 to track order again or 2 to go back");
		int option = Input.getInteger(1, 2);
		if (option == 1)
			trackOrder(c);
		if (option == 2)
			return;
	}

	void viewRecentOrders(Customer c) {
		if (c.getCompletedOrders().isEmpty()) {
			System.out.println("No orders were delivered...Try again later");
			Input.keepScreen();
			Input.keepScreen();
			return;
		}
		Set<Integer> set=c.getCompletedOrders().keySet();
		for (int orderId:set) {
			System.out.println(c.getCompletedOrders().get(orderId));
		}
	}

	@Override
	public boolean signup() {

		if (super.signup()) {
			if (super.newPassword()) {
				if (cDB.getCustomerList().containsKey(this.getMailId())) {
					System.out.print("Username already exists in database....Try again with different username...");
					Input.keepScreen();
					return false;
				}

				System.out.print("Enter UPI Id : ");
				int upiId = Input.getInteger(false);
				System.out.print("Enter UPI PIN : ");
				int upiPIN = Input.getInteger(false);

				String pincode = Integer.toString(getPincode());

				pincode = pincode.substring(0, 3);

				if (!hDB.isServiceable(pincode)) {
					System.out.println("There aren't any hotels at the moment to fill your stomach");
					System.out.print("Do u still wish to create an account ( Y-1/ N-2 )");
					int input = Input.getInteger(1, 2);
					if (input == -1)
						return false;
					if (input == 1) {
						cDB.addCustomer(new Customer(getName(), getPhoneNo(), getMailId(), getAddress(),
								getPincode(), getPassword(), upiId, upiPIN));
						System.out.print(Message.ACCOUNT_CREATED + "asdasdasd");
						Input.keepScreen();
						this.login(getMailId(), getPassword());
					} else {
						System.out.print(
								"Your account is not created at the moment...please try again in the future....");
						Input.keepScreen();
					}
				} else {
					cDB.addCustomer(new Customer(getName(), getPhoneNo(), getMailId(), getAddress(),
							getPincode(), getPassword(), upiId, upiPIN));
					System.out.print(Message.ACCOUNT_CREATED);
					Input.keepScreen();
					Input.keepScreen();
					this.login(getMailId(), getPassword());
				}

			}
		}
		return true;
	}

	void editAccount(Customer c) {
		int choice;

		do {
			CustomerMenu.accountSettings();
			choice = Input.getInteger(false);
			switch (choice) {
			case 1: {
				System.out.println("Enter New Name : ");
				String name = Input.getString(true);
				if (name == null)
					break;
				c.setName(name);
				break;
			}
			case 2: {
				System.out.println("Enter New Address : ");
				String address = Input.getString(false);
				if (address == null)
					break;
				c.setAddress(address);
				break;
			}
			case 3: {
				System.out.println("Enter New Pincode : ");
				int pinCode = Input.getInteger(600000, 650000);

				if (pinCode == -1)
					break;

				c.setPincode(pinCode);
				break;
			}
			case 4: {
				System.out.println("Enter Mail Id : ");
				String mailId = Input.getString(false);
				if (mailId == null)
					break;
				c.setMailId(mailId);
				break;
			}
			case 5: {
				System.out.println("Enter Phone No : ");
				long phoneNo = Input.getPhoneNumber();
				if (phoneNo == -1)
					return;
				c.setPhoneNo(phoneNo);
			}
			case 6: {
				if (newPassword()) {
					c.setPassword(this.getPassword());
					System.out.println("Password changed successfully...");
				}
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

	public void main() {
		int choice;
		do {
			CustomerMenu.customerHomePage();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {// register
				signup();
				break;
			}
			case 2: {// login
				login();
				break;
			}
			case 3: {// go back
				return;
			}
			default: {
				if (choice == -1)
					break;
				System.err.println(Message.INVALID_OPTION);
				Input.keepScreen();
				Input.keepScreen();

			}
			}

		} while (choice != 3);
	}

}
