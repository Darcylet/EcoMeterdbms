package models;

import java.util.ArrayList;
import java.util.List;

public class Household {
    private List<Appliance> appliances;
    private double pricePerKwh;

    public Household() {
        appliances = new ArrayList<>();
        // Default appliances for a typical Filipino household
        appliances.add(new Appliance("Air Conditioner", 1.2, 180));
        appliances.add(new Appliance("Electric Fan", 0.075, 300));
        appliances.add(new Appliance("Television", 0.1, 200));
        appliances.add(new Appliance("Lights", 0.06, 300));
    }

    public void addAppliance(Appliance appliance) {
        appliances.add(appliance);
    }

    public double calculateMonthlyConsumption() {
        double totalConsumption = 0.0;
        for (Appliance appliance : appliances) {
            totalConsumption += appliance.getMonthlyConsumption();
        }
        return totalConsumption;
    }

    public double calculateMonthlyBill() {
        return calculateMonthlyConsumption() * pricePerKwh;
    }

    // Getters and Setters
    public double getPricePerKwh() { return pricePerKwh; }
    public void setPricePerKwh(double pricePerKwh) { this.pricePerKwh = pricePerKwh; }

    public List<Appliance> getAppliances() { return appliances; }
}
