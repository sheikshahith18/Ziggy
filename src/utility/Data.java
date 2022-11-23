package utility;

import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class Data {

	private String name;
	private String phoneNo;
	private String mailId;
	private String address;
	private int pincode;
	private String password;

	private HashMap<Integer, Order> pendingOrders = new LinkedHashMap<>();  // orderId,order
	private HashMap<Integer, String> completedOrders = new LinkedHashMap<>();  // orderId,orderDetails

	public Data(String name, String phoneNo, String mailId, String address, int pincode, String password) {
		this.name = name;
		this.phoneNo = phoneNo;
		this.mailId = mailId;
		this.address = address;
		this.pincode = pincode;
		this.password = password;
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

}
