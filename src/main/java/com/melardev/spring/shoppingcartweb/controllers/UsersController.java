package com.melardev.spring.shoppingcartweb.controllers;

import com.melardev.spring.shoppingcartweb.dtos.request.RegisterDto;
import com.melardev.spring.shoppingcartweb.dtos.response.addresses.AddressListDto;
import com.melardev.spring.shoppingcartweb.dtos.response.base.AppResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.base.ErrorResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.base.SuccessResponse;
import com.melardev.spring.shoppingcartweb.errors.exceptions.PermissionDeniedException;
import com.melardev.spring.shoppingcartweb.models.Address;
import com.melardev.spring.shoppingcartweb.models.Role;
import com.melardev.spring.shoppingcartweb.models.User;
import com.melardev.spring.shoppingcartweb.services.RolesService;
import com.melardev.spring.shoppingcartweb.services.auth.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    UsersService usersService;

    @Autowired
    RolesService rolesService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @PostMapping
    public ResponseEntity<AppResponse> registerUser(@Valid @RequestBody RegisterDto createUserDto, BindingResult result) {
        if (usersService.existsByUsername(createUserDto.getUsername())) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("username", "Username already taken");
            return new ResponseEntity<AppResponse>(new ErrorResponse(errors),
                    HttpStatus.BAD_REQUEST);
        }

        if (usersService.existsByEmail(createUserDto.getEmail())) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("email", "Email already taken");
            return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
        }

        HashSet<Role> roles = new HashSet<Role>(Collections.singletonList(rolesService.getOrCreate("ROLE_USER")));

        // Creating user's account
        User user = new User(createUserDto.getFirstName(), createUserDto.getLastName(), createUserDto.getEmail(),
                createUserDto.getUsername(), createUserDto.getPassword(), roles);

        usersService.createUser(user);

        return new ResponseEntity<>(new SuccessResponse("User registered successfully"), HttpStatus.OK);

    }

    @GetMapping("my_addresses")
    public AddressListDto myAddresses() {
        User user = usersService.getCurrentLoggedInUser();
        if (user == null)
            throw new PermissionDeniedException("You are not logged In");
        List<Address> addresses = user.getAddresses();
        return AddressListDto.build(addresses);
    }

}
