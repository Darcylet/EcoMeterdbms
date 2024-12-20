package models;

public class Appliance {
    private final String name;
    private final double powerConsumptionPerHour; // kWh per hour
    private final int hoursUsedPerMonth;

    public Appliance(String name, double powerConsumptionPerHour, int hoursUsedPerMonth) {
        this.name = name;
        this.powerConsumptionPerHour = powerConsumptionPerHour;
        this.hoursUsedPerMonth = hoursUsedPerMonth;
    }

    // Getters and Setters
    public String getName() { return name; }
    public double getPowerConsumptionPerHour() { return powerConsumptionPerHour; }
    public int getHoursUsedPerMonth() { return hoursUsedPerMonth; }

    public double getMonthlyConsumption() {
        return powerConsumptionPerHour * hoursUsedPerMonth;
    }
}
