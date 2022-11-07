package utility;

import hotel.Hotel;
import hotel.HotelDB;
import customer.CustomerDB;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;

public class Order {
	private static int id = 1004;
	private final int orderId;
	private int custId;
	private int hotelId;
	private String hotelMail;
	private String custMail;
	private String orderStatus = "Not Delivered";
	private double price;

	private HashMap<Integer, String> orderList = new HashMap<>();

	private HashMap<Integer, Integer> dish = new HashMap<>();
	private HashMap<Integer, Integer> pizza = new HashMap<>();
	private HashMap<Integer, Integer> chicken = new HashMap<>();
	private HashMap<Integer, Integer> drinks = new HashMap<>();
	private ArrayList<String> toppings = new ArrayList<>();
	private ArrayList<String> extras = new ArrayList<>();

	public Order(HashMap<Integer, Integer> dish, HashMap<Integer, Integer> pizza, HashMap<Integer, Integer> chicken,
			HashMap<Integer, Integer> drinks, ArrayList<String> toppings, ArrayList<String> extras,
			HashMap<Integer, String> orderList, int custId, int hotelId, String hotelMail, String custMail) {
		this.dish = dish;
		this.pizza = pizza;
		this.chicken = chicken;
		this.drinks = drinks;
		this.toppings = toppings;
		this.extras = extras;
		this.orderList = orderList;
		this.custId = custId;
		this.hotelId = hotelId;
		this.hotelMail = hotelMail;
		this.custMail = custMail;

		this.orderId = id;
		id += 2;
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

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getCustId() {
		return custId;
	}

	public int getHotelId() {
		return hotelId;
	}

	public HashMap<Integer, Integer> getDish() {
		return dish;
	}

	public void setDish(HashMap<Integer, Integer> dish) {
		this.dish = dish;
	}

	public HashMap<Integer, Integer> getPizza() {
		return pizza;
	}

	public void setPizza(HashMap<Integer, Integer> pizza) {
		this.pizza = pizza;
	}

	public HashMap<Integer, Integer> getChicken() {
		return chicken;
	}

	public void setChicken(HashMap<Integer, Integer> chicken) {
		this.chicken = chicken;
	}

	public HashMap<Integer, Integer> getDrinks() {
		return drinks;
	}

	public void setDrinks(HashMap<Integer, Integer> drinks) {
		this.drinks = drinks;
	}

	public ArrayList<String> getToppings() {
		return toppings;
	}

	public void setToppings(ArrayList<String> toppings) {
		this.toppings = toppings;
	}

	public ArrayList<String> getExtras() {
		return extras;
	}

	public void setExtras(ArrayList<String> extras) {
		this.extras = extras;
	}

	public int getOrderId() {
		return orderId;
	}

	public String toString() {
		Set<Integer> set;
		String str = "Order ID : " + this.orderId + "\n";
		HotelDB hDB = HotelDB.getInstance();
		Hotel h = hDB.getHotel(this.hotelMail);
		if (!this.dish.isEmpty()) {
			str += "Food :\n";
			set = this.dish.keySet();
			for (Integer dishId : set) {
				str += dishId + "\t";
				String s = h.getDishes().get(dishId).getName() + "\t" + h.getDishes().get(dishId).getPrice() + "\t"
						+ this.dish.get(dishId);
				str += s + "\n";
			}
		}

		if (!this.pizza.isEmpty()) {
			set = this.pizza.keySet();
			str += "Pizza : \n";
			for (Integer dishId : set) {
				str += dishId + "\t";
				String string = h.getPizzas().get(dishId).getName() + "\t" + h.getPizzas().get(dishId).getPrice() + "\t"
						+ this.pizza.get(dishId);
				str += string + "\n";
				for (String s : this.getToppings()) {
					if (h.getPizzas().get(dishId).getToppings().containsKey(s)) {
						string = s + "\t" + h.getPizzas().get(dishId).getToppings().get(s);
						str += string + "\n";
					}
				}
			}
		}

		if (!this.chicken.isEmpty()) {
			set = this.chicken.keySet();
			str += "Chicken :\n";
			for (Integer dishId : set) {
				str += dishId + "\t";
				String string = h.getChicken().get(dishId).getName() + "\t" + h.getChicken().get(dishId).getPrice()
						+ "\t" + this.chicken.get(dishId);
				for (String s : this.getExtras()) {
					if (h.getChicken().get(dishId).getExtras().containsKey(s)) {
						string = s + "\t" + h.getChicken().get(dishId).getExtras().get(s);
						str += string + "\n";
					}
				}
			}
		}

		if (!this.drinks.isEmpty()) {
			set = this.drinks.keySet();
			str += "Drinks :\n";
			for (Integer dishId : set) {
				str += dishId + "\t";
				String s = h.getDrinks().get(dishId).getName() + "\t" + h.getDrinks().get(dishId).getPrice() + "\t"
						+ this.drinks.get(dishId);
				str += s + "\n";
			}
		}
		
		if(!this.dish.isEmpty() || !this.pizza.isEmpty() || !this.chicken.isEmpty() || !this.drinks.isEmpty()) {
			CustomerDB cDB=CustomerDB.getInstance();
			str+="Customer Name : " + cDB.getCustomerList().get(this.custMail).getName() + "\n";
			str+="Customer Address : " + cDB.getCustomerList().get(this.custMail).getAddress() + "\n";
			str+="Hotel Name : " + hDB.getHotelList().get(this.hotelMail).getName()+"\n";
		}
		return str;
	}

}
