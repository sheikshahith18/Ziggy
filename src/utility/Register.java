package utility;

public abstract class Register extends Data {

	private int attempt = 1;
	private static boolean firstTry = true;

	public void login() {
		System.out.print("Enter Username(mail Id) : ");
		this.setMailId(Input.getString(false));
		enterPassword(true);
	}

	public void enterPassword(boolean flag) {
		if (flag)
			System.out.print("Enter Password : ");
		else
			System.out.print(Message.ENTER_AGAIN);
		this.setPassword(Input.getString(false));
	}

	public boolean signup() {

		System.out.print("Enter Name : ");
		this.setName(Input.getString(true));

		if (this.getName() == null)
			return false;

		System.out.print("Enter Address : ");
		this.setAddress(Input.getString(false));

		if (this.getAddress() == null)
			return false;

		System.out.print("Enter Pincode : ");
		this.setPincode(Input.getInteger(600000, 650000));

		if (this.getPincode() == -1)
			return false;

		System.out.print("Enter Phone No : ");
		this.setPhoneNo(Input.getPhoneNumber());

		if (this.getPhoneNo() == -1)
			return false;

		System.out.print("Enter Mail Id : ");
		this.setMailId(Input.getString(false));
		if (this.getMailId() == null)
			return false;

		return true;

	}

	public boolean newPassword() {

		if (attempt++ > 3) {
			System.out.print(Message.TOO_MANY_ATTEMPTS);
			Input.keepScreen();
			attempt = 1;
			firstTry=true;
			return false;
		}
		String password, confirmPassword;
		if (firstTry) {
			System.out.print("Enter Password : ");
			firstTry = false;
		} else
			System.out.print("Enter Strong Password : ");
		password = Input.getString(false);

		if (password == null) {
			return false;
		}

		if (!Validator.validatePassword(password)) {
			System.out.println("validation failed");
			return newPassword();
		}

		attempt = 1;
		firstTry = true;

		do {
			if (firstTry) {
				firstTry = false;
				System.out.print("Confirm Password : ");
			} else {
				System.out.println(Message.PASSWORD_MISMATCH);
				System.out.print(Message.ENTER_AGAIN);
			}
			confirmPassword = Input.getString(false);

			if (password.equals(confirmPassword)) {
				this.setPassword(password);
				attempt = 1;
				firstTry = true;
				return true;
			}
		} while (attempt++ < 3);

		attempt = 1;
		firstTry=true;
		System.out.print(Message.TOO_MANY_ATTEMPTS);
		Input.keepScreen();
		return false;

	}

}
