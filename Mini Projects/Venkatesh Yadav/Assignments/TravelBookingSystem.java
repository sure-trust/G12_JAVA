package Day1;

import java.util.Scanner;

interface Booking {
    void bookTicket();

}

class FlightBooking implements Booking {
    @Override
    public void bookTicket() {
        System.out.println("Flight ticket booked successfully!");
    }
}

class BusBooking implements Booking {
    @Override
    public void bookTicket() {
        System.out.println("Bus ticket booked successfully!");
    }
}

public class TravelBookingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select booking type:");
        System.out.println("1. Flight Booking");
        System.out.println("2. Bus Booking");
        System.out.print("Enter the type :");
        int choice = scanner.nextInt();

        if (choice == 1) {
            System.out.println("Select the flight :");
            System.out.println("1. Air Express \n 2 Sky Voyager \n 3 StarJet \n 4 SwiftWings");
            int flightType = scanner.nextInt();
            System.out.print("Enter the type : ");
            System.out.println();

            if (flightType == 1) {
                System.out.println("You Selected Air Express");
            } else if (flightType == 2) {
                System.out.println("You Selected Sky Voyager");
            } else if (flightType == 3) {
                System.out.println("You Selected StarJet");
            } else if (flightType == 4) {
                System.out.println("You Selected SwiftWings");
            }
            Booking flightBooking = new FlightBooking();
            flightBooking.bookTicket();
        } else if (choice == 2) {
            System.out.println("Select the flight :");
            System.out.println(" 1.Travel Express \n 2 City Cruiser \n 3 MetroBus \n 4 SwiftTransit");
            int busType = scanner.nextInt();
            System.out.print("Enter the type :");
            System.out.println();

            if (busType == 1) {
                System.out.println("You Selected Travel Express");
            } else if (busType == 2) {
                System.out.println("You Selected City Cruiser");
            } else if (busType == 3) {
                System.out.println("You Selected MetroBus");
            } else if (busType == 4) {
                System.out.println("You Selected SwiftTransit");
            }
            Booking busBooking = new BusBooking();
            busBooking.bookTicket();
        } else {
            System.out.println("Invalid choice!");
        }
        scanner.close();
    }
}
