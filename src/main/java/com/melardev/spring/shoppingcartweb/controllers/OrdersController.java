package com.melardev.spring.shoppingcartweb.controllers;

import com.melardev.spring.shoppingcartweb.dtos.request.orders.CheckoutDto;
import com.melardev.spring.shoppingcartweb.dtos.response.base.AppResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.base.ErrorResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.orders.OrderListResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.orders.OrderSingleResponse;
import com.melardev.spring.shoppingcartweb.errors.exceptions.PermissionDeniedException;
import com.melardev.spring.shoppingcartweb.models.Order;
import com.melardev.spring.shoppingcartweb.models.User;
import com.melardev.spring.shoppingcartweb.services.OrdersService;
import com.melardev.spring.shoppingcartweb.services.SettingsService;
import com.melardev.spring.shoppingcartweb.services.auth.AuthorizationService;
import com.melardev.spring.shoppingcartweb.services.auth.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final SettingsService settingsService;
    private final OrdersService ordersService;
    private final UsersService userService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    public OrdersController(SettingsService settingsService, OrdersService ordersService,
                            UsersService userService) {
        this.ordersService = ordersService;
        this.settingsService = settingsService;
        this.userService = userService;
    }

    @GetMapping
    public AppResponse index(HttpServletRequest request,
                             @RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "page_size", defaultValue = "30") int pageSize) {

        if (this.userService.isAnonymous())
            return new ErrorResponse("We can not retrieve orders for anonymous users");

        Page<Order> ordersPage = this.ordersService.findOrderSummariesBelongingToUser(this.userService.getCurrentLoggedInUser(), page, pageSize);
        return OrderListResponse.build(ordersPage, request.getRequestURI());
    }

    @GetMapping("{id}")
    public OrderSingleResponse show(@PathVariable("id") Long id, Principal principal) {
        User user = userService.getCurrentLoggedInUser();

        if (user == null)
            throw new PermissionDeniedException("Not logged in");

        Order order = ordersService.findById(id);
        if (!order.getUser().getId().equals(user.getId()) || !userService.isUserAdmin((User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()))
            throw new PermissionDeniedException("You are not authorized");
        return OrderSingleResponse.build(order);
    }

    @PostMapping
    public AppResponse checkout(@RequestBody CheckoutDto form) {
        Order order = this.ordersService.save(form, userService.getCurrentLoggedInUser());
        return OrderSingleResponse.build(order);
    }
}
