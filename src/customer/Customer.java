package customer;

import java.util.ArrayList;
import java.util.HashMap;
import utility.Data;
import utility.UPI;
import utility.Order;

public class Customer extends Data {
	private final int C_ID;
	String orders = "";
	private UPI upi;
	private HashMap<Integer, Order> pendingOrder = new HashMap<>();
	private HashMap<Integer, String> orderStatus = new HashMap<>();
	private ArrayList<String> completedOrders = new ArrayList<>();
	private boolean isPureVeg = false;

	Customer(String name, long phoneNo, String mailId, String address, int pincode, String password, int upiId,
			int upiPIN) {
		super(name, phoneNo, mailId, address, pincode, password);
		C_ID = cId();
		this.upi = new UPI(upiId, upiPIN);
	}

	public void newOrder(Order order) {
		this.pendingOrder.put(order.getOrderId(), order);
	}

	public void setOrderStatus(int orderId, String status) {
		this.orderStatus.put(orderId, status);
	}

	public void addCompletedOrder(String str) {
		str = "--------------------------------------------------" + "\n" + str;
		str += "\n--------------------------------------------------";
		this.completedOrders.add(str);
	}

	public ArrayList<String> getCompletedOrders() {
		return this.completedOrders;
	}

	public HashMap<Integer, String> getOrderStatus() {
		return orderStatus;
	}

	public HashMap<Integer, Order> getPendingOrder() {
		return pendingOrder;
	}

	public boolean isPureVeg() {
		return isPureVeg;
	}

	public void setPureVeg(boolean isPureVeg) {
		this.isPureVeg = isPureVeg;
	}

	public int getC_ID() {
		return C_ID;
	}

	public UPI getUPI() {
		return this.upi;
	}

	public void addOrderHistory(Order order) {

	}
}
