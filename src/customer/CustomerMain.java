package customer;

import java.util.Set;
import java.util.HashMap;

import hotel.Hotel;
import hotel.HotelDB;

import utility.Input;
import utility.Order;
import utility.Register;
import utility.Validator;

public class CustomerMain extends Register {

	CustomerDB cDB = CustomerDB.getInstance();
	HotelDB hDB = HotelDB.getInstance();

	public void main() {
		int choice;
		do {
			customerHomePage();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {
				signup();
				break;
			}
			case 2: {
				login();
				break;
			}
			case 3: {
				return;
			}
			default: {
				System.out.println("\nInvalid Option...Try again....\n");

			}
			
			}

		} while (choice != 3);
	}

	private boolean signup() {

		if (super.signup(false)) {
			if (super.newPassword()) {
				if (cDB.getCustomerList().containsKey(this.getMailId())) {
					System.out.println("\nUsername already exists in database....Try again with different username...");
					return false;
				}

				String pincode = Integer.toString(getPincode());

				pincode = pincode.substring(0, 3);

				if (!hDB.isServiceable(pincode)) {
					System.out.println("There aren't any hotels near your area to serve you...");
					System.out.print("Do u still wish to create an account ( Y-1/ N-2 ) : ");

					int input = Input.getInteger(1, 2, false);

					if (input == 2) {
						System.out.println(
								"\nYour account is not created at the moment...please try again in the future....\n");
						return false;
					}
				}
				cDB.addCustomer(
						new Customer(getName(), getPhoneNo(), getMailId(), getAddress(), getPincode(), getPassword()));
				this.login(getMailId(), getPassword());

			}
		}
		return true;
	}

	@Override
	protected void login() {
		super.login();
		login(this.getMailId(), this.getPassword());
	}

	private void login(String mailId, String password) {
		if (!cDB.getCustomerList().containsKey(mailId)) {
			System.out.println("\nUser Not Found...");
			return;
		}

		int attempt = 1;
		do {
			if (!cDB.getCustomerList().get(mailId).getPassword().equals(this.getPassword())) {
				System.out.println("\nIncorrect Password\n");
				if (attempt++ > 2) {
					System.out.print("\nToo many unsuccessful attempts.....Do u want to try again (1-Y/2-N) : ");
					int option = Input.getInteger(1, 2, true);
					if (option == 1)
						attempt = 1;
					else if (option == 2 || option == -1)
						return;
				}
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

			customerLoginMenu();
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
			case 7:{
				c.print();
				break;
			}
			default: {
				System.out.println("Invalid Option...Try again");
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

		HashMap <String,Hotel> hotelList=new HashMap<>();   // hotels with same pincodes

		for (String mailId : set) {
			if (hDB.getHotelList().get(mailId).getPin().equals(c.getPin())) {
				hDB.getHotelList().get(mailId).printHotel();
				hotelList.put(mailId,hDB.getHotel(mailId));
			}
		}
		if (hotelList.isEmpty()) {
			System.out.println("\nNo Hotels Were found near your area to serve you.....sorry");
			return;
		}

		System.out.print("\nEnter Hotel Id : ");

		int hId = Input.getInteger(true);
		
		if (hId == -1)
			return;
		
		Hotel h = null;

		boolean flag = true;

		set=hotelList.keySet();
		for (String mailId : set) {
			if(hotelList.get(mailId).getH_ID()==hId) {
				h = hDB.getHotelList().get(mailId);				
				flag = false;
				break;
			}
		}
		if (flag) {
			System.out.println("\nHotel Id Not Found.....Try again");
			return;
		}

		h.displayDishes(false);

		if (h.getDishes().isEmpty() && h.getDrinks().isEmpty()) {
			System.out.println("\nThis hotels doesn't accept any orders at the moment....Try again later");
			return;
		}

		HashMap<Integer, Integer> dishes = new HashMap<>();
		HashMap<Integer, Integer> drinks = new HashMap<>();

		HashMap<Integer, HashMap<String, Double>> extras = new HashMap<>();

		int option;
		HashMap<Integer, String> orderList = new HashMap<>(); // dishId, dish details
		String orderDetails = "";
		do {

			System.out.print("\nEnter dish ID : ");
			int dishId = Input.getInteger(1000, 9999, false);

			boolean isDish = h.getDishes().containsKey(dishId) ? true : false;
			boolean isDrink = h.getDrinks().containsKey(dishId) ? true : false;

			if (isDish || isDrink) {
				System.out.print("Enter Quantity : ");
				int quantity = Input.getInteger(false);

				String dishName = "";
				double dishPrice = 0;

				if (isDish) {
					dishName = h.getDishes().get(dishId).getName();
					dishPrice = h.getDishes().get(dishId).getPrice();
				}

				if (isDish && !h.getDishes().get(dishId).getSideDishName().equals("")) {
					HashMap<String, Double> extra = new HashMap<>();
					set = h.getDishes().get(dishId).getExtras().keySet();
					orderDetails = dishName + "\t" + quantity + "\t" + dishPrice + "\n";

					for (String extrasName : set) {
						System.out.println(extrasName + "\t\t" + h.getDishes().get(dishId).getExtras().get(extrasName));
						System.out.print(
								"Do you want this " + h.getDishes().get(dishId).getSideDishName() + " (1-Y/2-N) :");
						option = Input.getInteger(1, 2, false);
						if (option == 1) {
							extra.put(extrasName, h.getDishes().get(dishId).getExtras().get(extrasName));
							orderDetails += extrasName + "\t" + h.getDishes().get(dishId).getExtras().get(extrasName) + "\n";
						}
					}
					HashMap<String, Double> newExtra = new HashMap<>();
					newExtra.putAll(extra);
					extra.clear();
					extras.put(dishId, newExtra);
					dishes.put(dishId, quantity);
					orderList.put(dishId, orderDetails);
				} else if (isDrink) {
					dishName = h.getDrinks().get(dishId).getName();
					dishPrice = h.getDrinks().get(dishId).getPrice();

					drinks.put(dishId, quantity);
					orderDetails = dishName + "\t" + quantity + "\t" + dishPrice + "\n";
					orderList.put(dishId, orderDetails);
				} else {
					orderDetails = dishName + "\t" + quantity + "\t" + dishPrice + "\n";
					orderList.put(dishId, orderDetails);
				}

			} else {
				System.out.println("\nInvalid dish Id.....Try again");
			}

			System.out.print("\nDo u want to order more (Press (1-Y / 2-N) : ");
			option = Input.getInteger(1, 2, true);
		} while (option == 1);

		if (!dishes.isEmpty() || !drinks.isEmpty()) {
			Order order = new Order(dishes, drinks, extras, orderList, c.getMailId(), h.getMailId());
			c.newOrder(order);
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
			System.out.println("Order Id : " + orderId + "\n");
			Set<Integer> set = c.getPendingOrder().get(orderId).getOrderList().keySet();
			for (Integer dishId : set) {
				System.out.println(dishId + "\t" + c.getPendingOrder().get(orderId).getOrderList().get(dishId));
			}
		}
		int orderId;
		int attempt = 1;
		do {
			System.out.print("Enter Order Id : ");
			orderId = Input.getInteger(1000, 9999, true);
			if (orderId == -1)
				return;
			if (!s.contains(orderId)) {
				System.out.println("\nInvalid order id....try again\n");
			}
		} while (!s.contains(orderId) && attempt++ < 3);

		if (!s.contains(orderId) && attempt >= 3) {
			System.out.println("Too many unsuccessful attempts....Try again later...\n");
			return;
		}

		System.out.println("\n1.Confirm Order");
		System.out.println("2.Cancel Order");
		System.out.println("3.Go Back");
		System.out.print("Enter Option : ");

		int option = Input.getInteger(1, 3, false);
		if (option == -1)
			return;
		if (option == 2) {
			String orderDetails = c.getPendingOrder().get(orderId).toString() + "\nOrder Status : Cancelled\n"
					+ Input.getDateTime();
			c.addCompletedOrder(orderId, orderDetails);
			c.getPendingOrder().remove(orderId);
			System.out.println("\nOrder cancelled successfully\n");
			return;
		} else if (option == 1) {
			String hMail = c.getPendingOrder().get(orderId).getHotelMail();
			double price = hDB.getHotel(hMail).calculatePrice(c.getPendingOrder().get(orderId));
			c.getPendingOrder().get(orderId).setPrice(price);
			System.out.println("Total Price : Rs." + price);
			System.out.print("Enter UPI Id : ");
			int upiId = Input.getInteger(true);
			if (upiId == -1)
				return;
			System.out.print("Enter UPI PIN : ");
			int upiPin = Input.getInteger(true);
			if (upiPin == -1)
				return;
			if (c.getUPI().getUPI_ID() == upiId && c.getUPI().getUpiPin() == upiPin) {
				if (c.getUPI().getBalance() < price) {
					System.out.println("\nInsufficient Balance...Try again later\n");
					return;
				}
				hDB.getHotel(hMail).newOrder(c.getPendingOrder().get(orderId));
				c.getUPI().payMoney(price);
				String str = c.getPendingOrder().get(orderId) + "\nOrder Status : Paid\n" + Input.getDateTime();
				c.addCompletedOrder(orderId, str);
				c.getPendingOrder().remove(orderId);
				System.out.println("\nPaid successfully...Your order will be delivered soon...\n");
			} else {
				System.out.println("\nInvalid Credentials...\n");
			}
		} else if (option == 3)
			return;

	}

	private void trackOrder(Customer c) {
		System.out.print("Enter Order Id : ");
		
		int orderId = Input.getInteger(1000, 9999, true);
		if (orderId == -1)
			return;

		if (c.getCompletedOrders().containsKey(orderId))
			System.out.println("\n" + c.getCompletedOrders().get(orderId));
		else
			System.out.println("\nInvalid order id\n");
		
		System.out.print("Press 1 to track order again or 2 to go back : ");

		int option = Input.getInteger(1, 2, true);
		
		if (option == 1)
			trackOrder(c);

	}

	private void viewRecentOrders(Customer c) {
		if (c.getCompletedOrders().isEmpty()) {
			System.out.println("\nNo orders were delivered...Try again later\n");
			return;
		}
		System.out.println();
		Set<Integer> set = c.getCompletedOrders().keySet();
		for (int orderId : set) {
			System.out.println(c.getCompletedOrders().get(orderId));
		}
	}

	private void editAccount(Customer c) {
		int choice;

		do {
			accountSettings();
			choice = Input.getInteger(false);
			switch (choice) {
			case 1: {
				System.out.print("Enter New Name : ");
				String name = Input.getString(true, true);
				if (name == null)
					break;
				c.setName(name);
				System.out.println("\n\u001b[32m" + "Name Changed Successfully" + "\u001b[0m");
				break;
			}
			case 2: {
				System.out.print("Enter New Address : ");
				String address = Input.getString(false, true);
				if (address == null)
					break;
				c.setAddress(address);
				System.out.println("\n\u001b[32m" + "Address Changed Successfully" + "\u001b[0m");
				break;
			}
			case 3: {
				System.out.print("Enter New Pincode : ");
				int pinCode = Input.getInteger(600000, 650000, true);
				if (pinCode == -1)
					break;
				c.setPincode(pinCode);
				c.setPin(Integer.toString(pinCode).substring(0, 3));
				System.out.println("\n\u001b[32m" + "Pincode Changed Successfully" + "\u001b[0m");
				break;
			}
			case 4: {
				System.out.print("Enter Mail Id : ");
				String mailId;
				int attempt = 1;
				do {
					mailId = Input.getString(false, false);
					if (!Validator.validateMailId(mailId) && attempt < 3)
						System.out.print("Enter Valid Mail Id : ");

				} while (!Validator.validateMailId(mailId) && attempt++ < 3);
				if (mailId == null || (!Validator.validateMailId(mailId) && attempt >= 3)) {
					System.out.println("\nToo many unsuccessful attempts....Try again later...");
					break;
				}
				if (cDB.getCustomerList().containsKey(mailId)) {
					System.out.println("\nUsername already exists....try again with different username");
					break;
				}
				String oldMailId = c.getMailId();

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

				int attempt = 1;
				do {
					mobile = Input.getString(false, false);
					if (!Validator.validateMobileNo(mobile) && attempt < 3)
						System.out.print("Enter Valid Mobile No : ");
				} while (!Validator.validateMobileNo(mobile) && attempt++ < 3);
				if (mobile == null || (!Validator.validateMobileNo(mobile) && attempt >= 3)) {
					System.out.println("\nToo many unsuccessful attempts....Try again later...");
					break;
				}
				c.setPhoneNo(mobile);
				System.out.println("\n\u001b[32m" + "Mobile No Changed Successfully" + "\u001b[0m");
				break;
			}
			case 6: {
				System.out.print("Enter Old Password : ");
				String password = Input.getString(false, false);
				if (password.equals(this.getPassword())) {

					if (newPassword()) {
						c.setPassword(this.getPassword());
						System.out.println("\n\u001b[32m" + "Password Changed Successfully" + "\u001b[0m");
					}
				} else {
					System.out.println("Incorrect Password");
				}
				break;
			}
			case 7: {
				return;
			}
			default: {
				System.out.println("\n" + "Invalid Option...Try again....");
			}
			}
		} while (choice != 7);
	}

	private void customerHomePage() {
		System.out.println("\n\t\t\t ---------------------------------------------");
		System.out.println("\t\t\t|                    Customer                 |");
		System.out.println("\t\t\t ---------------------------------------------");

		System.out.println("1.Sign Up");
		System.out.println("2.Log in");
		System.out.println("3.Go Back");
		System.out.print("\nEnter Your Option : ");

	}

	private void customerLoginMenu() {
		System.out.println("\n1.Order Food");
		System.out.println("2.View Cart");
		System.out.println("3.Track Order Status");
		System.out.println("4.View Recent Orders");
		System.out.println("5.Account Settings");
		System.out.println("6.Logout");
		System.out.print("\nEnter Your Option : ");
	}

	private void accountSettings() {
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
