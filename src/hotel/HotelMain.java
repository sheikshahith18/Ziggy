package hotel;

import java.util.HashMap;
import java.util.Set;

import customer.CustomerDB;

import utility.Data;
import utility.Input;
import utility.Order;
import utility.Register;


public class HotelMain extends Register {

    private static final HotelDB HOTEL_DB = HotelDB.getInstance();

    @Override
    public void mainMenu() {
        System.out.println("\t\t\t ---------------------------------------------");
        System.out.println("\t\t\t|                     Hotel                   |");
        System.out.println("\t\t\t ---------------------------------------------");
        int choice;
        do {
            displayMainMenuOptions();
            choice = Input.getInteger(false);

            switch (choice) {
                case 1: {
                    successfulLogin(super.signup(true));
                    break;
                }
                case 2: {
                    String mailId = super.login(true);
                    if (mailId != null)
                        successfulLogin(mailId);
                    break;
                }
                case 3: {
                    return;
                }
                default: {
                    System.out.println("\nInvalid Option...Try again....");
                }
            }

        } while (true);
    }

    @Override
    public void successfulLogin(String mailId) {
        Hotel hotel = HOTEL_DB.getHotelList().get(mailId);
        System.out.println("\n....Welcome Mr." + hotel.getOwnerName() + "....\n");
        int choice;
        do {

            displayHomePageOptions();
            choice = Input.getInteger(false);
            switch (choice) {

                case 1: {
                    addDish(hotel);
                    break;
                }
                case 2: {
                    viewAddedDishes(hotel);
                    break;
                }
                case 3: {
                    updateDish(hotel);
                    break;
                }
                case 4: {
                    removeDish(hotel);
                    break;
                }
                case 5: {
                    updatePendingOrders(hotel);
                    break;
                }
                case 6: {
                    ViewRecentOrders(hotel);
                    break;
                }
                case 7: {
                    editAccount(hotel);
                    break;
                }
                case 8: {
                    return;
                }
                default: {
                    System.out.println("\nInvalid Option...Try again....");
                }
            }

        } while (true);
    }

    private void addDish(Hotel hotel) {

        int choice;

        do {
            System.out.println("1.Dish");
            System.out.println("2.Drinks");
            System.out.println("3.Go back");
            System.out.print("Enter your option : ");

            choice = Input.getInteger(false);

            switch (choice) {
                case 1: {
                    Dish dish = addDish(true, hotel.isHotelVeg());
                    hotel.addDish(dish);
                    System.out.println("\n\u001b[36m" + "Dish Added Successfully..." + "\u001b[0m\n");
                    break;
                }

                case 2: {
                    Dish drink = addDish(false, hotel.isHotelVeg());
                    hotel.addDrink(drink);
                    System.out.println("\n\u001b[36m" + "Drink Added Successfully..." + "\u001b[0m\n");
                    break;
                }
                case 3: {
                    return;
                }
                default: {
                    System.out.println("\nInvalid Option...Try again....\n");
                }
            }

        } while (true);
    }

    private Dish addDish(boolean isDish, boolean isVeg) {

        System.out.print("Enter Food Name : ");
        String dishName = Input.getString(false, false);

        System.out.print("Enter Price : ");
        double dishPrice = Input.getDouble(false);

        System.out.print("Enter Quantity : ");
        int dishQuantity = Input.getInteger(false);

        String dishCategory = "Veg";

        if (!isVeg) {
            System.out.print("Enter Category ( 1.Veg / 2.Non-Veg ) : ");
            int choice = Input.getInteger(1, 2, false);
            if (choice == 2)
                dishCategory = "Non-Veg";
        }

        if (isDish) {

            System.out.print("Do you want to add sidedish for this dish (1-Y/2-N) : ");
            int option = Input.getInteger(1, 2, false);
            HashMap<String, Double> extras = new HashMap<>();

            String sideDishName;

            if (option == 1) {
                System.out.print("1-Toppings/2-Sidedish : ");
                option = Input.getInteger(1, 2, false);
                if (option == 1)
                    sideDishName = "Topping";
                else
                    sideDishName = "Sidedish";

                do {
                    System.out.print("Enter " + sideDishName + "'s name : ");
                    String name = Input.getString(false, false);
                    System.out.print("Enter " + sideDishName + "'s price : ");
                    double sideDishPrice = Input.getDouble(false);
                    extras.put(name, sideDishPrice);
                    System.out.print("Do you want to add more " + sideDishName + " (1-Y/2-N) : ");
                    option = Input.getInteger(1, 2, false);
                } while (option == 1);
                return new Dish(dishName, dishPrice, dishQuantity, dishCategory, sideDishName, extras);

            } else {
                return new Dish(dishName, dishPrice, dishQuantity, dishCategory);

            }
        } else {
            System.out.print("Enter Temperature (1-Cold/2-Hot) : ");

            int temperature = Input.getInteger(1, 2, false);

            return new Dish(dishName, dishPrice, dishQuantity, dishCategory, temperature == 1 ? "Cold" : "Hot");
        }
    }

    private void viewAddedDishes(Hotel hotel) {
        if (hotel.getDishes().isEmpty() && hotel.getDrinks().isEmpty()) {
            System.out.println("\nNo Dishes were added...Add some before updating\n");
            return;
        }
        hotel.displayDishes(true);
    }

    private void updateDish(Hotel hotel) {

        if (hotel.getDishes().isEmpty() && hotel.getDrinks().isEmpty()) {
            System.out.println("\nNo Dishes were added...Add some before updating\n");
            return;
        }

        int option;

        do {
            hotel.displayDishes(true);
            System.out.print("\nEnter Dish ID : ");
            int dishId = Input.getInteger(true);
            if (dishId == -1)
                return;

            if (hotel.getDishes().containsKey(dishId)) {
                updateDish(hotel, dishId, true);
            } else if (hotel.getDrinks().containsKey(dishId)) {
                updateDish(hotel, dishId, false);
            } else {
                System.out.println("\nInvalid Dish Id");
            }
            System.out.print("Press 1 to update again or 2 to exit : ");
            option = Input.getInteger(1, 2, true);
        } while (option == 1);

    }

    private void updateDish(Hotel hotel, int dishId, boolean isDish) {
        Dish d;
        if (isDish)
            d = hotel.getDishes().get(dishId);
        else
            d = hotel.getDrinks().get(dishId);

        int choice;
        do {
            boolean isSideDishPresent = !d.getExtras().isEmpty();
            displayUpdateDishMenuOptions(d.getSideDishName(), !isDish);
            choice = Input.getInteger(false);
            switch (choice) {

                case 1: {
                    System.out.print("\nEnter Name : ");
                    String name = Input.getString(false, true);
                    if (name == null)
                        break;
                    System.out.println("\n\u001b[36m" + "Name Changed Successfully" + "\u001b[0m");
                    d.setName(name);
                    break;
                }
                case 2: {
                    System.out.print("\nEnter Price : ");
                    double price = Input.getDouble(true);
                    if (price == -1)
                        break;
                    System.out.println("\n\u001b[36m" + "Price Updated Successfully" + "\u001b[0m");
                    d.setPrice(price);
                    break;
                }
                case 3: {
                    System.out.print("\nEnter Quantity : ");
                    int quantity = Input.getInteger(true);
                    if (quantity == -1)
                        break;
                    System.out.println("\n\u001b[36m" + "Quantity Updated Successfully" + "\u001b[0m");
                    d.setQuantity(quantity);
                    break;
                }
                case 4: {
                    if (!isDish) {
                        System.out.print("Set Temperature (1-Cold/2-Hot) : ");
                        int temperature = Input.getInteger(1, 2, true);
                        if (temperature == -1)
                            break;
                        if (temperature == 1)
                            d.setTemperature("cold");
                        else
                            d.setTemperature("hot");
                    } else if (isSideDishPresent) {
                        System.out.print("Enter " + d.getSideDishName() + "'s name : ");
                        String name = Input.getString(false, false);
                        System.out.print("Enter " + d.getSideDishName() + "'s price : ");
                        double price = Input.getDouble(false);
                        if (price == -1)
                            break;
                        d.addExtras(name, price);
                        System.out.println("\u001b[36m" + d.getSideDishName() + " added successfully" + "\u001b[0m");
                        break;
                    } else
                        return;

                    break;
                }
                case 5: {
                    if (!isDish)
                        return;

                    if (!isSideDishPresent) {
                        System.out.println("\nInvalid Option...Try again....");
                        break;
                    }

                    System.out.print("Enter " + d.getSideDishName() + "'s name : ");
                    String name = Input.getString(false, false);
                    if (d.getExtras().containsKey(name)) {
                        d.getExtras().remove(name);
                        System.out.println("\u001b[36m" + d.getSideDishName() + " removed successfully" + "\u001b[0m");
                    } else {
                        System.out.println(d.getSideDishName() + " not found");
                    }
                    break;
                }
                case 6: {
                    if (isSideDishPresent)
                        return;
                }
                default: {
                    System.out.println("\nInvalid Option...Try again....");
                }
            }

        } while (true);
    }

    private void removeDish(Hotel hotel) {

        if (hotel.getDishes().isEmpty() && hotel.getDrinks().isEmpty()) {
            System.out.println("\nNo Dishes were added...Add some before removing\n");
            return;
        }

        hotel.displayDishes(true);

        int dishId;
        System.out.print("\nEnter Dish Id : ");
        dishId = Input.getInteger(true);

        boolean isDish;

        if ((isDish = hotel.getDishes().containsKey(dishId)) || hotel.getDrinks().containsKey(dishId)) {
            System.out.print("Do you really wish to remove the dish (1-Y/2-N) : ");
            int option = Input.getInteger(1, 2, true);
            if (option == 2 || option == -1)
                return;
            if (isDish)
                hotel.getDishes().remove(dishId);
            else
                hotel.getDrinks().remove(dishId);

            System.out.println("\nDish removed successfully....\n");
        } else {
            System.out.println("\nInvalid Dish Id\n");
        }
    }

    private void updatePendingOrders(Hotel hotel) {

        if (hotel.getPendingOrders().isEmpty()) {
            System.out.println("\nNo orders available...try again later");
            return;
        }

        System.out.println();

        Set<Integer> set = hotel.getPendingOrders().keySet();
        for (int orderID : set) {
            System.out.println("----------------------------------------");
            System.out.println(hotel.getPendingOrders().get(orderID).getOrderDetailsForHotel());
            System.out.println("----------------------------------------");
        }

        System.out.print("Enter Order Id : ");
        Order order;
        int orderId;
        int attempt = 1;
        do {
            orderId = Input.getInteger(true);
            if (!set.contains(orderId)) {
                if (Input.isAttemptExceeded(attempt++) || orderId == -1)
                    return;
                System.out.print("Invalid order Id....Enter again : ");
            } else {
                order = hotel.getPendingOrders().get(orderId);
                break;
            }
        } while (true);

        System.out.println("\n1.Deliver Order");
        System.out.println("2.Cancel Order");
        System.out.println("3.Go Back");
        System.out.print("Enter Option : ");

        int option = Input.getInteger(1, 3, false);

        String orderDetail = "";
        boolean isOrderAvailable = hotel.isOrderAvailable(order);

        switch (option) {
            case 1: {
                if (isOrderAvailable) {
                    hotel.deliverOrder(order);
                    orderDetail = order.getOrderDetailsForHotel() + "\nOrder Status : Delivered Successfully\n"
                            + Input.getDateTime();
                    System.out.println("\nOrder delivered Successfully");
                    break;
                }
                orderDetail = order.getOrderDetailsForHotel() + "\nOrder Status : Refunded\n" + Input.getDateTime();
                System.out.println("\nInsufficient Products");

            }
            case 2: {
                if (option == 2) {
                    orderDetail = order.getOrderDetailsForHotel() + "\nOrder Status : Cancelled by hotel\n" + Input.getDateTime();
                    System.out.println("\nOrder Cancelled");
                }

                CustomerDB.getInstance().getCustomerList().get(order.getCustomerMail()).getUpi()
                        .refundMoney(order.getPrice());
                System.out.println("Amount Refunded to Customer\n");
                break;
            }
            case 3: {
                return;
            }
        }

        hotel.addCompletedOrder(orderId, orderDetail);

        CustomerDB.getInstance().getCustomerList().get(order.getCustomerMail()).addCompletedOrder(order.getOrderId(),
                orderDetail);
    }

    @Override
    public void ViewRecentOrders(Data data) {

        Hotel hotel = (Hotel) data;
        if (hotel.getCompletedOrders().isEmpty()) {
            System.out.println("\nNo Delivery were made...Try again later");
            return;
        }
        System.out.println();
        Set<Integer> set = hotel.getCompletedOrders().keySet();
        for (int orderId : set) {
            System.out.println(hotel.getCompletedOrders().get(orderId));
        }

    }

    @Override
    public void editAccount(Data data) {
        Hotel hotel = (Hotel) (data);
        int choice;
        do {

            displayAccountSettingsOptions();
            choice = Input.getInteger(false);
            switch (choice) {
                case 1: {
                    System.out.println("\nCurrent Hotel Name : " + hotel.getName() + "\n");
                    String name = super.getNewName("Hotel", true);
                    if (name == null)
                        break;
                    hotel.setName(name);
                    System.out.println("\n\u001b[36m" + "Name Changed Successfully" + "\u001b[0m");
                    break;
                }
                case 2: {
                    System.out.println("\nCurrent Owner's Name : " + hotel.getOwnerName() + "\n");
                    String ownerName = super.getNewName("Owner", true);
                    if (ownerName == null)
                        break;

                    hotel.setOwnerName(ownerName);
                    System.out.println("\n\u001b[36m" + "Owner's Name Changed Successfully" + "\u001b[0m");
                    break;
                }
                case 3: {
                    System.out.println("\nCurrent Adress : " + hotel.getAddress() + "\n");
                    String address = super.getNewAddress(true);
                    if (address == null)
                        break;
                    hotel.setAddress(address);
                    System.out.println("\n\u001b[36m" + "Address Changed Successfully" + "\u001b[0m");
                    break;
                }
                case 4: {
                    System.out.println("\nCurrent Pincode : " + hotel.getPincode() + "\n");

                    int pincode = super.getNewPincode(true);
                    if (pincode == -1)
                        break;

                    HOTEL_DB.removeServiceablePincode(hotel.getPincode());
                    hotel.setPincode(pincode);
                    HOTEL_DB.addServiceablePincode(pincode);
                    System.out.println("\n\u001b[36m" + "Pincode Changed Successfully" + "\u001b[0m");

                    break;
                }
                case 5: {
                    System.out.println("\nCurrent Mail Id : " + hotel.getMailId() + "\n");
                    String mailId = super.getNewMailId(true, true);
                    if (mailId == null)
                        break;
                    HOTEL_DB.removeHotel(hotel.getMailId());
                    hotel.setMailId(mailId);
                    HOTEL_DB.addHotel(hotel);
                    System.out.println("\n\u001b[36m" + "Mail Id Changed Successfully" + "\u001b[0m");

                    Set<Integer> set = hotel.getPendingOrders().keySet();
                    for (int orderId : set) {
                        hotel.getPendingOrders().get(orderId).setHotelMail(mailId);
                    }
                    break;
                }
                case 6: {
                    System.out.println("\nCurrent Phone No : " + hotel.getPhoneNo() + "\n");
                    String phoneNo = super.getNewPhoneNo(true);
                    if (phoneNo == null)
                        break;

                    hotel.setPhoneNo(phoneNo);
                    System.out.println("\n\u001b[36m" + "Phone No Changed Successfully" + "\u001b[0m");
                    break;
                }
                case 7: {
                    System.out.println("\nCurrent Category : " + hotel.getCategory() + "\n");
                    System.out.print("Enter Category ( 1.Veg / 2.Non-Veg ) : ");
                    int option = Input.getInteger(1, 2, true);
                    if (option == -1)
                        break;
                    boolean isVeg = false;
                    if (option == 1)
                        isVeg = true;

                    hotel.setCategory(isVeg);

                    System.out.println("\n\u001b[36m" + "Category Changed Successfully" + "\u001b[0m");
                    break;
                }
                case 8: {
                    System.out.print("Enter Old Password : ");
                    String password = Input.getString(false, false);
                    if (password.equals(hotel.getPassword())) {

                        String newPassword;
                        if ((newPassword = super.getNewPassword(true)) != null) {
                            hotel.setPassword(newPassword);
                            System.out.println("\n\u001b[36m" + "Password Changed Successfully" + "\u001b[0m");
                        }

                    } else {
                        System.out.println("\nIncorrect Password");
                    }

                    break;
                }
                case 9: {
                    return;
                }
                default: {
                    System.out.println("\nInvalid Option...Try again....\n");
                }
            }
        } while (true);
    }

    @Override
    public void displayHomePageOptions() {
        System.out.println("\n1.Add Dish");
        System.out.println("2.View Added Dishes");
        System.out.println("3.Update Dish");
        System.out.println("4.Remove Dish");
        System.out.println("5.View Orders");
        System.out.println("6.Order History");
        System.out.println("7.Account Settings");
        System.out.println("8.Logout");
        System.out.print("\nEnter Your Option : ");
    }

    @Override
    public void displayAccountSettingsOptions() {
        System.out.println("\n1.Change Hotel Name");
        System.out.println("2.Change Owner Name");
        System.out.println("3.Change Address");
        System.out.println("4.Change Pincode");
        System.out.println("5.Change Mail Id");
        System.out.println("6.Change Phone No");
        System.out.println("7.Change Category");
        System.out.println("8.Change Password");
        System.out.println("9.Go Back");
        System.out.print("\nEnter Your Option : ");
    }

    private void displayUpdateDishMenuOptions(String sideDishName, boolean isDrink) {
        System.out.println("\n1.Change Name");
        System.out.println("2.Change Price");
        System.out.println("3.Change Quantity");
        if (sideDishName != null) {
            System.out.println("4.Add " + sideDishName);
            System.out.println("5.Remove " + sideDishName);
            System.out.println("6.Go back");
        } else if (isDrink) {
            System.out.println("4.Change Temperature");
            System.out.println("5.Go Back");
        } else {
            System.out.println("4.Go back");
        }
        System.out.print("\nEnter your option : ");
    }

}
