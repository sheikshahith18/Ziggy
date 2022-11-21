package utility;

public interface UserInterface {

    default void homeScreen(){
        System.out.println("1.Register");
        System.out.println("2.Log in");
        System.out.println("3.Go Back");
        System.out.print("\nEnter Your Option : ");
    }
    void mainMenu();


    void loginScreen();
    void successfulLogin(String mailId);


    void accountSettingsScreen();
    void editAccount(Data data);


    void orderHistory(Data data);


}
