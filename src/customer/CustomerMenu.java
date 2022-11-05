package customer;

public class CustomerMenu {
	
	static void customerHomePage() {
		System.out.println("\n\t\t\t ---------------------------------------------");
		System.out.println("\t\t\t|                    Customer                 |");
		System.out.println("\t\t\t ---------------------------------------------");

		System.out.println("1.Sign Up");
		System.out.println("2.Log in");
		System.out.println("3.Go Back");
		System.out.print("Enter Your Option : ");

	}
	
	static void customerLoginMenu() {
		System.out.println("\n1.Order Food");
		System.out.println("2.View Cart");
		System.out.println("3.Track Order Status");
		System.out.println("4.View Recent Orders");
		System.out.println("5.Account Settings");
		System.out.println("6.Logout");
		System.out.print("Enter Your Option : ");
	}

	static void accountSettings() {
		System.out.println("\n1.Change Name");
		System.out.println("2.Change Address");
		System.out.println("3.Change Pincode");
		System.out.println("4.Change Mail Id");
		System.out.println("5.Change Phone No");
		System.out.println("6.Change Password");
		System.out.println("7.Go Back");
		System.out.print("Enter Your Option : ");
	}
}
