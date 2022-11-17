package utility;

public abstract class Data {

	private String name;
	private String phoneNo;
	private String mailId;
	private String address;
	private int pincode;
	private String password;

	public Data(String name, String phoneNo, String mailId, String address, int pincode, String password) {
		this.name = name;
		this.phoneNo = phoneNo;
		this.mailId = mailId;
		this.address = address;
		this.pincode = pincode;
		this.password = password;
	}
	
	public String getName() {
		return this.name;
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
		return this.mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPincode() {
		return this.pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
