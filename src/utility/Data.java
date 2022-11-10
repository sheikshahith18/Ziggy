package utility;

public abstract class Data {

	private String name;
	private String phoneNo;
	private String mailId;
	private String address;
	private int pincode;
	private String pin;
	private String password;

	Data() {
	}

	public Data(String name, String phoneNo, String mailId, String address, int pincode, String password) {
		this.name = name;
		this.phoneNo = phoneNo;
		this.mailId = mailId;
		this.address = address;
		this.pincode = pincode;
		this.password = password;

		String pin = Integer.toString(pincode);
		setPin(pin.substring(0, 3));
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNo() {
		return this.phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
