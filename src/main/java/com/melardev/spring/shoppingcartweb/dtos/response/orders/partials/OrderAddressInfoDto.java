package com.melardev.spring.shoppingcartweb.dtos.response.orders.partials;

import com.melardev.spring.shoppingcartweb.models.Address;

public class OrderAddressInfoDto {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String city;
    private final String country;
    private final String zipCode;

    public OrderAddressInfoDto(String firstName, String lastName, String address, String city, String country, String zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.country = country;
        this.zipCode = zipCode;
    }

    public static OrderAddressInfoDto build(Address address) {
        return new OrderAddressInfoDto(
                address.getFirstName(),
                address.getLastName(),
                address.getAddress(),
                address.getCity(),
                address.getCountry(),
                address.getZipCode()
        );
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getZipCode() {
        return zipCode;
    }


}
