package hotel;

import java.util.HashMap;
import java.util.ArrayList;

public class HotelDB {

	private HashMap<String, Hotel> hotelList = new HashMap<>();
	private ArrayList<String> serviceablePinCodes = new ArrayList<>();
	private static HotelDB hotel_DB_instance = null;

	private HotelDB() {
	}

	public static HotelDB getInstance() {
		if (hotel_DB_instance == null)
			hotel_DB_instance = new HotelDB();
		return hotel_DB_instance;

	}

	public void addHotel(Hotel h) {
		hotelList.put(h.getMailId(), h);
	}

	public void addServiceablePincode(int pincode) {
		serviceablePinCodes.add(Integer.toString(pincode).substring(0,3));
	}
	
	public void removeServiceablePincode(int pincode) {
		serviceablePinCodes.remove(Integer.toString(pincode).substring(0, 3));
	}

	public boolean isServiceable(int pincode) {
		return serviceablePinCodes.contains(Integer.toString(pincode).substring(0,3));
	}

	public HashMap<String, Hotel> getHotelList() {
		return hotelList;
	}

	public void removeHotel(String mailId) {
		hotelList.remove(mailId);
	}

}
