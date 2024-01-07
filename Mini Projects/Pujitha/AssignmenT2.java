package SourceCode;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AssignmenT2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		class Vehicle {
		    private String registrationNumber;
		    private double totalDistanceTravelled;
		    private int capacity;

		    public Vehicle(String registrationNumber, int capacity)
		    {
		        this.registrationNumber = registrationNumber;
		        this.totalDistanceTravelled = 0.0;
		        this.capacity = capacity;
		    }

		    public String getRegistrationNumber() {
		        return registrationNumber;
		    }

		    public double getTotalDistanceTravelled() {
		        return totalDistanceTravelled;
		    }

		    public int getCapacity() {
		        return capacity;
		    }

		    public void addDistance(double distance) {
		        totalDistanceTravelled += distance;
		    }
		}

		class Trip {
		    private Vehicle vehicle;
		    private double distance;

		    public Trip(Vehicle vehicle, double distance) {
		        this.vehicle = vehicle;
		        this.distance = distance;
		        vehicle.addDistance(distance);
		    }

		    public Vehicle getVehicle() {
		        return vehicle;
		    }

		    public double getDistance() {
		        return distance;
		    }
		}

		class VehicleBookingSystem {
		    private List<Vehicle> vehicles;
		    private List<Trip> trips;
		    private ExecutorService executorService;

		    public VehicleBookingSystem(int threadPoolSize) {
		        vehicles = new ArrayList<>();
		        trips = new ArrayList<>();
		        executorService = Executors.newFixedThreadPool(threadPoolSize);
		    }

		    public void registerVehicle(String registrationNumber, int capacity) {
		        vehicles.add(new Vehicle(registrationNumber, capacity));
		        System.out.println("Vehicle registered: " + registrationNumber);
		    }

		    public void requestAndAllocateVehicle() {
		        executorService.execute(() -> {
		        
		            Vehicle vehicle = vehicles.get(new java.util.Random().nextInt(vehicles.size()));
		            double distance = new java.util.Random().nextDouble() * 100;
		            Trip trip = new Trip(vehicle, distance);
		            trips.add(trip);
		            System.out.println("Trip completed: " + trip.getVehicle().getRegistrationNumber() +
		                    ", Distance: " + trip.getDistance());
		        });
		    }

		    public void generateTripsReport() {
		        System.out.println("Trips Report");
		        System.out.println("Vehicle_registration | Vehicle_Type | Total_Trips_completed | Total_Distance");
		        System.out.println("--------------------------------------------------------------------------------");

		        for (Vehicle vehicle : vehicles) {
		            long totalTrips = trips.stream().filter(t -> t.getVehicle().equals(vehicle)).count();
		            double totalDistance = trips.stream()
		                    .filter(t -> t.getVehicle().equals(vehicle))
		                    .mapToDouble(Trip::getDistance)
		                    .sum();

		            System.out.printf("%-20s | %-13s | %-22d | %.2f km\n",
		                    vehicle.getRegistrationNumber(),
		                    (vehicle.getCapacity() > 2 ? "Car" : "Two-wheeler"),
		                    totalTrips,
		                    totalDistance);
		        }
		    }

		    public void shutdown() {
		        executorService.shutdown();
		    }

		    public void awaitTermination() throws InterruptedException {
		        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		    }
		}

		class Main {
		    public static void main(String[] args) throws InterruptedException {
		        VehicleBookingSystem bookingSystem = new VehicleBookingSystem(10);
		        Scanner scanner = new Scanner(System.in);

		        int choice;
		        do {
		            System.out.println("Enter 1 for Vehicle Registration");
		            System.out.println("Enter 2 for Vehicle Allocation Request");
		            System.out.println("Enter 3 for Multiple Allocation Requests in Parallel");
		            System.out.println("Enter 4 for Trips Report");
		            System.out.println("Enter 0 to exit");

		            choice = scanner.nextInt();
		            switch (choice) {
		                case 1:
		                    System.out.print("Enter registration number: ");
		                    String regNumber = scanner.next();
		                    System.out.print("Enter vehicle capacity: ");
		                    int capacity = scanner.nextInt();
		                    bookingSystem.registerVehicle(regNumber, capacity);
		                    break;

		                case 2:
		                    bookingSystem.requestAndAllocateVehicle();
		                    break;

		                case 3:
		                    System.out.print("Enter number of requests: ");
		                    int numRequests = scanner.nextInt();
		                    for (int i = 0; i < numRequests; i++) {
		                        bookingSystem.requestAndAllocateVehicle();
		                    }
		                    break;

		                case 4:
		                    bookingSystem.generateTripsReport();
		                    break;
		            }

		        } while (choice != 0);
		        
		        bookingSystem.shutdown();
		        bookingSystem.awaitTermination();
		        scanner.close();
		    }
		}

	}

}
