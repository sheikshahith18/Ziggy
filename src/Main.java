package Ziggy;

import utility.HomePage;
import utility.Input;
import utility.Message;
import hotel.HotelMain;
import customer.CustomerMain;
public class Main {

	public static void main(String[] args) {

		int choice;
		do {

			HomePage.homePage();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {// Hotel
				HotelMain hm = new HotelMain();
				hm.main();
				break;
			}
			case 2: {// Customer
				CustomerMain cm=new CustomerMain();
				cm.main();
				break;
			}
			case 3: {// Exit
				break;
			}
			default: {

				System.err.print(Message.INVALID_OPTION);
				Input.keepScreen();
				Input.keepScreen();
			}
			}

		} while (choice != 3);

	}

}
