package customer;

import java.util.HashMap;

public class CustomerDB {

	private HashMap<String, Customer> customerList = new HashMap<>();

	private static CustomerDB customer_DB_instance = null;

	private CustomerDB() {
	}

	public static CustomerDB getInstance() {
		if (customer_DB_instance == null)
			customer_DB_instance = new CustomerDB();
		return customer_DB_instance;
	}

	public void addCustomer(Customer c) {
		customerList.put(c.getMailId(), c);
	}

	public HashMap<String, Customer> getCustomerList() {
		return customerList;
	}

	public void removeCustomer(String mailId) {
		customerList.remove(mailId);
	}

}
