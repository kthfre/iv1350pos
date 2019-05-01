package se.kth.iv1350.registersystem.model;

/**
 * Address of a physical property.
 *
 */

public class Address {
    private final String streetName;
    private final String streetNumber;
    private final String city;

    /**
     * Creates a new Address instance with passed arguments. Each object is immutable.
     *
     * @param streetName The name of the street of the Address instance to be created.
     * @param streetNumber The number of the street of the Address instance to be created.
     * @param city The name of the city of the Address instance to be created.
     */

    public Address(String streetName, String streetNumber, String city) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.city = city;
    }

    /**
     * Gets the street name of this Address.
     *
     * @return Street name of referenced Address instance.
     */

    public String getStreetName() {
        return streetName;
    }

    /**
     * Gets the street number of this Address.
     *
     * @return Street number of referenced Address instance.
     */

    public String getStreetNumber() {
        return streetNumber;
    }

    /**
     * Gets the city name of this Address.
     *
     * @return City name of referenced Address instance.
     */

    public String getCity() {
        return city;
    }
}
