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

	public void addServiceablePincodes(String pincode) {
		serviceablePinCodes.add(pincode);
	}

	public ArrayList<String> getServiceablePinCodes() {
		return serviceablePinCodes;
	}

	public boolean isServiceable(String pincode) {
		return serviceablePinCodes.contains(pincode);
	}

	public Hotel getHotel(String mailId) {
		return hotelList.get(mailId);
	}

	public HashMap<String, Hotel> getHotelList() {
		return hotelList;
	}

	public void removeHotel(String mailId) {
		hotelList.remove(mailId);
	}

}
