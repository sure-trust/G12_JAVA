public class Vehicle {
    String registration;
    String type;
    String manufacturer;
    int capacity;
    int totalTrips;
    double totalDistance;

    public Vehicle(String registration, String type, String manufacturer, int capacity) {
        this.registration = registration;
        this.type = type;
        this.manufacturer = manufacturer;
        this.capacity = capacity;
        this.totalTrips = 0;
        this.totalDistance = 0;
    }
}
