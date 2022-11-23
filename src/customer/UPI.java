package customer;

public class UPI {
	private  int upiId;
	private  int upiPin;
	private double balance=10000;

	public UPI(int upiId, int upiPin) {
		this.upiId = upiId;
		this.upiPin = upiPin;
	}

	public void setUpiId(int upiId){
		this.upiId=upiId;
	}

	public void setUpiPin(int upiPin){
		this.upiPin=upiPin;
	}

	public int getUpiPin() {
		return this.upiPin;
	}

	public int getUpiId() {
		return this.upiId;
	}

	public void payMoney(double price) {
		this.balance-=price;
	}
	public void refundMoney(double refund) {
		this.balance+=refund;
	}
	public double getBalance() {
		return this.balance;
	}
}
