package utility;

import hotel.Hotel;
import hotel.HotelDB;

import customer.Customer;
import customer.CustomerDB;

public abstract class Register {

	private static final HotelDB HOTEL_DB = HotelDB.getInstance();
	private static final CustomerDB CUSTOMER_DB = CustomerDB.getInstance();



	protected abstract void mainMenu();

	protected abstract void loginScreen();
	protected abstract void successfulLogin(String mailId);


	protected abstract void accountSettingsScreen();
	protected abstract void editAccount(Data d);

	protected abstract void orderHistory(Data data);

	protected String login(boolean isHotel) {
		System.out.print("\nEnter Username(mail Id) : ");
		String username = Input.getString(false, false);
		System.out.print("Enter Password : ");
		String password = Input.getString(false, false);

		if ((isHotel && HOTEL_DB.getHotelList().containsKey(username))
				|| (!isHotel && CUSTOMER_DB.getCustomerList().containsKey(username))) {

			int attempt = 1;
			do {
				if ((isHotel && !HOTEL_DB.getHotelList().get(username).getPassword().equals(password))
						|| (!isHotel && !CUSTOMER_DB.getCustomerList().get(username).getPassword().equals(password))) {

					System.out.println("\nIncorrect Password");
					if (attempt++ > 2) {
						System.out
								.print("\nToo many unsuccessful attempts.....Do u want to try again ( 1-Y / 2-N ) : ");
						int option = Input.getInteger(1, 2, true);
						if (option == 1)
							attempt = 1;
						else
							return "";
					}
					System.out.print("Enter Password Again : ");
					password = Input.getString(false, false);

				} else
					return username;
			} while (true);

		} else {
			System.out.println("\nUser Not Found...Try again later\n");
			return "";
		}
	}

	protected String signup(boolean isHotel) {
		System.out.println();
		String name = newName(isHotel, false, false);
		String ownerName = "";

		if (isHotel)
			ownerName = newName(true, true, false);

		String address = newAddress(false);

		int pincode = newPincode(false);

		String phoneNo = newPhoneNo(false);

		String mailId = newMailId(isHotel, false);

		if (isHotel) {

			boolean isVeg = false;
			System.out.print("Enter Category ( 1.Veg / 2.Non-Veg ) : ");
			int choice = Input.getInteger(1, 2, false);

			if (choice == 1)
				isVeg = true;

			String password = newPassword(false);

			HOTEL_DB.addHotel(new Hotel(name, ownerName, address, pincode, phoneNo, mailId, isVeg, password));
			System.out.println("\n\u001b[36m" + "Account created successfully..." + "\u001b[0m\n");
			return mailId;

		}

		String password = newPassword(false);

		if (!HOTEL_DB.isServiceable(pincode)) {
			System.out.println("There aren't any hotels near your area to serve you...");
			System.out.print("Do u still wish to create an account ( Y-1/ N-2 ) : ");

			int choice = Input.getInteger(1, 2, false);

			if (choice == 2) {
				System.out
						.println("\nYour account is not created at the moment...please try again in the future....\n");
				return "";
			}
		}

		CUSTOMER_DB.addCustomer(new Customer(name, phoneNo, mailId, address, pincode, password));

		System.out.println("\n\u001b[3m" + "Account created successfully..." + "\u001b[0m\n");

		return mailId;

	}

	protected String newName(boolean isHotel, boolean isOwner, boolean limit) {
		if (isOwner)
			System.out.print("Enter Owner's Name : ");
		else if (isHotel)
			System.out.print("Enter Hotel Name : ");
		else
			System.out.print("Enter Name : ");
		return Input.getString(true, limit);
	}

	protected String newAddress(boolean limit) {
		System.out.print("Enter Address : ");
		return Input.getString(false, limit);
	}

	protected int newPincode(boolean limit) {
		System.out.print("Enter Pincode : ");
		return Input.getInteger(600000, 650000, limit);
	}

	protected String newPhoneNo(boolean limit) {
		String phoneNo;
		int attempt = 1;

		System.out.print("Enter Phone No : ");
		do {
			phoneNo = Input.getString(false, false);

			if (Validator.validateMobileNo(phoneNo))
				break;

			if (limit && Input.isAttemptExceeded(true, attempt++))
				return "";

			System.out.print("Enter Valid Mobile No : ");

		} while (true);

		return phoneNo;
	}

	protected String newMailId(boolean isHotel, boolean limit) {

		String mailId;
		int attempt = 1;

		do {

			System.out.print("Enter Mail Id : ");

			do {
				mailId = Input.getString(false, false);

				if (Validator.validateMailId(mailId))
					break;

				if (limit && Input.isAttemptExceeded(true, attempt++))
					return "";

				System.out.print("Enter Valid Mail Id : ");

			} while (true);

			if ((isHotel && HOTEL_DB.getHotelList().containsKey(mailId))
					|| (!isHotel && CUSTOMER_DB.getCustomerList().containsKey(mailId))) {
				System.out.println("\nMail Id already exists....Try again with different mailId...\n");
				if (limit)
					return "";
			} else {
				break;
			}

		} while (true);

		return mailId;

	}

	protected String newPassword(boolean limit) {
		int attempt = 1;
		String password;
		System.out.print("Enter New Password : ");

		do {
			password = Input.getString(false, false);
			if (!Validator.validatePassword(password)) {
				System.err.println(
						"\nYour password is too weak....Please create a strong password following the below criteria");

				String strongPassword = "\n1.Length of the password should be 8-20 digits"
						+ "\n2.Password should contains at least 1 capital letter"
						+ "\n3.Password should contains at least 1 small letter"
						+ "\n4.Password should contain at least 1 symbol";

				System.out.println("\u001b[36m" + strongPassword + "\u001b[0m\n");

				if (Input.isAttemptExceeded(limit, attempt++))
					return "";

				System.out.print("Enter Strong Password : ");

			} else {
				break;
			}
		} while (true);

		attempt = 1;
		String confirmPassword;
		System.out.print("Confirm Password : ");
		do {
			confirmPassword = Input.getString(false, false);
			if (!password.equals(confirmPassword)) {
				System.out.println("Passwords don't match...");

				if (Input.isAttemptExceeded(limit, attempt++))
					return "";

				System.out.print("Enter Again : ");
			} else {
				break;
			}
		} while (true);

		return confirmPassword;
	}

	protected void homeScreen(){
		System.out.println("1.Register");
		System.out.println("2.Log in");
		System.out.println("3.Go Back");
		System.out.print("\nEnter Your Option : ");
	}

}
