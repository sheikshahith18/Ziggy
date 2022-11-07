package hotel;

public class HotelMenu {

	public static void hotelHomePage() {
		System.out.println("\t\t\t ---------------------------------------------");
		System.out.println("\t\t\t|                     Hotel                   |");
		System.out.println("\t\t\t ---------------------------------------------");

		System.out.println("\n1.Register");
		System.out.println("2.Login");
		System.out.println("3.Go Back");
		System.out.print("Enter Your Option : ");
	}

	public static void hotelLoginMenu() {

		System.out.println("\n1.Add Dish");
		System.out.println("2.Update Dish");
		System.out.println("3.View Orders");
		System.out.println("4.Order History");
		System.out.println("5.Account Settings");
		System.out.println("6.Logout");
		System.out.print("Enter Your Option : ");
	}

	public static void dishMenu() {
		System.out.println("\n1.Dish");
		System.out.println("2.Pizza");
		System.out.println("3.Chicken");
		System.out.println("4.Drinks");
		System.out.println("5.Go Back");
		System.out.print("Enter Your Option : ");
	}

	public static void updateDishMenu() {
		System.out.println("\n1.Change Name");
		System.out.println("2.Change Price");
		System.out.println("3.Change Quantity");
	}

	public static void updatePizzaMenu() {
		updateDishMenu();
		System.out.println("4.Add Toppings");
		System.out.println("5.Remove Topping");
		System.out.println("6.Delete Dish");
		System.out.println("7.Go Back");
		System.out.print("Enter Your Option : ");
	}

	public static void updateChickenMenu() {
		updateDishMenu();
		System.out.println("4.Add Extras");
		System.out.println("5.Remove Extras");
		System.out.println("6.Delete Dish");
		System.out.println("7.Go Back");
		System.out.print("Enter Your Option : ");
	}

	public static void updateDrinksMenu() {
		updateDishMenu();
		System.out.println("4.Change Temperature");
		System.out.println("5.Delete Dish");
		System.out.println("6.Go Back");
		System.out.print("Enter Your Option : ");
	}

	public static void accountSettings() {
		System.out.println("\n1.Change Name");
		System.out.println("2.Change Address");
		System.out.println("3.Change Pincode");
		System.out.println("4.Change Mail Id");
		System.out.println("5.Change Phone No");
		System.out.println("6.Change Owner Name");
		System.out.println("7.Change Category");
		System.out.println("8.Change Password");
		System.out.println("9.Go Back");
		System.out.print("Enter Your Option : ");
	}

}
