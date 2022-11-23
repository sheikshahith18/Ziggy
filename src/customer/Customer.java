package customer;

import utility.Data;

public class Customer extends Data {

	private UPI upi=null;

	public Customer(String name, String phoneNo, String mailId, String address, int pincode, String password) {
		super(name, phoneNo, mailId, address, pincode, password);
	}

	public UPI getUpi() {
		return this.upi;
	}

	public void addUPIDetails(int upiId,int upiPin){
		if(this.upi==null){
			this.upi=new UPI(upiId,upiPin);
			return;
		}

		this.upi.setUpiId(upiId);
		this.upi.setUpiPin(upiPin);
	}

}
