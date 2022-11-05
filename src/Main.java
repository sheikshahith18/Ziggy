package Ziggy;

import utility.Init;
import utility.Input;
import utility.Message;
import hotel.HotelMain;
import customer.CustomerMain;

public class Main {

	public static void main(String[] args) {

		System.out.println("\t\t\t\t\t----------Welcome to Ziggy----------\n");

		Init.initializeHotel();
		Init.initializeCustomer();

//				  Hotel Login : 
//				  Username : hotel@gmail.com 
//				  Password : Qwerty@123 
//		  		  Customer Login : 
//		  		  Username : customer1@gmail.com , customer2@gmail.com 
//		  		  Password : Qwerty@123

		int choice;
		do {

			homePage();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {

				HotelMain hm = new HotelMain();
				hm.main();
				break;
			}
			case 2: {

				CustomerMain cm = new CustomerMain();
				cm.main();
				break;
			}
			case 3: {
				break;
			}
			default: {

				System.out.println(Message.INVALID_OPTION);
			}
			}

		} while (choice != 3);

	}

	public static void homePage() {
		System.out.println("\t\t\t ---------------------------------------------");
		System.out.println("\t\t\t|                 MAIN MENU                   |");
		System.out.println("\t\t\t ---------------------------------------------");

		System.out.println("\n1.Hotel");
		System.out.println("2.Customer");
		System.out.println("3.Exit\n");
		System.out.print("Enter Your Option : ");
	}

}
