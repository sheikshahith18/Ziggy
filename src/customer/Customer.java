package customer;

import utility.UPI;
import utility.Data;
import utility.Order;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Customer extends Data {

	private UPI upi;

	private HashMap<Integer, Order> pendingOrders = new HashMap<>();  // orderId,order
	private HashMap<Integer, String> completedOrders = new LinkedHashMap<>();  // orderId,orderDetails

	public Customer(String name, String phoneNo, String mailId, String address, int pincode, String password) {
		super(name, phoneNo, mailId, address, pincode, password);
		this.upi = new UPI(1234, 1234);
	}

	public UPI getUPI() {
		return this.upi;
	}

	public HashMap<Integer, String> getCompletedOrders() {
		return this.completedOrders;
	}

	public HashMap<Integer, Order> getPendingOrders() {
		return pendingOrders;
	}

	public void newOrder(Order order) {
		this.pendingOrders.put(order.getOrderId(), order);
	}

	public void addCompletedOrder(int orderId, String str) {
		this.pendingOrders.remove(orderId);
		str = "--------------------------------------------------" + "\n" + str;
		str += "\n--------------------------------------------------\n";
		this.completedOrders.put(orderId, str);
	}
	
	public void print() {
		System.out.println("Hotelname : "+this.getName());
		System.out.println("address : "+this.getAddress());
		System.out.println("pincode : "+this.getPincode());
		System.out.println("mail : "+this.getMailId());
		System.out.println("phone no : "+this.getPhoneNo());
		System.out.println("Password : "+this.getPassword());
		System.out.println("Upi balance : "+this.getUPI().getBalance());
	}
}
