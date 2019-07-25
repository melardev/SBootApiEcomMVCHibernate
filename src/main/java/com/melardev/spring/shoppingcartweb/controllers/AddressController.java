package com.melardev.spring.shoppingcartweb.controllers;

import com.melardev.spring.shoppingcartweb.dtos.response.addresses.AddressListDto;
import com.melardev.spring.shoppingcartweb.errors.exceptions.PermissionDeniedException;
import com.melardev.spring.shoppingcartweb.models.Address;
import com.melardev.spring.shoppingcartweb.models.User;
import com.melardev.spring.shoppingcartweb.services.auth.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AddressController {

    @Autowired
    UsersService usersService;

    @GetMapping({"my_addresses", "addresses"})
    public AddressListDto myAddresses() {
        User user = usersService.getCurrentLoggedInUser();
        if (user == null)
            throw new PermissionDeniedException("You are not logged In");
        List<Address> addresses = user.getAddresses();
        return AddressListDto.build(addresses);
    }
}
