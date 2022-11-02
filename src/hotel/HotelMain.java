package hotel;

import java.util.HashMap;
import java.util.Set;
import utility.Register;
import utility.Input;
import utility.Message;
import dish.*;
import customer.CustomerDB;

public class HotelMain extends Register {

	HotelDB hDB = HotelDB.getInstance();
	CustomerDB cDB=CustomerDB.getInstance();

	public void main() {
		int choice;
		do {
			HotelMenu.hotelHomePage();
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
				System.err.println(Message.INVALID_OPTION);
				Input.keepScreen();
				Input.keepScreen();

			}
			}

		} while (choice != 3);
	}

	@Override
	public boolean signup() {
		boolean flag = false;
		String ownerName = null;
		String category = null;
		if (super.signup()) {

			System.out.print("Enter Owner Name : ");
			ownerName = Input.getString(true);

			if (ownerName == null)
				return false;

			System.out.print("Enter Category : ");
			category = Input.getString(false);

			if (category == null)
				return false;

			flag = true;

		}
		if (flag) {
			if (super.newPassword()) {
				if (hDB.getHotelList().containsKey(this.getMailId())) {
					System.out.print("Username already exists in database....Try again with different username...");
					Input.keepScreen();
					return false;
				}
				System.out.print(Message.ACCOUNT_CREATED);
				Input.keepScreen();

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
	public void login() {
		super.login();
		login(this.getMailId(), this.getPassword());
	}

	public void login(String mailId, String password) {
		if (!hDB.getHotelList().containsKey(mailId)) {
			System.out.print("User Not Found...");
			Input.keepScreen();
			return;
		}

		int attempt = 1;
		do {
			if (!hDB.getHotelList().get(mailId).getPassword().equals(this.getPassword())) {
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

	public void successfulLogin(String mailId) {
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
				System.err.print(Message.INVALID_OPTION);
				Input.keepScreen();
				Input.keepScreen();

			}

			}

		} while (choice != 6);
	}

	public void viewOrders(Hotel h) {
		if (h.getPendingOrders().isEmpty()) {
			System.out.print("No orders available...try again later");
			Input.keepScreen();
			return;
		}
		Set<Integer> s = h.getPendingOrders().keySet();

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
		int orderId, cnt = 0;
		do {
			orderId = Input.getInteger(1000, 9999);
			if(!s.contains(orderId)) {
				System.out.println("Invalid order Id....");
			}
		} while (!s.contains(orderId) && cnt++ < 4);
		if (cnt > 4)
			return;

		if (!h.isOrderAvailable(h.getPendingOrders().get(orderId))) {
			String str = h.getPendingOrders().get(orderId).toString() + "\nOrder Status : Cancelled\n"
					+ Input.getDateTime();
			h.addCompletedOrder(str);
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).addCompletedOrder(orderId,str);
			System.out.println("insufficient products");
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).getUPI()
					.refundMoney(h.getPendingOrders().get(orderId).getPrice());
			System.out.println("Amount Refunded to Customer");
			return;
		}

		System.out.println("\n1.Deliver Order");
		System.out.println("2.Cancel Order");
		System.out.print("Enter Option : ");

		int option = Input.getInteger(1, 2);
		if (option == -1)
			return;
		if (option == 1) {
			String str = h.getPendingOrders().get(orderId).toString() + "\nOrder Status : Delivered Successfully\n"
					+ Input.getDateTime();
			h.addCompletedOrder(str);
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).addCompletedOrder(orderId,str);
			h.deliverOrder(h.getPendingOrders().get(orderId));
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).getPendingOrder().remove(orderId);
			h.getPendingOrders().remove(orderId);
			System.out.println("Order delivered Successfully");
		}
		if (option == 2) {
			String str = h.getPendingOrders().get(orderId).toString() + "\nOrder Status : Cancelled\n"
					+ Input.getDateTime();
			h.addCompletedOrder(str);
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).addCompletedOrder(orderId,str);
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).getPendingOrder().remove(orderId);
			h.getPendingOrders().remove(orderId);
			cDB.getCustomer(h.getPendingOrders().get(orderId).getCustMail()).getUPI()
					.refundMoney(h.getPendingOrders().get(orderId).getPrice());
			System.out.println("Order Cancelled");
			System.out.println("Amount Refunded to Customer");
		}

	}

	public void editAccount(Hotel h) {
		int choice;

		do {
			HotelMenu.accountSettings();
			choice = Input.getInteger(false);
			switch (choice) {
			case 1: {
				System.out.println("Enter New Name : ");
				String name = Input.getString(true);
				if (name == null)
					break;
				h.setName(name);
				break;
			}
			case 2: {
				System.out.println("Enter New Address : ");
				String address = Input.getString(false);
				if (address == null)
					break;
				h.setAddress(address);
				break;
			}
			case 3: {
				String pincode = Integer.toString(h.getPincode());
				pincode = pincode.substring(0, 3);
				hDB.getServiceablePinCodes().remove(pincode);

				System.out.println("Enter New Pincode : ");
				int pinCode = Input.getInteger(600000, 650000);

				if (pinCode == -1)
					break;

				h.setPincode(pinCode);

				pincode = Integer.toString(pinCode).substring(0, 3);

				hDB.addServiceablePincodes(pincode);

				break;
			}
			case 4: {
				System.out.println("Enter Mail Id : ");
				String mailId = Input.getString(false);
				if (hDB.getHotelList().containsKey(mailId)) {
					System.out.print("Username already exists....try again with different username");
					Input.keepScreen();
					return;
				}
				String oldMailId=h.getMailId();
				if (mailId == null)
					break;
				h.setMailId(mailId);
				hDB.addHotel(h);
				hDB.removeHotel(oldMailId);
				break;
			}
			case 5: {
				System.out.println("Enter Phone No : ");
				long phoneNo = Input.getPhoneNumber();
				if (phoneNo == -1)
					return;
				h.setPhoneNo(phoneNo);
			}
			case 6: {
				System.out.println("Enter Owner Name : ");
				String ownerName = Input.getString(true);
				if (ownerName == null)
					break;
				h.setOwnerName(ownerName);
				break;
			}
			case 7: {
				System.out.println("Enter Category : ");
				String category = Input.getString(false);
				if (category == null)
					break;
				h.setCategory(category);
				break;
			}
			case 8: {
				System.out.print("Enter Old Password : ");
				String password=Input.getString(false);
				if(password.equals(this.getPassword())) {
					
					if (newPassword()) {
						h.setPassword(this.getPassword());
						System.out.println("Password changed successfully...");
					}
				}else {
					System.out.println(Message.INCORRECT_PASSWORD);
//					System.out.println();
				}
				break;
			}
			case 9: {
				return;
			}
			default: {
				System.out.println(Message.INVALID_OPTION);
			}
			}
		} while (choice != 9);
	}

	public void orderHistory(Hotel h) {
		if (h.getCompletedOrders().isEmpty()) {
			System.out.println("No Delivery were made...Try again later");
			Input.keepScreen();
			Input.keepScreen();
			return;
		}

		for (String str : h.getCompletedOrders()) {
			System.out.println(str + "\n");
		}

		Input.keepScreen();
		Input.keepScreen();
	}

	public void addDish(Hotel h) {
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
				System.out.print("Dish added successfully...");
				Input.keepScreen();
				break;
			}
			case 2: {
				Pizza p = add.addPizza();
				if (p == null)
					break;
				h.addPizza(p);
				System.out.print("Dish added successfully...");
				Input.keepScreen();
				Input.keepScreen();
				break;
			}
			case 3: {
				Chicken c = add.addChicken();
				if (c == null)
					break;

				h.addChicken(c);
				System.out.print("Dish added successfully...");
				Input.keepScreen();
				Input.keepScreen();
				break;
			}
			case 4: {
				Drinks d = add.addDrinks();
				if (d == null)
					break;
				h.addDrink(d);
				System.out.print("Dish added successfully...");
				Input.keepScreen();
				break;
			}
			case 5: {
				break;
			}
			default: {

				System.err.println(Message.INVALID_OPTION);
				Input.keepScreen();
				Input.keepScreen();

			}
			}

		} while (choice != 5);
	}

	public void updateDish(Hotel h) {

		int option;
		AddDish add = new AddDish();

		do {
			h.displayDishes();
			System.out.print("Enter Dish ID : ");
			HashMap<Integer, Dish> dish = h.getDishes();
			int dishId = Input.getInteger(true);
			if (dish.containsKey(dishId)) {
				add.updateDish(h, dishId);

			} else if (h.getPizzas().containsKey(dishId)) {
				add.updatePizza(h, dishId);

			} else if (h.getChicken().containsKey(dishId)) {
				add.updateChicken(h, dishId);

			} else if (h.getDrinks().containsKey(dishId)) {
				add.updateDrinks(h, dishId);

			} else {
				System.out.println("Invalid Dish Id");
			}
			System.out.print("Press 1 to update again else exit");
			option = Input.getInteger(false);
		} while (option == 1);

	}

}
