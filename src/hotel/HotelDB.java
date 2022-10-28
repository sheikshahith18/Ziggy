package hotel;

import java.util.ArrayList;
import java.util.HashMap;

public class HotelDB {

	private static HashMap<String, Hotel> hotelList=new HashMap<>();
	private static ArrayList<String> serviceablePinCodes=new ArrayList<>();
	private static HotelDB hotel_DB_instance=null;
	
	private HotelDB() {}
	
	public static HotelDB getInstance() {
		if(hotel_DB_instance==null)
			hotel_DB_instance=new HotelDB();
		return hotel_DB_instance;
			
	}

	public static void addHotel(Hotel h) {
		hotelList.put(h.getMailId(), h);
	}
	
	public void addServiceablePincodes(String pincode) {
		serviceablePinCodes.add(pincode);
	}
	
	public static ArrayList<String> getServiceablePinCodes() {
		return serviceablePinCodes;
	}
	
	public static boolean isServiceable(String pincode) {
		if(serviceablePinCodes.contains(pincode))
			return true;
		return false;
	}

	public static Hotel getHotel(String mailId) {
		if(hotelList.containsKey(mailId))
			return hotelList.get(mailId);
		return null;
	}
	
	public HashMap<String,Hotel> getHotelList(){
		return hotelList;
	}
	
	public static void removeHotel(String mailId) {
		if(hotelList.containsKey(mailId))
			hotelList.remove(mailId);
	}
	
	public static Hotel containsHotel(String mailId,String password) {
		
		if(!hotelList.containsKey(mailId))
			return null;
		
		if(password.equals(hotelList.get(mailId).getPassword()))
			return hotelList.get(mailId);
		
		return null;
	}

}
