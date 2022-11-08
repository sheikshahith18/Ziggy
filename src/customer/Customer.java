package customer;

import utility.UPI;
import utility.Data;
import utility.Order;
import java.util.HashMap;

public class Customer extends Data {

	private final int C_ID;
	private UPI upi;
	private HashMap<Integer, Order> pendingOrder = new HashMap<>();
	private HashMap<Integer, String> completedOrders = new HashMap<>();

	public Customer(String name, String phoneNo, String mailId, String address, int pincode, String password) {
		super(name, phoneNo, mailId, address, pincode, password);
		C_ID = cId();
		this.upi = new UPI(1234, 1234);
	}

	public void newOrder(Order order) {
		this.pendingOrder.put(order.getOrderId(), order);
	}

	public void addCompletedOrder(int orderId, String str) {
		str = "--------------------------------------------------" + "\n" + str;
		str += "\n--------------------------------------------------\n";
		this.completedOrders.put(orderId, str);
	}

	public HashMap<Integer, String> getCompletedOrders() {
		return this.completedOrders;
	}

	public HashMap<Integer, Order> getPendingOrder() {
		return pendingOrder;
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
