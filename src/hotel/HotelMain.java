package hotel;

import dish.*;
import java.util.Set;
import java.util.HashMap;
import utility.Input;
import utility.Register;
import utility.Validator;
import customer.CustomerDB;

public class HotelMain extends Register {

	HotelDB hDB = HotelDB.getInstance();
	CustomerDB cDB = CustomerDB.getInstance();

	public void main() {
		int choice;
		do {
			HotelMenu.hotelHomePage();
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
				System.out.println("\nInvalid Option...Try again....");
			}
			}

		} while (choice != 3);
	}

	@Override
	protected boolean signup() {
		boolean flag = false;
		String ownerName = null;
		String category = null;
		if (super.signup()) {

			System.out.print("Enter Owner Name : ");
			ownerName = Input.getString(true, false);

			if (ownerName == null)
				return false;

			System.out.print("Enter Category : ");
			category = Input.getString(false, false);

			if (category == null)
				return false;

			flag = true;

		}
		if (flag) {
			if (super.newPassword()) {
				if (hDB.getHotelList().containsKey(this.getMailId())) {
					System.out.println("\nUsername already exists in database....Try again with different username...");
					return false;
				}
				System.out.println("\n\u001b[32m" + "Account created successfully..." + "\u001b[0m");

				hDB.addHotel(new Hotel(getName(), ownerName, getAddress(), getPincode(), getPhoneNo(), getMailId(),
						category, getPassword()));

				String pincode = Integer.toString(getPincode());

				pincode = pincode.substring(0, 3);

				hDB.addServiceablePincodes(pincode);

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
		if (!hDB.getHotelList().containsKey(mailId)) {
			System.out.println("\nUser Not Found...");
			return;
		}

		int attempt = 1;
		do {
			if (!hDB.getHotelList().get(mailId).getPassword().equals(this.getPassword())) {
				System.out.println("\n" + "Incorrect Password");
				if (attempt++ > 2) {
					System.out.print("\nToo many unsuccessful attempts.....Do u want to try again ( 1-Y / 2-N ) : ");
					int option = Input.getInteger(1, 2, true);
					if (option == 1)
						attempt = 1;
					else if (option == 2 || option == -1)
						return;
				}
				super.enterPassword(false);

			} else
				break;
		} while (!hDB.getHotelList().get(mailId).getPassword().equals(this.getPassword()));

		successfulLogin(mailId);
	}

	private void successfulLogin(String mailId) {
		Hotel h = hDB.getHotelList().get(mailId);
		System.out.println("\n....Welcome Mr." + h.getOwnerName() + "....\n");
		int choice;
		do {

			HotelMenu.hotelLoginMenu();
			choice = Input.getInteger(false);
			switch (choice) {
			case 1: { // add dish
				addDish(h);
				break;
			}
			case 2: { // update dish
				updateDish(h);
				break;
			}
			case 3: { // view orders
				viewOrders(h);
				break;
			}
			case 4: { // order history
				orderHistory(h);
				break;
			}
			case 5: { // account settings
				editAccount(h);
				break;
			}
			case 6: { // logout
				break;
			}
			default: {
				System.out.println("\nInvalid Option...Try again....");
			}

			}

		} while (choice != 6);
	}

	private void viewOrders(Hotel h) {
		if (h.getPendingOrders().isEmpty()) {
			System.out.println("\nNo orders available...try again later");
			return;
		}
		Set<Integer> s = h.getPendingOrders().keySet();
		System.out.println();
		for (Integer orderId : s) {
			System.out.println("----------------------------------------");
			System.out.println("Order Id : " + orderId);
			Set<Integer> set = h.getPendingOrders().get(orderId).getOrderList().keySet();
			for (Integer dishId : set) {
				System.out.println(dishId + "\t" + h.getPendingOrders().get(orderId).getOrderList().get(dishId));
			}
			System.out.println("----------------------------------------");
		}

		System.out.print("Enter Order Id : ");
		int orderId;
		do {
			orderId = Input.getInteger(1000, 9999, false);
			if (!s.contains(orderId)) {
				System.out.println("Invalid order Id....Enter again : ");
			}
		} while (!s.contains(orderId));

		if (!h.isOrderAvailable(h.getPendingOrders().get(orderId))) {
			String str = h.getPendingOrders().get(orderId).toString() + "\nOrder Status : Refunded\n"
					+ Input.getDateTime();
			h.addCompletedOrder(str);
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).addCompletedOrder(orderId, str);
			System.out.println("\nInsufficient Products");
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).getUPI()
					.refundMoney(h.getPendingOrders().get(orderId).getPrice());
			h.getPendingOrders().remove(orderId);
			System.out.println("\nAmount Refunded to Customer");

			return;
		}

		System.out.println("\n1.Deliver Order");
		System.out.println("2.Cancel Order");
		System.out.println("3.Go Back");
		System.out.print("Enter Option : ");

		int option = Input.getInteger(1, 3, false);
		if (option == -1)
			return;
		if (option == 1) {
			String str = h.getPendingOrders().get(orderId).toString() + "\nOrder Status : Delivered Successfully\n"
					+ Input.getDateTime();
			h.addCompletedOrder(str);
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).addCompletedOrder(orderId, str);
			h.deliverOrder(h.getPendingOrders().get(orderId));
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).getPendingOrder().remove(orderId);
			h.getPendingOrders().remove(orderId);
			System.out.println("\nOrder delivered Successfully");
		} else if (option == 2) {
			String str = h.getPendingOrders().get(orderId).toString() + "\nOrder Status : Cancelled by hotel\n"
					+ Input.getDateTime();
			h.addCompletedOrder(str);
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).addCompletedOrder(orderId, str);
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).getUPI()
					.refundMoney(h.getPendingOrders().get(orderId).getPrice());
			System.out.println("\nOrder Cancelled");
			System.out.println("Amount Refunded to Customer");
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).getPendingOrder().remove(orderId);
			h.getPendingOrders().remove(orderId);
		} else if (option == 3)
			return;

	}

	private void editAccount(Hotel h) {
		int choice;

		do {
			HotelMenu.accountSettings();
			choice = Input.getInteger(false);
			switch (choice) {
			case 1: {
				System.out.print("Enter New Name : ");
				String name = Input.getString(true, true);
				if (name == null)
					break;
				h.setName(name);
				System.out.println("\n\u001b[32m" + "Name Changed Successfully" + "\u001b[0m");
				break;
			}
			case 2: {
				System.out.print("Enter New Address : ");
				String address = Input.getString(false, true);
				if (address == null)
					break;
				h.setAddress(address);
				System.out.println("\n\u001b[32m" + "Address Changed Successfully" + "\u001b[0m");
				break;
			}
			case 3: {
				String pincode = Integer.toString(h.getPincode());
				pincode = pincode.substring(0, 3);

				System.out.print("Enter New Pincode : ");
				int pinCode = Input.getInteger(600000, 650000, true);

				if (pinCode == -1)
					break;

				hDB.getServiceablePinCodes().remove(pincode);
				h.setPincode(pinCode);
				System.out.println("\n\u001b[32m" + "Pincode Changed Successfully" + "\u001b[0m");

				hDB.addServiceablePincodes(pincode);

				h.setPin(Integer.toString(pinCode).substring(0, 3));

				break;
			}
			case 4: {
				System.out.print("Enter Mail Id : ");
				String mailId;
				int attempt = 1;
				do {

					mailId = Input.getString(false, false);
					if (!Validator.validateMailId(mailId) && attempt < 3) {
						System.out.print("Enter Valid Mail ID : ");
					}
				} while (!Validator.validateMailId(mailId) && attempt++ < 3);
				if (mailId == null || (!Validator.validateMailId(mailId) && attempt >= 3)) {
					System.out.println("\nToo many unsuccessful attempts....Try again later...");
					break;
				}
				if (hDB.getHotelList().containsKey(mailId)) {
					System.out.println("\nUsername already exists....try again with different username");
					break;
				}
				String oldMailId = h.getMailId();
				h.setMailId(mailId);
				System.out.println("\n\u001b[32m" + "Mail Id Changed Successfully" + "\u001b[0m");
				hDB.addHotel(h);
				hDB.removeHotel(oldMailId);
				break;
			}
			case 5: {
				System.out.print("Enter Phone No : ");
				String phoneNo;
				int attempt = 1;
				do {
					phoneNo = Input.getString(false, false);
					if (!Validator.validateMobileNo(phoneNo) && attempt < 3)
						System.out.print("Enter Valid Mobile No : ");
				} while (!Validator.validateMobileNo(phoneNo) && attempt++ < 3);
				if (phoneNo == null || (!Validator.validateMobileNo(phoneNo) && attempt >= 3)) {
					System.out.println("\nToo many unsuccessful attempts....Try again later...");
					break;
				}
				h.setPhoneNo(phoneNo);
				System.out.println("\n\u001b[32m" + "Phone No Changed Successfully" + "\u001b[0m");
				break;
			}
			case 6: {
				System.out.print("Enter Owner Name : ");
				String ownerName;

				ownerName = Input.getString(true, true);
				if (ownerName == null)
					break;

				h.setOwnerName(ownerName);
				System.out.println("\n\u001b[32m" + "Owner's Name Changed Successfully" + "\u001b[0m");
				break;
			}
			case 7: {
				System.out.print("Enter Category : ");
				String category = Input.getString(false, false);
				if (category == null)
					break;
				h.setCategory(category);
				System.out.println("\n\u001b[32m" + "Category Changed Successfully" + "\u001b[0m");
				break;
			}
			case 8: {
				System.out.print("Enter Old Password : ");
				String password = Input.getString(false, false);
				if (password.equals(this.getPassword())) {

					if (newPassword()) {
						h.setPassword(this.getPassword());
						System.out.println("\n\u001b[32m" + "Password Changed Successfully" + "\u001b[0m");
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
				System.out.println("\nInvalid Option...Try again....");
			}
			}
		} while (choice != 9);
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

	private void addDish(Hotel h) {
		AddDish add = new AddDish();
		int choice;

		do {
			HotelMenu.dishMenu();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {
				Dish d = add.addDish(true);

				if (d == null)
					break;
				h.addDish(d);
				System.out.println("\nDish added successfully...");
				break;
			}
			case 2: {
				Pizza p = add.addPizza();
				if (p == null)
					break;
				h.addPizza(p);
				System.out.println("\nDish added successfully...");
				break;
			}
			case 3: {
				Chicken c = add.addChicken();
				if (c == null)
					break;

				h.addChicken(c);
				System.out.println("\nDish added successfully...");
				break;
			}
			case 4: {
				Drinks d = add.addDrinks();
				if (d == null)
					break;
				h.addDrink(d);
				System.out.println("\nDish added successfully...");
				break;
			}
			case 5: {
				break;
			}
			default: {

				System.out.println("\nInvalid Option...Try again....");

			}
			}

		} while (choice != 5);
	}

	private void updateDish(Hotel h) {

		int option;
		AddDish add = new AddDish();

		do {
			h.displayDishes();
			System.out.print("Enter Dish ID : ");
			HashMap<Integer, Dish> dish = h.getDishes();
			int dishId = Input.getInteger(true);
			if(dishId==-1) {
				return;
			}
			if (dish.containsKey(dishId)) {
				add.updateDish(h, dishId);

			} else if (h.getPizzas().containsKey(dishId)) {
				add.updatePizza(h, dishId);

			} else if (h.getChicken().containsKey(dishId)) {
				add.updateChicken(h, dishId);

			} else if (h.getDrinks().containsKey(dishId)) {
				add.updateDrinks(h, dishId);

			} else {
				System.out.println("\nInvalid Dish Id");
			}
			System.out.print("Press 1 to update again or 2 to exit : ");
			option = Input.getInteger(1, 2, true);
		} while (option == 1);

	}

}
