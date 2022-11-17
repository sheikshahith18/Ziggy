package utility;

import customer.CustomerDB;

import hotel.Hotel;
import hotel.HotelDB;

import java.util.Set;
import java.util.HashMap;

public class Order {
	private static int id = 1004;
	private final int ORDER_ID;
	private String hotelMail;
	private String customerMail;
	private double price;

	private HashMap<Integer, Integer> dishes;
	private HashMap<Integer, Integer> drinks;

	private HashMap<Integer, HashMap<String, Double>> extras = new HashMap<>();

	public Order(HashMap<Integer, Integer> dishes, HashMap<Integer, Integer> drinks,
			HashMap<Integer, HashMap<String, Double>> extras, String customerMail, String hotelMail) {
		this.dishes = dishes;
		this.drinks = drinks;
		this.customerMail = customerMail;
		this.hotelMail = hotelMail;
		this.extras.putAll(extras);

		this.ORDER_ID = id;
		id += 2;
	}

	public HashMap<Integer, Integer> getDishes() {
		return this.dishes;
	}

	public HashMap<Integer, Integer> getDrinks() {
		return this.drinks;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getHotelMail() {
		return this.hotelMail;
	}

	public String getCustomerMail() {
		return this.customerMail;
	}

	public int getOrderId() {
		return this.ORDER_ID;
	}

	public double calculatePrice() {

		Hotel h = HotelDB.getInstance().getHotelList().get(this.hotelMail);
		double price = 0;
		Set<Integer> set;

		if (!this.dishes.isEmpty()) {
			set = this.dishes.keySet();
			for (Integer dishId : set) {
				price += this.dishes.get(dishId) * h.getDishes().get(dishId).getPrice();
				if (this.extras.containsKey(dishId)) {
					Set<String> extraSet = this.extras.get(dishId).keySet();
					for (String extrasName : extraSet) {
						price += this.extras.get(dishId).get(extrasName);
					}
				}
			}
		}

		if (!this.drinks.isEmpty()) {
			set = this.drinks.keySet();
			for (Integer dishId : set) {
				price += this.drinks.get(dishId) * h.getDrinks().get(dishId).getPrice();
			}
		}

		double tax = price * ((double) 5 / 100);
		System.out.println("\nPrice : " + price);
		System.out.println("Tax : " + tax);
		price += tax;

		return price;

	}

	public String getOrderDetails(boolean isHotel) {
		Set<Integer> set;
		String orderDetails = "ORDER ID : " + this.ORDER_ID + "\n";

		Hotel h = HotelDB.getInstance().getHotelList().get(this.hotelMail);

		if (!this.dishes.isEmpty()) {
			orderDetails += "\nFOOD :\n";
			set = this.dishes.keySet();
			for (Integer dishId : set) {
				orderDetails += "\n" + dishId + "\t" + h.getDishes().get(dishId).getName() + "\t"
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
				orderDetails += "\n" + dishId + "\t" + h.getDrinks().get(dishId).getName() + "\t"
						+ h.getDrinks().get(dishId).getPrice() + "\t" + this.drinks.get(dishId) + "\n";
			}
		}

		if (!this.dishes.isEmpty() || !this.drinks.isEmpty()) {
			if (isHotel) {
				CustomerDB customerDB = CustomerDB.getInstance();
				orderDetails += "\nCustomer Name : " + customerDB.getCustomerList().get(this.customerMail).getName();
				orderDetails += "\nCustomer Address : "
						+ customerDB.getCustomerList().get(this.customerMail).getAddress();
			} else {
				orderDetails += "\nHotel Name : " + h.getName();
				orderDetails += "\nHotel PhoneNo : " + h.getPhoneNo() + "\n";
			}
		}

		return orderDetails;
	}

}
