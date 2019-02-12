package com.melardev.spring.shoppingcartweb.services;

import com.melardev.spring.shoppingcartweb.admin.dtos.AdminOrderUpdateDto;
import com.melardev.spring.shoppingcartweb.dtos.request.cart.CartItemDto;
import com.melardev.spring.shoppingcartweb.dtos.request.orders.CheckoutDto;
import com.melardev.spring.shoppingcartweb.errors.exceptions.PermissionDeniedException;
import com.melardev.spring.shoppingcartweb.errors.exceptions.ResourceNotFoundException;
import com.melardev.spring.shoppingcartweb.errors.exceptions.UnexpectedStateException;
import com.melardev.spring.shoppingcartweb.models.*;
import com.melardev.spring.shoppingcartweb.repository.OrdersRepository;
import com.melardev.spring.shoppingcartweb.services.auth.UsersService;
import com.melardev.spring.shoppingcartweb.services.interfaces.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.toList;

@Service
public class OrdersService implements IOrderService {
    private final OrdersRepository ordersRepository;

    @Autowired
    private OrderItemRepository orderItemsRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private AddressService addressesService;
    //private final UsersService usersService;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
        //this.usersService = usersService;
    }

    @Override
    public Page<Order> findLatest(int page, int count) {
        PageRequest pageRequest = PageRequest.of(page - 1, count, Sort.Direction.DESC, "createdAt");
        Page<Order> result = this.ordersRepository.findAll(pageRequest);
        return result;
    }

    @Override
    public Page<Order> findOrderSummariesBelongingToUser(User user, int page, int count) {
        PageRequest pageRequest = PageRequest.of(page - 1, count, Sort.Direction.DESC, "createdAt");
        /*User user = this.usersService.getCurrentLoggedInUser();
        if (user == null)
            throw new UnexpectedStateException();
*/
        Page<Order> pagedOrders = this.ordersRepository.findOrdersByUserId(user.getId(), pageRequest);
        List<Order> orders = pagedOrders.getContent();
        List<Long> orderIds = orders.stream().map(TimestampedEntity::getId).collect(toList());

        List<Object[]> orderItems = orderItemsRepository.getOrderItemsCountForOrderIds(orderIds);
        orderItems.forEach(orderItem -> {
            Long orderId = (Long) orderItem[0];
            Long orderItemsCount = (Long) orderItem[1];
            Optional<Order> order = orders.stream().filter(p -> p.getId().equals(orderId)).findFirst();
            if (order.isPresent())
                order.get().setOrderItemsCount(orderItemsCount);
            else
                throw new UnexpectedStateException();

        });

        return pagedOrders;
    }

    @Override
    public Page<Order> findOrderDetailsBelongingToUser(User user, int page, int count) {
        PageRequest pageRequest = PageRequest.of(page - 1, count, Sort.Direction.DESC, "createdAt");
        /*User user = this.usersService.getCurrentLoggedInUser();
        if (user == null)
            throw new UnexpectedStateException();
*/
        return this.ordersRepository.findOrdersByUserId(user.getId(), pageRequest);
    }

    @Override
    public Order findById(Long id) {
        return findByIdOrThrow(id);
    }

    @Override
    public Order findById(Long id, boolean throwExceptionIfNotFound) {
        Optional<Order> order = this.ordersRepository.findById(id);
        if (throwExceptionIfNotFound && !order.isPresent())
            throw new ResourceNotFoundException();
        return order.orElse(null);
    }

    @Override
    public Order findByIdOrNull(Long id) {
        return findById(id, false);
    }

    @Override
    public Order findByIdOrThrow(Long id) {
        return findById(id, true);
    }

    @Override
    public List<Order> findAll() {
        return ordersRepository.findAll();
    }

    public long getAllCount() {
        return ordersRepository.count();
    }

    @Override
    public Order save(Order order) {
        return ordersRepository.save(order);
    }

    @Override
    public List<Order> saveAll(List<Order> orders) {
        return ordersRepository.saveAll(orders);
    }

    public Order save(CheckoutDto form, User user) {
        final AtomicReference<Order> orderAtomicReference = new AtomicReference<>();

        Order order = new Order();
        Address address;
        if (user != null && form.getAddressId() != null) {
            // reusing address
            address = addressesService.getById(form.getAddressId());
            if (!address.getUser().getId().equals(user.getId()))
                throw new PermissionDeniedException("You can not use other's address for your order");

        } else if (form.getAddressId() == null) {
            // new address
            address = new Address();
            address.setAddress(form.getAddress());
            address.setFirstName(form.getFirstName());
            address.setLastName(form.getLastName());
            address.setZipCode(form.getZipCode());
            address.setCity(form.getCity());
            address.setCountry(form.getCountry());
            if (user != null)
                address.setUser(user);
            //order.setPh(form.getPhoneNumber());
            //order.setCard(form.getCardNumber());
        } else {
            throw new UnsupportedOperationException("What are you trying to do slat??");
        }

        order.setAddress(address);
        if (user != null)
            order.setUser(user);

        List<Long> productIds = form.getCartItems().stream().map(cartItem -> cartItem.getId()).collect(toList());
        List<Product> products = this.productsService.findBasicInfoWhereProductIds(productIds);
        if (products == null || products.size() != form.getCartItems().size())
            throw new PermissionDeniedException("make sure to check all products are still available");
        ArrayList<CartItemDto> cartItems = new ArrayList<>(form.getCartItems());
        AtomicInteger count = new AtomicInteger();
        List<OrderItem> orderItems = products.stream().map(p -> new OrderItem(orderAtomicReference.get(), p, cartItems.get(count.getAndIncrement()).getQuantity(), p.getPrice())).collect(toList());

/*        // TODO: I only need the name,slug, price, create a model extension
        List<OrderItem> orderItems = form.getCartItems().stream().map(cartItem -> {
            Product product = OrdersService.this.productsService.findByIdWithElementalInfo(cartItem.getId());
            return new OrderItem(orderAtomicReference.findById(), product, cartItem.getQuantity(), product.getPrice());
        }).collect(toList());
        */
        order.setOrderItems(orderItems);
        return ordersRepository.save(order);
    }

    @Override
    public Order getRandom() {
        return null;
    }

    @Override
    public List<Order> saveAll(Set<Order> products) {
        return null;
    }

    @Override
    public Order update(Order object) {
        return null;
    }

    @Override
    public void delete(Order order) {

    }

    @Override
    public void delete(Long id) {

    }

    public Order update(Long id, AdminOrderUpdateDto form) {
        //Order order = findById(id);
        Order order = ordersRepository.getOne(id);
        order.setTrackingNumber(form.getTrackingNumber());
        order.setOrderStatus(form.getOrderStatus());
        return ordersRepository.save(order);
    }
}
