package utility;

public abstract class HomePage {
	
	static {
		System.out.println("\t\t\t\t\t----------Welcome to Ziggy----------\n");
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
