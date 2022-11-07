package utility;

public class UPI {
	private final int UPI_ID;
	private int upiPin;
	private double balance=10000;

	public UPI(int upiId, int upiPin) {
		this.UPI_ID = upiId;
		this.upiPin = upiPin;
	}

	public int getUpiPin() {
		return upiPin;
	}

	public void setUpiPin(int upiPin) {
		this.upiPin = upiPin;
	}

	public int getUPI_ID() {
		return UPI_ID;
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
