package customer;

import java.util.Set;
import java.util.HashMap;

import hotel.Hotel;
import hotel.HotelDB;

import utility.Data;
import utility.Input;
import utility.Order;
import utility.Register;

public class CustomerMain extends Register{

	private static final CustomerDB CUSTOMER_DB = CustomerDB.getInstance();

	@Override
	public void mainMenu() {
		System.out.println("\n\t\t\t ---------------------------------------------");
		System.out.println("\t\t\t|                    Customer                 |");
		System.out.println("\t\t\t ---------------------------------------------");
		int choice;
		do {
			homeScreen();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {
				String mailId = super.signup(false);
				if (!mailId.equals(""))
					successfulLogin(mailId);
				break;
			}
			case 2: {
				String mailId = super.login(false);
				if (!mailId.equals(""))
					successfulLogin(mailId);
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

	@Override
	public void successfulLogin(String mailId) {

		Customer customer = CUSTOMER_DB.getCustomerList().get(mailId);
		System.out.println("\n....Welcome Mr." + customer.getName() + "....\n");
		int choice;
		do {

			loginScreen();
			choice = Input.getInteger(false);
			switch (choice) {
			case 1: {
				orderFood(customer);
				break;
			}
			case 2: {
				cart(customer);
				break;
			}
			case 3: {
				trackOrder(customer);
				break;
			}
			case 4: {
				orderHistory(customer);
				break;
			}
			case 5: {
				editAccount(customer);
				break;
			}
			case 6: {
				return;
			}
			default: {
				System.out.println("Invalid Option...Try again");
			}
			}

		} while (true);
	}

	private void orderFood(Customer customer) {

		HotelDB hotelDB = HotelDB.getInstance();
		if (!hotelDB.isServiceable(customer.getPincode())) {
			System.out.println("\nNo Hotels Were found near your area to serve you.....sorry");
			return;
		}

		Set<String> set = hotelDB.getHotelList().keySet();
		if (!set.isEmpty()) {
			System.out.println("\n----------------------------------------");
			System.out.println("Id\t\tName\t\tCategory");
			System.out.println("----------------------------------------");
		}

		HashMap<String, Hotel> hotelList = new HashMap<>(); // hotels with same pincodes (mailId,hotel)

		for (String mailId : set) {
			if (hotelDB.getHotelList().get(mailId).isHotelServiceable(customer.getPincode())) {
				hotelList.put(mailId, hotelDB.getHotelList().get(mailId));
				hotelList.get(mailId).printHotel();
			}
		}

		System.out.print("\nEnter Hotel Id : ");

		int hotelId = Input.getInteger(true);

		if (hotelId == -1)
			return;

		Hotel hotel = null;

		boolean isIdFound = false;

		set = hotelList.keySet();
		for (String mailId : set) {
			if (hotelList.get(mailId).getHotelId() == hotelId) {
				hotel = hotelDB.getHotelList().get(mailId);
				isIdFound = true;
				break;
			}
		}
		if (!isIdFound) {
			System.out.println("\nHotel Id Not Found.....Try again");
			return;
		}

		if (hotel.getDishes().isEmpty() && hotel.getDrinks().isEmpty()) {
			System.out.println("\nThis hotels doesn't accept any orders at the moment....Try again later");
			return;
		}

		hotel.displayDishes(false);

		HashMap<Integer, Integer> dishes = new HashMap<>(); // dishId,quantity
		HashMap<Integer, Integer> drinks = new HashMap<>(); // dishId,quantity

		HashMap<Integer, HashMap<String, Double>> extras = new HashMap<>(); // dishId,extras<dishId,price>

		int option;

		do {

			System.out.print("\nEnter dish ID : ");
			int dishId = Input.getInteger(1000, 9999, false);

			boolean isDish = hotel.getDishes().containsKey(dishId);

			if (isDish || hotel.getDrinks().containsKey(dishId)) {
				System.out.print("Enter Quantity : ");
				int quantity;

				do {
					quantity = Input.getInteger(false);
					if (quantity == 0)
						System.out.print("Enter valid quantity : ");
				} while (quantity == 0);

				if (isDish) {
					dishes.put(dishId, quantity);

					set = hotel.getDishes().get(dishId).getExtras().keySet();
					if (!set.isEmpty()) {
						HashMap<String, Double> extra = new HashMap<>();

						for (String extrasName : set) {
							System.out.println(
									extrasName + "\t\t" + hotel.getDishes().get(dishId).getExtras().get(extrasName));
							System.out.print(
									"Do you want this " + hotel.getDishes().get(dishId).getSideDishName() + " (1-Y/2-N) :");
							option = Input.getInteger(1, 2, false);

							if (option == 1)
								extra.put(extrasName, hotel.getDishes().get(dishId).getExtras().get(extrasName));

						}
						extras.put(dishId, extra);
					}
				} else {
					drinks.put(dishId, quantity);
				}

			} else {
				System.out.println("\nInvalid dish Id.....Try again");
			}

			System.out.print("\nDo u want to order more (Press (1-Y / 2-N) : ");
			option = Input.getInteger(1, 2, true);
		} while (option == 1);

		Order order = new Order(dishes, drinks, extras, customer.getMailId(), hotel.getMailId());
		customer.newOrder(order);
		cart(customer);

	}

	private void cart(Customer customer) {
		if (customer.getPendingOrders().isEmpty()) {
			System.out.println("\nCart is Empty....Try again later...");
			return;
		}

		System.out.println("\n\t\t\t ---------------------------------------------");
		System.out.println("\t\t\t|                     Cart                    |");
		System.out.println("\t\t\t ---------------------------------------------");

		Set<Integer> set = customer.getPendingOrders().keySet();
		for (int orderId : set) {
			System.out.println("----------------------------------------");
			System.out.println(customer.getPendingOrders().get(orderId).getOrderDetails(false));
			System.out.println("----------------------------------------");
		}

		int orderId;
		Order order;
		int attempt = 1;

		System.out.print("Enter Order Id : ");
		do {
			orderId = Input.getInteger(true);
			if (!set.contains(orderId)) {
				if (orderId == -1 || Input.isAttemptExceeded(true, attempt++))
					return;
				System.out.print("Invalid order Id....Enter again : ");
			} else {
				order = customer.getPendingOrders().get(orderId);
				break;
			}
		} while (true);

		System.out.println("\n1.Confirm Order");
		System.out.println("2.Cancel Order");
		System.out.println("3.Go Back");
		System.out.print("Enter Option : ");

		int option = Input.getInteger(1, 3, false);

		if (option == 3)
			return;

		String orderDetail ;

		if (option == 2) {
			orderDetail = order.getOrderDetails(false) + "\nOrder Status : Cancelled\n" + Input.getDateTime();
			customer.addCompletedOrder(orderId, orderDetail);
			System.out.println("\nOrder cancelled successfully\n");
			return;
		}

		double price = order.calculatePrice();
		System.out.println("Total Price : Rs." + price);
		order.setPrice(price);

		System.out.print("\nPress 1 for proceed to payment or 2 for exit : ");
		int choice = Input.getInteger(1, 2, false);

		if (choice == 2)
			return;

		System.out.print("\nEnter UPI Id : ");
		int upiId = Input.getInteger(true);
		if (upiId == -1)
			return;

		System.out.print("Enter UPI PIN : ");
		int upiPin = Input.getInteger(true);
		if (upiPin == -1)
			return;

		if (customer.getUPI().getUPI_ID() == upiId && customer.getUPI().getUpiPin() == upiPin) {
			if (customer.getUPI().getBalance() < price) {
				System.out.println("\nInsufficient Balance...Try again later\n");
				return;
			}

			HotelDB.getInstance().getHotelList().get(order.getHotelMail()).newOrder(order);

			customer.getUPI().payMoney(price);
			orderDetail = order.getOrderDetails(false) + "\nOrder Status : Paid\n" + Input.getDateTime();
			customer.addCompletedOrder(orderId, orderDetail);
			System.out.println("\nPaid successfully...Your order will be delivered soon...\n");
		} else {
			System.out.println("\nInvalid Credentials...\n");
		}

	}

	private void trackOrder(Customer customer) {
		System.out.print("\nEnter Order Id : ");

		int orderId = Input.getInteger(true);
		if (orderId == -1)
			return;

		if (customer.getCompletedOrders().containsKey(orderId))
			System.out.println("\n" + customer.getCompletedOrders().get(orderId));
		else
			System.out.println("\nInvalid order id\n");

		System.out.print("Press 1 to track another order or 2 to go back : ");

		int option = Input.getInteger(1, 2, true);

		if (option == 1)
			trackOrder(customer);

	}

	@Override
	public void orderHistory(Data data) {
		Customer customer= (Customer) data;
		if (customer.getCompletedOrders().isEmpty()) {
			System.out.println("\nNo orders were delivered...Try again later\n");
			return;
		}
		System.out.println();
		Set<Integer> set = customer.getCompletedOrders().keySet();
		for (int orderId : set) {
			System.out.println(customer.getCompletedOrders().get(orderId));
		}
	}

	@Override
	public void editAccount(Data d) {

		Customer c=(Customer)d;

		int choice;
		do {

			accountSettingsScreen();
			choice = Input.getInteger(false);
			switch (choice) {
			case 1: {
				String name = super.newName(true, false, true);
				if (name.equals(""))
					break;
				c.setName(name);
				System.out.println("\n\u001b[36m" + "Name Changed Successfully" + "\u001b[0m");
				break;
			}
			case 2: {
				String address = super.newAddress(true);
				if (address.equals(""))
					break;
				c.setAddress(address);
				System.out.println("\n\u001b[36m" + "Address Changed Successfully" + "\u001b[0m");
				break;
			}
			case 3: {
				int pincode = super.newPincode(true);
				if (pincode == -1)
					break;

				c.setPincode(pincode);

				System.out.println("\n\u001b[36m" + "Pincode Changed Successfully" + "\u001b[0m");

				break;
			}
			case 4: {
				String mailId = super.newMailId(true, true);
				if (mailId.equals(""))
					break;
				CUSTOMER_DB.removeCustomer(c.getMailId());
				c.setMailId(mailId);
				CUSTOMER_DB.addCustomer(c);
				System.out.println("\n\u001b[36m" + "Mail Id Changed Successfully" + "\u001b[0m");
				break;
			}
			case 5: {
				String phoneNo = super.newPhoneNo(true);
				if (phoneNo.equals(""))
					break;

				c.setPhoneNo(phoneNo);
				System.out.println("\n\u001b[36m" + "Phone No Changed Successfully" + "\u001b[0m");
				break;
			}

			case 6: {
				System.out.print("Enter Old Password : ");
				String password = Input.getString(false, false);
				if (password.equals(c.getPassword())) {

					String newPassword;
					if (!(newPassword = super.newPassword(true)).equals("")) {
						c.setPassword(newPassword);
						System.out.println("\n\u001b[36m" + "Password Changed Successfully" + "\u001b[0m");
					}

				} else {
					System.out.println("\nIncorrect Password");
				}
				break;
			}
			case 7: {
				return;
			}
			default: {
				System.out.println("\nInvalid Option...Try again....\n");
			}
			}
		} while (true);

	}

	@Override
	public void loginScreen() {
		System.out.println("\n1.Order Food");
		System.out.println("2.View Cart");
		System.out.println("3.Track Order Status");
		System.out.println("4.View Recent Orders");
		System.out.println("5.Account Settings");
		System.out.println("6.Logout");
		System.out.print("\nEnter Your Option : ");
	}

	@Override
	public void accountSettingsScreen() {
		System.out.println("\n1.Change Name");
		System.out.println("2.Change Address");
		System.out.println("3.Change Pincode");
		System.out.println("4.Change Mail Id");
		System.out.println("5.Change Phone No");
		System.out.println("6.Change Password");
		System.out.println("7.Go Back");
		System.out.print("\nEnter Your Option : ");
	}

}
