package customer;

import java.util.Set;
import java.util.HashMap;

import hotel.Hotel;
import hotel.HotelDB;

import utility.Data;
import utility.Input;
import utility.Order;
import utility.Register;

public class CustomerMain extends Register {

    private static final CustomerDB CUSTOMER_DB = CustomerDB.getInstance();

    @Override
    public void mainMenu() {
        System.out.println("\n\t\t\t ---------------------------------------------");
        System.out.println("\t\t\t|                    Customer                 |");
        System.out.println("\t\t\t ---------------------------------------------");
        int choice;
        do {
            super.displayMainMenuOptions();
            choice = Input.getInteger(false);

            switch (choice) {
                case 1: {
                    String mailId = super.signup(false);
                    if (mailId != null)
                        successfulLogin(mailId);
                    break;
                }
                case 2: {
                    String mailId = super.login(false);
                    if (mailId != null)
                        successfulLogin(mailId);
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

    @Override
    public void successfulLogin(String mailId) {

        Customer customer = CUSTOMER_DB.getCustomerList().get(mailId);
        System.out.println("\n....Welcome Mr." + customer.getName() + "....\n");

        if (customer.getUpi() == null) {
            System.out.println("Your UPI details hasn't been added yet...You can't make orders until you add UPI");
            System.out.print("Do you want to add your UPI details now (1-Y/2-N) : ");
            int option = Input.getInteger(1, 2, false);

            if (option == 1 && isUpiDetailsAddedSuccessfully(customer, true))
                System.out.println("UPI details added successfully....");


        }

        int choice;
        do {
            displayHomePageOptions();
            choice = Input.getInteger(false);
            switch (choice) {
                case 1: {
                    orderFood(customer);
                    break;
                }
                case 2: {
                    cart(customer);
                    break;
                }
                case 3: {
                    trackOrderStatus(customer);
                    break;
                }
                case 4: {
                    ViewRecentOrders(customer);
                    break;
                }
                case 5: {
                    editAccount(customer);
                    break;
                }
                case 6: {
                    return;
                }
                default: {
                    System.out.println("Invalid Option...Try again");
                }
            }

        } while (true);
    }

    private void orderFood(Customer customer) {

        HotelDB hotelDB = HotelDB.getInstance();
        if (!hotelDB.isServiceable(customer.getPincode())) {
            System.out.println("\nNo Hotels Were found near your area to serve you.....sorry");
            return;
        }

        Set<String> set = hotelDB.getHotelList().keySet();
        if (!set.isEmpty()) {
            System.out.println("\n----------------------------------------");
            System.out.println("Id\t\tName\t\tCategory");
            System.out.println("----------------------------------------");
        }

        HashMap<String, Hotel> hotelList = new HashMap<>(); // hotels with same pincodes (mailId,hotel)

        for (String hotelMailId : set) {
            if (hotelDB.getHotelList().get(hotelMailId).isHotelServiceable(customer.getPincode())) {
                hotelList.put(hotelMailId, hotelDB.getHotelList().get(hotelMailId));
                hotelList.get(hotelMailId).printHotelOverview();
            }
        }

        System.out.print("\nEnter Hotel Id : ");

        int hotelId = Input.getInteger(true);

        if (hotelId == -1)
            return;

        Hotel hotel = null;

        boolean isIdFound = false;

        set = hotelList.keySet();
        for (String mailId : set) {
            if (hotelList.get(mailId).getHotelId() == hotelId) {
                hotel = hotelDB.getHotelList().get(mailId);
                isIdFound = true;
                break;
            }
        }
        if (!isIdFound) {
            System.out.println("\nHotel Id Not Found.....Try again");
            return;
        }

        if (hotel.getDishes().isEmpty() && hotel.getDrinks().isEmpty()) {
            System.out.println("\nThis hotels doesn't accept any orders at the moment....Try again later");
            return;
        }

        hotel.displayDishes(false);

        HashMap<Integer, Integer> dishes = new HashMap<>(); // dishId,quantity
        HashMap<Integer, Integer> drinks = new HashMap<>(); // dishId,quantity

        HashMap<Integer, HashMap<String, Double>> extras = new HashMap<>(); // dishId,extras<dishId,price>

        int option;

        do {

            System.out.print("\nEnter dish ID : ");
            int dishId = Input.getInteger(1000, 9999, false);

            boolean isDishIdInDishes = hotel.getDishes().containsKey(dishId);

            if (isDishIdInDishes || hotel.getDrinks().containsKey(dishId)) {
                System.out.print("Enter Quantity : ");
                int quantity;

                do {
                    quantity = Input.getInteger(false);
                    if (quantity == 0)
                        System.out.print("Enter valid quantity : ");
                } while (quantity == 0);

                if (isDishIdInDishes) {
                    dishes.put(dishId, quantity);

                    set = hotel.getDishes().get(dishId).getExtras().keySet();
                    if (!set.isEmpty()) {
                        HashMap<String, Double> extra = new HashMap<>();

                        for (String extrasName : set) {
                            System.out.println(
                                    extrasName + "\t\t" + hotel.getDishes().get(dishId).getExtras().get(extrasName));
                            System.out.print(
                                    "Do you want this " + hotel.getDishes().get(dishId).getSideDishName() + " (1-Y/2-N) :");
                            option = Input.getInteger(1, 2, false);

                            if (option == 1)
                                extra.put(extrasName, hotel.getDishes().get(dishId).getExtras().get(extrasName));

                        }
                        extras.put(dishId, extra);
                    }
                } else {
                    drinks.put(dishId, quantity);
                }

            } else {
                System.out.println("\nInvalid dish Id.....Try again");
            }

            System.out.print("\nDo u want to order more (Press (1-Y / 2-N) : ");
            option = Input.getInteger(1, 2, true);
        } while (option == 1);

        if (!dishes.isEmpty() || !drinks.isEmpty()) {
            Order order = new Order(dishes, drinks, extras, customer.getMailId(), hotel.getMailId());
            customer.newOrder(order);
            cart(customer);
        }

    }

    private void cart(Customer customer) {
        if (customer.getPendingOrders().isEmpty()) {
            System.out.println("\nCart is Empty....Try again later...");
            return;
        }

        System.out.println("\n\t\t\t ---------------------------------------------");
        System.out.println("\t\t\t|                     Cart                    |");
        System.out.println("\t\t\t ---------------------------------------------");

        Set<Integer> set = customer.getPendingOrders().keySet();
        for (int orderId : set) {
            System.out.println("----------------------------------------");
            System.out.println(customer.getPendingOrders().get(orderId).getOrderDetailsForCustomer());
            System.out.println("----------------------------------------");
        }

        int orderId;
        Order order;
        int attempt = 1;

        System.out.print("Enter Order Id : ");
        do {
            orderId = Input.getInteger(true);
            if (orderId == -1)
                return;
            if (!set.contains(orderId)) {
                if (Input.isAttemptExceeded(attempt++))
                    return;
                System.out.print("Invalid order Id....Enter again : ");
            } else {
                order = customer.getPendingOrders().get(orderId);
                break;
            }
        } while (true);

        System.out.println("\n1.Confirm Order");
        System.out.println("2.Cancel Order");
        System.out.println("3.Go Back");
        System.out.print("Enter Option : ");

        int option = Input.getInteger(1, 3, false);

        String orderDetail;
        if (option == 1) {
            double price = order.calculatePrice();
            System.out.println("Total Price : Rs." + price);
            order.setPrice(price);

            System.out.print("\nPress 1 for proceed to payment or 2 for exit : ");
            int choice = Input.getInteger(1, 2, false);

            if (choice == 2)
                return;

            boolean isUpiAlreadyAdded = true;

            if (customer.getUpi() == null) {
                isUpiAlreadyAdded = false;
                System.out.println("\nYou have to add you UPI details before ordering food");
                System.out.print("Do you want to add your UPI details (1-Y,2-N) : ");
                option = Input.getInteger(1, 2, false);
                if (option == 2)
                    return;

                if (!isUpiDetailsAddedSuccessfully(customer, true))
                    return;

            }
            if (isUpiAlreadyAdded) {
                System.out.print("\nEnter UPI Id : ");
                int upiId = Input.getInteger(true);
                if (upiId == -1)
                    return;

                System.out.print("Enter UPI PIN : ");
                int upiPin = Input.getInteger(true);
                if (upiPin == -1)
                    return;
                if (customer.getUpi().getUpiId() != upiId || customer.getUpi().getUpiPin() != upiPin) {
                    System.out.println("\nInvalid Credentials...\n");
                    return;
                }
            }

            if (customer.getUpi().getBalance() < price) {
                System.out.println("\nInsufficient Balance...Try again later\n");
                return;
            }
            HotelDB.getInstance().getHotelList().get(order.getHotelMail()).newOrder(order);

            customer.getUpi().payMoney(price);
            orderDetail = order.getOrderDetailsForCustomer() + "\nOrder Status : Paid\n" + Input.getDateTime();
            customer.addCompletedOrder(orderId, orderDetail);
            System.out.println("\nPaid successfully...Your order will be delivered soon...\n");

        } else if (option == 2) {
            orderDetail = order.getOrderDetailsForCustomer() + "\nOrder Status : Cancelled\n" + Input.getDateTime();
            customer.addCompletedOrder(orderId, orderDetail);
            System.out.println("\nOrder cancelled successfully\n");
        }

    }

    private void trackOrderStatus(Customer customer) {
        System.out.print("\nEnter Order Id : ");

        int orderId = Input.getInteger(true);
        if (orderId == -1)
            return;

        if (customer.getCompletedOrders().containsKey(orderId))
            System.out.println("\n" + customer.getCompletedOrders().get(orderId));
        else
            System.out.println("\nInvalid order id\n");

        System.out.print("Press 1 to track another order or 2 to go back : ");

        int option = Input.getInteger(1, 2, true);

        if (option == 1)
            trackOrderStatus(customer);

    }

    @Override
    public void ViewRecentOrders(Data data) {
        Customer customer = (Customer) data;
        if (customer.getCompletedOrders().isEmpty()) {
            System.out.println("\nNo orders were delivered...Try again later\n");
            return;
        }
        System.out.println();
        Set<Integer> set = customer.getCompletedOrders().keySet();
        for (int orderId : set) {
            System.out.println(customer.getCompletedOrders().get(orderId));
        }
    }

    @Override
    public void editAccount(Data d) {

        Customer customer = (Customer) d;

        int choice;
        do {

            displayAccountSettingsOptions();
            choice = Input.getInteger(false);
            switch (choice) {
                case 1: {
                    System.out.println("\nCurrent Name : " + customer.getName() + "\n");
                    String name = super.getNewName("Customer", true);
                    if (name == null)
                        break;
                    customer.setName(name);
                    System.out.println("\n\u001b[36m" + "Name Changed Successfully" + "\u001b[0m");
                    break;
                }
                case 2: {
                    System.out.println("\nCurrent Address : " + customer.getAddress() + "\n");
                    String address = super.getNewAddress(true);
                    if (address == null)
                        break;
                    customer.setAddress(address);
                    System.out.println("\n\u001b[36m" + "Address Changed Successfully" + "\u001b[0m");
                    break;
                }
                case 3: {
                    System.out.println("\nCurrent Pincode : " + customer.getPincode() + "\n");
                    int pincode = super.getNewPincode(true);
                    if (pincode == -1)
                        break;

                    customer.setPincode(pincode);
                    System.out.println("\n\u001b[36m" + "Pincode Changed Successfully" + "\u001b[0m");

                    break;
                }
                case 4: {
                    System.out.println("\nCurrent Mail Id : " + customer.getMailId() + "\n");
                    String mailId = super.getNewMailId(true, true);
                    if (mailId == null)
                        break;

                    CUSTOMER_DB.removeCustomer(customer.getMailId());
                    customer.setMailId(mailId);
                    CUSTOMER_DB.addCustomer(customer);
                    System.out.println("\n\u001b[36m" + "Mail Id Changed Successfully" + "\u001b[0m");

                    Set<Integer> set = customer.getPendingOrders().keySet();
                    for (int orderId : set) {
                        customer.getPendingOrders().get(orderId).setCustomerMail(mailId);
                    }

                    break;
                }
                case 5: {
                    System.out.println("\nCurrent Phone No : " + customer.getPhoneNo() + "\n");
                    String phoneNo = super.getNewPhoneNo(true);
                    if (phoneNo == null)
                        break;

                    customer.setPhoneNo(phoneNo);
                    System.out.println("\n\u001b[36m" + "Phone No Changed Successfully" + "\u001b[0m");
                    break;
                }

                case 6: {
                    System.out.print("Enter Old Password : ");
                    String password = Input.getString(false, false);
                    if (password.equals(customer.getPassword())) {

                        String newPassword;
                        if ((newPassword = super.getNewPassword(true)) != null) {
                            customer.setPassword(newPassword);
                            System.out.println("\n\u001b[36m" + "Password Changed Successfully" + "\u001b[0m");
                        }

                    } else {
                        System.out.println("\nIncorrect Password");
                    }
                    break;
                }
                case 7: {
                    if (isUpiDetailsAddedSuccessfully(customer, true))
                        System.out.println("\n\u001b[36m" + "UPI Details Updated Successfully" + "\u001b[0m");
                    break;
                }
                case 8: {
                    return;
                }
                default: {
                    System.out.println("\nInvalid Option...Try again....\n");
                }
            }
        } while (true);

    }

    public boolean isUpiDetailsAddedSuccessfully(Customer customer, boolean limit) {
        System.out.print("Enter 6 digits UPI ID : ");
        int upiId = Input.getInteger(100000, 999999, limit);
        if (upiId == -1)
            return false;
        System.out.print("Enter UPI Pin : ");
        int upiPin = Input.getInteger(1000, 9999, limit);
        if (upiPin == -1)
            return false;
        customer.addUPIDetails(upiId, upiPin);
        return true;
    }

    @Override
    public void displayHomePageOptions() {
        System.out.println("\n1.Order Food");
        System.out.println("2.View Cart");
        System.out.println("3.Track Order Status");
        System.out.println("4.View Recent Orders");
        System.out.println("5.Account Settings");
        System.out.println("6.Logout");
        System.out.print("\nEnter Your Option : ");
    }

    @Override
    public void displayAccountSettingsOptions() {
        System.out.println("\n1.Change Name");
        System.out.println("2.Change Address");
        System.out.println("3.Change Pincode");
        System.out.println("4.Change Mail Id");
        System.out.println("5.Change Phone No");
        System.out.println("6.Change Password");
        System.out.println("7.Change UPI Details");
        System.out.println("8.Go Back");
        System.out.print("\nEnter Your Option : ");
    }

}
