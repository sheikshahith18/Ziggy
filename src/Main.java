import hotel.HotelMain;

import customer.CustomerMain;

import utility.Init;
import utility.Input;


public class Main {

	public static void main(String[] args) {

		System.out.println("\t\t\t\t\t----------Welcome to Ziggy----------\n");

		Init.initializeHotel();
		Init.initializeCustomer();

//				  HOTEL LOGIN : 
//				  username : hotel@gmail.com 
//				  password : Qwerty@123 

//		  		  CUSTOMER LOGIN : 
//		  		  username : customer1@gmail.com , customer2@gmail.com 
//		  		  password : Qwerty@123

//				  UPI ID   : 1234
//				  UPI PIN  : 1234

		int choice;
		do {

			homePage();
			choice = Input.getInteger(false);

			switch (choice) {
			case 1: {

				HotelMain hotelMain = new HotelMain();
				hotelMain.mainMenu();
				break;
			}
			case 2: {

				CustomerMain customerMain = new CustomerMain();
				customerMain.mainMenu();
				break;
			}
			case 3: {
				break;
			}
			default: {

				System.out.println("Invalid Option...Try again....");
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
