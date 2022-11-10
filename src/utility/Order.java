package utility;

import hotel.Hotel;
import hotel.HotelDB;
import customer.CustomerDB;
import java.util.Set;
import java.util.HashMap;

public class Order {
	private static int id = 1004;
	private final int orderId;
	private String hotelMail;
	private String custMail;
	private double price;

	private HashMap<Integer, String> orderList = new HashMap<>();

	private HashMap<Integer, Integer> dishes;
	private HashMap<Integer, Integer> drinks;

	private HashMap<Integer, HashMap<String, Double>> extras = new HashMap<>();

	public Order(HashMap<Integer, Integer> dishes, HashMap<Integer, Integer> drinks,
			HashMap<Integer, HashMap<String, Double>> extras, HashMap<Integer, String> orderList, String custMail,
			String hotelMail) {
		this.dishes = dishes;
		this.drinks = drinks;
		this.orderList = orderList;
		this.custMail = custMail;
		this.hotelMail = hotelMail;
		this.extras.putAll(extras);

		this.orderId = id;
		id += 2;
	}

	public HashMap<Integer, Integer> getDishes() {
		return this.dishes;
	}

	public HashMap<Integer, Integer> getDrinks() {
		return this.drinks;
	}

	public HashMap<Integer, HashMap<String, Double>> getExtras() {
		return this.extras;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getHotelMail() {
		return hotelMail;
	}

	public String getCustMail() {
		return custMail;
	}

	public HashMap<Integer, String> getOrderList() {
		return orderList;
	}

	public int getOrderId() {
		return orderId;
	}

	@Override
	public String toString() {
		Set<Integer> set;
		String orderDetails = "ORDER ID : " + this.orderId + "\n";
		HotelDB hDB = HotelDB.getInstance();
		Hotel h = hDB.getHotel(this.hotelMail);

		if (!this.dishes.isEmpty()) {
			orderDetails += "\nFOOD :\n";
			set = this.dishes.keySet();
			for (Integer dishId : set) {
				orderDetails += dishId + "\t" + h.getDishes().get(dishId).getName() + "\t"
						+ h.getDishes().get(dishId).getPrice() + "\t" + this.dishes.get(dishId) + "\n";
				if (this.extras.containsKey(dishId)) {
					Set<String> extraSet = this.extras.get(dishId).keySet();
					String extrasDetails = "";
					for (String extrasName : extraSet) {
						extrasDetails += extrasName + "\t" + this.extras.get(dishId).get(extrasName) + "\n";
					}
					orderDetails = orderDetails + extrasDetails;
				}
			}
		}

		if (!this.drinks.isEmpty()) {
			orderDetails += "\nDRINKS :\n";
			set = this.drinks.keySet();
			for (Integer dishId : set) {
				orderDetails += dishId + "\t" + h.getDrinks().get(dishId).getName() + "\t"
						+ h.getDrinks().get(dishId).getPrice() + "\t" + this.drinks.get(dishId) + "\n";
			}
		}

		if (!this.dishes.isEmpty() || !this.drinks.isEmpty()) {
			CustomerDB cDB = CustomerDB.getInstance();
			orderDetails += "\nCustomer Name : " + cDB.getCustomerList().get(this.custMail).getName() + "\n";
			orderDetails += "Hotel Name : " + hDB.getHotelList().get(this.hotelMail).getName() + "\n";
		}

		return orderDetails;
	}

}
