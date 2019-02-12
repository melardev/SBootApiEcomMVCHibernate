package com.melardev.spring.shoppingcartweb.dtos.response.addresses;

import com.melardev.spring.shoppingcartweb.models.Address;

public class AddressExcludeUserDto {
    private final Long id;
    private final String city;
    private final String country;
    private final String zipCode;
    private final String address;

    public AddressExcludeUserDto(Long id, String country, String city, String address, String zipCode) {
        this.id = id;
        this.city = city;
        this.country = country;
        this.zipCode = zipCode;
        this.address = address;
    }

    public static AddressExcludeUserDto build(Address address) {
        return new AddressExcludeUserDto(address.getId(), address.getCity(), address.getCountry(), address.getAddress(), address.getZipCode());
    }

    public String getAddress() {
        return address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public Long getId() {
        return id;
    }
}
