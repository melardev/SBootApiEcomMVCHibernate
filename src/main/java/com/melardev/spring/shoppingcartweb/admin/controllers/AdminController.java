package com.melardev.spring.shoppingcartweb.admin.controllers;

import com.melardev.spring.shoppingcartweb.dtos.response.admin.AdminDashboardResponse;
import com.melardev.spring.shoppingcartweb.models.Comment;
import com.melardev.spring.shoppingcartweb.models.Order;
import com.melardev.spring.shoppingcartweb.models.Product;
import com.melardev.spring.shoppingcartweb.models.User;
import com.melardev.spring.shoppingcartweb.services.CommentsService;
import com.melardev.spring.shoppingcartweb.services.OrdersService;
import com.melardev.spring.shoppingcartweb.services.ProductsService;
import com.melardev.spring.shoppingcartweb.services.SettingsService;
import com.melardev.spring.shoppingcartweb.services.auth.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("admin")
// @PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final OrdersService ordersService;
    private final SettingsService settingsService;
    private final ProductsService productsService;
    private final CommentsService commentsService;
    private final UsersService usersService;


    @Autowired
    public AdminController(ProductsService productsService, SettingsService settingsService,
                           CommentsService commentsService, UsersService usersService, OrdersService ordersService) {
        this.productsService = productsService;
        this.commentsService = commentsService;
        this.settingsService = settingsService;
        this.usersService = usersService;
        this.ordersService = ordersService;
    }

    @GetMapping
    public AdminDashboardResponse index(Model model, HttpServletRequest request,
                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "page_size", defaultValue = "5") int pageSize) {

        page = Math.max(1, page); // Ensure min 1
        pageSize = Math.max(1, pageSize); // Ensure pageSize min 1
        pageSize = Math.min(10, pageSize); // Ensure pageSize max 10
        Page<Product> products = productsService.findAllForSummary(page, pageSize);

        // Min 1 and Max 50 items per page
        Page<User> users = usersService.getLatest(page, Math.min(50, Math.max(1, pageSize)));

        Page<Comment> comments = commentsService.findLatest(page, pageSize);

        Page<Order> orders = ordersService.findLatest(page, pageSize);

        return AdminDashboardResponse.build(products, comments, orders, users,
                "/admin/products",
                "admin/comments",
                "admin/orders",
                "admin/users");
    }

}