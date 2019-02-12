package com.melardev.spring.shoppingcartweb.dtos.request.orders;

import com.melardev.spring.shoppingcartweb.dtos.request.cart.CartItemDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

public class CheckoutDto {

    private String firstName;
    private String lastName;

    @NotEmpty
    @NotNull
    private String cardNumber;
    @NotEmpty
    @NotNull
    public String address;
    @NotNull
    public Long addressId;

    @NotEmpty
    @NotNull
    private String city;

    @NotEmpty
    @NotNull
    public String country;

    public String phoneNumber;

    @NotEmpty
    @NotNull
    public String zipCode;

    @NotEmpty
    @NotNull
    Collection<CartItemDto> cartItems;
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }



    public Collection<CartItemDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Collection<CartItemDto> cartItems) {
        this.cartItems = cartItems;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public CheckoutDto() {
    }
}
