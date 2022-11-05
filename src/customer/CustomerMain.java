package customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import utility.Register;
import utility.Validator;
import hotel.HotelDB;
import utility.Input;
import utility.Message;
import hotel.Hotel;
import utility.Order;

public class CustomerMain extends Register {

	CustomerDB cDB = CustomerDB.getInstance();
	HotelDB hDB = HotelDB.getInstance();

	public void main() {
		int choice;
		do {
			CustomerMenu.customerHomePage();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: { // register
				signup();
				break;
			}
			case 2: { // login
				login();
				break;
			}
			case 3: { // go back
				return;
			}
			default: {
				if (choice == -1)
					break;
				System.err.println(Message.INVALID_OPTION);

			}
			}

		} while (choice != 3);
	}

	@Override
	public boolean signup() {

		if (super.signup()) {
			if (super.newPassword()) {
				if (cDB.getCustomerList().containsKey(this.getMailId())) {
					System.out.println("\nUsername already exists in database....Try again with different username...");
					return false;
				}

				System.out.print("Enter UPI Id : ");
				int upiId = Input.getInteger(false);
				System.out.print("Enter UPI PIN : ");
				int upiPIN = Input.getInteger(false);

				String pincode = Integer.toString(getPincode());

				pincode = pincode.substring(0, 3);

				if (!hDB.isServiceable(pincode)) {
					System.out.println("There aren't any hotels near your area to serve you...");
					System.out.print("Do u still wish to create an account ( Y-1/ N-2 ) : ");
					int input = Input.getInteger(1, 2, false);
					if (input == -1)
						return false;
					if (input == 1) {
						cDB.addCustomer(new Customer(getName(), getPhoneNo(), getMailId(), getAddress(), getPincode(),
								getPassword(), upiId, upiPIN));
						System.out.println("\n\u001b[32m" + Message.ACCOUNT_CREATED + "\u001b[0m");

						this.login(getMailId(), getPassword());
					} else {
						System.out.println(
								"\nYour account is not created at the moment...please try again in the future....");
					}
				} else {
					cDB.addCustomer(new Customer(getName(), getPhoneNo(), getMailId(), getAddress(), getPincode(),
							getPassword(), upiId, upiPIN));
					System.out.println("\n\u001b[32m" + Message.ACCOUNT_CREATED + "\u001b[0m");

					this.login(getMailId(), getPassword());
				}

			}
		}
		return true;
	}

	@Override
	public void login() {
		super.login();
		login(this.getMailId(), this.getPassword());
	}

	private void login(String mailId, String password) {
		if (!cDB.getCustomerList().containsKey(mailId)) {
			System.out.println("\nUser Not Found...");
			return;
		}

		do {
			if (!cDB.getCustomerList().get(mailId).getPassword().equals(this.getPassword())) {
				System.out.println("\n" + Message.INCORRECT_PASSWORD);
				super.enterPassword(false);

			} else
				break;
		} while (!cDB.getCustomerList().get(mailId).getPassword().equals(this.getPassword()));

		successfulLogin(mailId);
	}

	private void successfulLogin(String mailId) {

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

	private void orderFood(Customer c) {
		Set<String> set = hDB.getHotelList().keySet();
		if (!set.isEmpty()) {
			System.out.println("\n----------------------------------------");
			System.out.println("Id\t\tName\t\tCategory");
			System.out.println("----------------------------------------");
		}
		boolean flag = true;

		for (String s : set) {
			if (hDB.getHotelList().get(s).getPin().equals(c.getPin())) {
				hDB.getHotelList().get(s).printHotel();
				flag = false;
			}
		}
		if (flag) {
			System.out.println("\nNo Hotels Were found near your area.....sorry");
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
			System.out.println("\nHotel Id Not Found.....Try again");
			return;
		}

		h.displayDishes();

		if (h.getDishes().isEmpty() && h.getPizzas().isEmpty() && h.getChicken().isEmpty() && h.getDrinks().isEmpty()) {
			System.out.println("\nThis hotels doesn't accept any orders at the moment....Try again later");
			return;
		}

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
			int dishId = Input.getInteger(1004, 9999, false);

			if (h.containsDishes(dishId)) {
				System.out.print("Enter Quantity : ");
				int quantity = Input.getInteger(false);
				dish.put(dishId, quantity);
				orders = h.getDishes().get(dishId).getName() + "\t" + quantity + "\n";
				orderList.put(dishId, orders);

			} else if (h.containsPizza(dishId)) {
				System.out.print("Enter Quantity : ");
				int quantity = Input.getInteger(false);
				orders = h.getPizzas().get(dishId).getName() + "\t" + quantity + "\n";
				Set<String> s = h.getPizzas().get(dishId).getToppings().keySet();
				for (String str : s) {
					System.out.println(str + "\t" + h.getPizzas().get(dishId).getToppings().get(str));
					System.out.print("Do you want this topping (press 1 for yes 2 for no) :");
					option = Input.getInteger(1, 2, false);
					if (option == 1) {
						toppings.add(str);
						orders += str + "\t" + h.getPizzas().get(dishId).getToppings().get(str) + "\n";
					}
				}
				orderList.put(dishId, orders);
				pizza.put(dishId, quantity);
			} else if (h.containsChicken(dishId)) {
				System.out.print("Enter Quantity : ");
				int quantity = Input.getInteger(false);
				orders = h.getChicken().get(dishId).getName() + "\t" + quantity + "\n";
				Set<String> s = h.getChicken().get(dishId).getExtras().keySet();
				for (String str : s) {
					System.out.println(str + "\t" + h.getChicken().get(dishId).getExtras().get(str));
					System.out.print("Do you want this sidedish (press 1 for yes 2 for no) :");
					option = Input.getInteger(1, 2, false);
					if (option == 1) {
						extras.add(str);
						orders += str + "\t" + h.getChicken().get(dishId).getExtras().get(str) + "\n";
					}
				}
				orderList.put(dishId, orders);
				chicken.put(dishId, quantity);
			} else if (h.containsDrinks(dishId)) {
				System.out.print("Enter Quantity : ");
				int quantity = Input.getInteger(false);
				orders = h.getDrinks().get(dishId).getName() + "\t" + quantity + "\n";
				orderList.put(dishId, orders);
				drinks.put(dishId, quantity);
			} else {
				System.out.println("\nInvalid dish Id.....Try again");
			}

			System.out.print("\nDo u want to order more (Press 1 for yes or 2 for exit): ");
			option = Input.getInteger(1, 2, false);
		} while (option == 1);

		if (!dish.isEmpty() || !pizza.isEmpty() || !chicken.isEmpty() || !drinks.isEmpty()) {
			Order order = new Order(dish, pizza, chicken, drinks, toppings, extras, orderList, c.getC_ID(), h.getH_ID(),
					h.getMailId(), c.getMailId());
			c.newOrder(order);
			System.out.println("\nFood Added to Cart");
			cart(c);
		}

	}

	private void cart(Customer c) {

		if (c.getPendingOrder().isEmpty()) {
			System.out.println("\nCart is Empty....Try again later...");
			return;
		}

		System.out.println("\n\t\t\t ---------------------------------------------");
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
		System.out.print("Enter Order Id : ");
		int orderId;
		do {
			orderId = Input.getInteger(1000, 9999, false);
			if (!s.contains(orderId)) {
				System.out.println("\nInvalid order id....try again");
			}
		} while (!s.contains(orderId));

		System.out.println("\n1.Confirm Order");
		System.out.println("2.Cancel Order");
		System.out.println("3.Go Back");
		System.out.print("Enter Option : ");

		int option = Input.getInteger(1, 3, false);
		if (option == -1)
			return;
		if (option == 2) {
			String str = c.getPendingOrder().get(orderId).toString() + "\nOrder Status : Cancelled\n"
					+ Input.getDateTime();
			c.addCompletedOrder(orderId, str);
			c.getPendingOrder().remove(orderId);
			System.out.println("\nOrder cancelled successfully");
			return;
		} else if (option == 1) {
			String hMail = c.getPendingOrder().get(orderId).getHotelMail();
			double price = hDB.getHotel(hMail).calculatePrice(c.getPendingOrder().get(orderId));
			c.getPendingOrder().get(orderId).setPrice(price);
			System.out.println("Total Price : Rs." + price);
			System.out.print("Enter UPI Id : ");
			int upiId = Input.getInteger(false);
			System.out.print("Enter UPI PIN : ");
			int upiPin = Input.getInteger(false);
			if (c.getUPI().getUPI_ID() == upiId && c.getUPI().getUpiPin() == upiPin) {
				if (c.getUPI().getBalance() < price) {
					System.out.println("\nInsufficient Balance...Try again later");
					return;
				}
				hDB.getHotel(hMail).newOrder(c.getPendingOrder().get(orderId));
				c.getUPI().payMoney(price);
				String str = c.getPendingOrder().get(orderId).toString() + "\nOrder Status : Paid\n"
						+ Input.getDateTime();
				c.addCompletedOrder(orderId, str);
				c.getPendingOrder().remove(orderId);
				System.out.println("\nPaid successfully...");
			} else {
				System.out.println("\nInvalid Credentials...");
			}
		} else if (option == 3)
			return;

	}

	private void trackOrder(Customer c) {
		System.out.print("Enter Order Id : ");
		int orderId = Input.getInteger(1000, 9999, false);
		if (!c.getPendingOrder().containsKey(orderId) && !c.getCompletedOrders().containsKey(orderId)) {
			System.out.println("\nInvalid order id");
		}
		if (c.getPendingOrder().containsKey(orderId))
			System.out.print(c.getPendingOrder().get(orderId));
		else if (c.getCompletedOrders().containsKey(orderId))
			System.out.print(c.getCompletedOrders().get(orderId));
		System.out.print("Press 1 to track order again or 2 to go back : ");
		int option = Input.getInteger(1, 2, false);
		if (option == 1)
			trackOrder(c);
		if (option == 2)
			return;
	}

	private void viewRecentOrders(Customer c) {
		if (c.getCompletedOrders().isEmpty()) {
			System.out.println("\nNo orders were delivered...Try again later");
			return;
		}
		Set<Integer> set = c.getCompletedOrders().keySet();
		for (int orderId : set) {
			System.out.println(c.getCompletedOrders().get(orderId));
		}
	}

	private void editAccount(Customer c) {
		int choice;

		do {
			CustomerMenu.accountSettings();
			choice = Input.getInteger(false);
			switch (choice) {
			case 1: {
				System.out.print("Enter New Name : ");
				String name = Input.getString(true, false);
				if (name == null)
					break;

				c.setName(name);
				System.out.println("\n\u001b[32m" + "Name Changed Successfully" + "\u001b[0m");
				break;
			}
			case 2: {
				System.out.print("Enter New Address : ");
				String address = Input.getString(false, false);
				if (address == null)
					break;
				c.setAddress(address);
				System.out.println("\n\u001b[32m" + "Address Changed Successfully" + "\u001b[0m");
				break;
			}
			case 3: {
				System.out.print("Enter New Pincode : ");
				int pinCode = Input.getInteger(600000, 650000, false);

				if (pinCode == -1)
					break;

				c.setPincode(pinCode);
				System.out.println("\n\u001b[32m" + "Pincode Changed Successfully" + "\u001b[0m");
				break;
			}
			case 4: {

				System.out.print("Enter Mail Id : ");
				String mailId;
				do {
					mailId = Input.getString(false, false);
					if (!Validator.validateMailId(mailId)) {
						System.out.print("Enter Valid Mail Id : ");
					}
				} while (!Validator.validateMailId(mailId));
				if (cDB.getCustomerList().containsKey(mailId)) {
					System.out.println("\nUsername already exists....try again with different username");
					break;
				}
				String oldMailId = c.getMailId();
				if (mailId == null)
					break;
				c.setMailId(mailId);
				cDB.addCustomer(c);
				cDB.removeCustomer(oldMailId);
				;
				System.out.println("\n\u001b[32m" + "Mail Id Changed Successfully" + "\u001b[0m");
				break;
			}
			case 5: {
				System.out.print("Enter Phone No : ");
				String mobile;
				do {
					mobile = Input.getString(false, false);
					if (!Validator.validateMobileNo(mobile))
						System.out.print("Enter Valid Mobile No : ");
				} while (!Validator.validateMobileNo(mobile));
				c.setPhoneNo(mobile);
				System.out.println("\n\u001b[32m" + "Mobile No Changed Successfully" + "\u001b[0m");
				break;
			}
			case 6: {
				System.out.print("Enter Old Password : ");
				String pass = Input.getString(false, false);
				if (!pass.equals(c.getPassword())) {
					System.err.println(Message.INCORRECT_PASSWORD);
					break;
				}
				if (newPassword()) {
					c.setPassword(this.getPassword());
					System.out.println("\n\u001b[32m" + "Password Changed Successfully" + "\u001b[0m");
				}
				break;
			}
			case 7: {
				return;
			}
			default: {
				System.out.println("\n" + Message.INVALID_OPTION);
			}
			}
		} while (choice != 7);
	}

}
