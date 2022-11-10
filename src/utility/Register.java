package utility;

import hotel.HotelDB;
import customer.CustomerDB;

public abstract class Register extends Data {

	private static boolean firstTry = true;

	protected void login() {
		System.out.print("\nEnter Username(mail Id) : ");
		this.setMailId(Input.getString(false, false));
		enterPassword(true);
	}

	public void enterPassword(boolean flag) {
		if (flag)
			System.out.print("Enter Password : ");
		else
			System.out.print("Enter Password Again : ");
		this.setPassword(Input.getString(false, false));
	}

	protected boolean signup(boolean isHotel) {

		if (isHotel)
			System.out.print("\nEnter Hotel Name : ");
		else
			System.out.print("\nEnter Name : ");
		this.setName(Input.getString(true, false));

		if (this.getName() == null)
			return false;

		System.out.print("Enter Address : ");
		this.setAddress(Input.getString(false, false));

		if (this.getAddress() == null)
			return false;

		System.out.print("Enter Pincode (600000-650000) : ");
		this.setPincode(Input.getInteger(600000, 650000, false));

		if (this.getPincode() == -1)
			return false;

		System.out.print("Enter Phone No : ");
		String mobile;
		do {
			mobile = Input.getString(false, false);
			if (!Validator.validateMobileNo(mobile))
				System.out.print("Enter Valid Mobile No : ");
		} while (!Validator.validateMobileNo(mobile));
		this.setPhoneNo(mobile);

		if (this.getPhoneNo() == null)
			return false;

		String mail = "";
		if (isHotel) {
			HotelDB hDB = HotelDB.getInstance();
			do {
				System.out.print("Enter Mail Id : ");
				do {
					mail = Input.getString(false, false);
					if (!Validator.validateMailId(mail)) {
						System.out.print("Enter Valid Mail Id : ");
					}
				} while (!Validator.validateMailId(mail));
				if (hDB.getHotelList().containsKey(mail)) {
					System.out.println("\nUsername already exists in database....Try again with different username...\n");
				}

			} while (hDB.getHotelList().containsKey(mail));
		} else {
			CustomerDB cDB = CustomerDB.getInstance();
			do {
				System.out.print("Enter Mail Id : ");
				do {
					mail = Input.getString(false, false);
					if (!Validator.validateMailId(mail)) {
						System.out.print("Enter Valid Mail Id : ");
					}
				} while (!Validator.validateMailId(mail));
				if (cDB.getCustomerList().containsKey(mail)) {
					System.out.println("\nUsername already exists in database....Try again with different username...\n");
				}

			} while (cDB.getCustomerList().containsKey(mail));
		}
		this.setMailId(mail);
		if (this.getMailId() == null)
			return false;

		return true;

	}

	public boolean newPassword() {

		String password, confirmPassword;
		if (firstTry) {
			System.out.print("Enter New Password : ");
			firstTry = false;
		} else
			System.out.print("\nEnter Strong Password : ");
		password = Input.getString(false, false);

		if (password == null) {
			return false;
		}

		if (!Validator.validatePassword(password)) {
			System.err.println(
					"\nYour password is too weak....Please create a strong password following the below criteria");

			String strongPassword = "\n1.Length of the password should be 8-20 digits"
					+ "\n2.Password should contains atleast 1 capital letter"
					+ "\n3.Password should contains at least 1 small letter"
					+ "\n4.Password should contain at least 1 symbol";

			System.out.println("\u001b[32m" + strongPassword + "\u001b[0m");
			return newPassword();
		}

		firstTry = true;

		do {
			if (firstTry) {
				firstTry = false;
				System.out.print("Confirm Password : ");
			} else {
				System.out.println("Passwords don't match...Try again...");
				System.out.print("Enter Again : ");
			}
			confirmPassword = Input.getString(false, false);

			if (password.equals(confirmPassword)) {
				this.setPassword(password);
				firstTry = true;
				return true;
			}
		} while (!password.equals(confirmPassword));

		return false;

	}

}
