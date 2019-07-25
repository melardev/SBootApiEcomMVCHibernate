package com.melardev.spring.shoppingcartweb.seeds;


import com.github.javafaker.Faker;
import com.melardev.spring.shoppingcartweb.enums.OrderStatus;
import com.melardev.spring.shoppingcartweb.models.*;
import com.melardev.spring.shoppingcartweb.services.*;
import com.melardev.spring.shoppingcartweb.services.auth.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Component
@Profile("seeds")
@Transactional
public class DbSeeder implements CommandLineRunner {

    private final UsersService userService;
    private final SettingsService settingsService;
    private final OrdersService ordersService;
    private final RolesService rolesService;
    private final Faker faker;

    @Autowired
    FileUploadService fileUploadService;

    private final ProductsService productsService;
    @Autowired
    private AddressService addressesService;
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CategoryService categoriesService;


    @Autowired
    public DbSeeder(UsersService userService, RolesService rolesService, OrdersService ordersService, SettingsService settingsService,
                    ProductsService productsService) {
        this.userService = userService;
        this.ordersService = ordersService;
        this.settingsService = settingsService;
        this.productsService = productsService;
        faker = new Faker(Locale.getDefault());
        this.rolesService = rolesService;
    }

    public void createAdminFeature() {
        createAdminRole();
        createAdminUser();
    }

    private void createAdminRole() {
        if (this.rolesService.getRoleDontThrow(settingsService.getAdminRoleName()) == null)
            this.rolesService.save(new Role(this.settingsService.getAdminRoleName()));
    }

    private void createAdminUser() {
        //User user = userService.findByEmailNoException(settingsService.getDefaultAdminEmail());
        User user = userService.findByUsernameNoException(settingsService.getDefaultAdminUsername());
        if (user == null) {
            HashSet<Role> roles = new HashSet<>();
            roles.add(this.rolesService.getRoleOrThrow(this.settingsService.getAdminRoleName()));

            this.userService.createUser(new User(settingsService.getDefaultAdminFirstName(),
                    settingsService.getDefaultAdminLastName(),
                    settingsService.getDefaultAdminEmail(),
                    settingsService.getDefaultAdminUsername(),
                    settingsService.getDefaultAdminPassword(),
                    roles));
        }
    }

    public void seedUsers() {
        if (this.rolesService.getRoleDontThrow(this.settingsService.getAuthenticatedRoleName()) == null)
            this.rolesService.save(new Role(this.settingsService.getAuthenticatedRoleName()));


        int maxUsersToSeed = this.settingsService.getMaxUsersToSeed();
        long currentUsersCount = this.userService.getAllCount();
        HashSet<Role> roles = new HashSet<>();
        roles.add(this.rolesService.getRoleOrThrow(this.settingsService.getAuthenticatedRoleName()));

        List<User> users = IntStream.range((int) currentUsersCount, maxUsersToSeed)
                .mapToObj(i -> new User(faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress(),
                        faker.name().name(), "password", roles)).collect(Collectors.toList());

        this.userService.saveAll(users);
    }


    public void seedTags() {
        int maxTagsToSeed = this.settingsService.getMaxTagsToSeed();
        int currentTags = (int) this.tagService.getAllCount();
        List<Tag> tags = IntStream.range(currentTags, maxTagsToSeed)
                .mapToObj(i -> {
                    Tag tag = new Tag();
                    tag.setName(faker.lorem().word());
                    tag.setDescription(faker.lorem().sentence());
                    return tag;
                }).collect(Collectors.toList());
        tags = tags.stream().filter(distinctByKey(Tag::getName)).collect(Collectors.toList());
        tagService.saveAll(tags);
    }

    public void seedCategories() {
        long maxCategoriesToSeed = this.settingsService.getMaxCategoriesToSeed();
        long currentCategoriesCount = this.categoriesService.getAllCount();
        List<Category> categories = LongStream.range(currentCategoriesCount, maxCategoriesToSeed)
                .mapToObj(i -> {
                    Category category = new Category();
                    category.setName(faker.lorem().word());
                    category.setDescription(faker.lorem().sentence());
                    return category;
                }).collect(Collectors.toList());
        categories = categories.stream().filter(distinctByKey(Category::getName)).collect(Collectors.toList());
        categoriesService.saveAll(categories);
    }

    public void seedProducts() {
        int maxProductsToSeed = this.settingsService.getMaxProductsToSeed();
        int currentProducts = (int) this.productsService.getAllCount();
        if (maxProductsToSeed - currentProducts > 0) {
            List<Tag> tags = tagService.getAllTags();
            List<Category> categories = categoriesService.getAllTags();
            Set<Product> products = IntStream.range(currentProducts, maxProductsToSeed)
                    .mapToObj(i -> {
                        Product product = new Product();
                        product.setName(faker.commerce().productName() + faker.random().nextInt(0, 10));
                        product.setSlug(StringHelper.slugify(product.getName()));
                        product.setDescription(faker.lorem().paragraph(6));
                        product.setPrice(faker.random().nextDouble() * 100);
                        product.setStock(faker.random().nextInt(0, 1200));
                        product.setTags(new HashSet<>(Collections.singletonList(
                                tags.get(faker.random().nextInt(1, tags.size() - 1))
                        )));

                        product.setCategories(new HashSet<Category>(Arrays
                                .asList(
                                        categories.get(faker.random().nextInt(1, categories.size() - 1))
                                )));

                        AtomicBoolean first = new AtomicBoolean(true);
                        IntStream.range(0, faker.random().nextInt(1, 2)).forEach(index -> {
                            FileUpload fu = generateFileUpload(ProductImage.class);
                            if (first.get())
                                fu.setFeaturedImage(true);
                            else
                                first.set(false);
                            fu.setProduct(product);
                            fileUploadService.save(fu);
                        });

                        return product;
                    })
                    .collect(toSet());

            this.productsService.saveAll(products);
        }
    }

    private void seedOrders() {
        int maxOrdersToSeed = this.settingsService.getOrdersToSeedCount();
        int currentOrders = (int) this.ordersService.getAllCount();
        Random random = new Random();
        if (maxOrdersToSeed - currentOrders > 0) {
            List<Product> products = this.productsService.findAll();
            List<Order> orders = IntStream.range(currentOrders, maxOrdersToSeed)
                    .mapToObj(i -> {
                        User user = userService.getRandom();
                        Order order = new Order();
                        order.setUser(user);
                        order.setOrderStatus(getRandom(OrderStatus.values()));
                        List<Address> addresses = user.getAddresses();
                        order.setTrackingNumber(faker.bothify("??-####-####", true));

                        if (addresses != null && addresses.size() > 0)
                            order.setAddress(addresses.get(random.nextInt(addresses.size())));
                        else {
                            Address address = new Address();
                            address.setCountry(faker.address().country());
                            address.setFirstName(user.getFirstName());
                            address.setLastName(user.getLastName());
                            address.setAddress(faker.address().streetAddress());
                            address.setCountry(faker.address().country());
                            address.setCity(faker.address().city());
                            address.setZipCode(faker.address().zipCode());
                            address.setUser(user);
                            // addressesService.save(address);

                            order.setAddress(address);
                        }

                        List<OrderItem> orderItems;
                        List<Long> ids = new ArrayList<>();
                        orderItems = IntStream.range(0, faker.random().nextInt(1, 10))
                                .mapToObj(j -> {
                                    Product product = getRandomProductNotIn(products, ids, random);
                                    ids.add(product.getId());
                                    return new OrderItem(order, product, faker.random().nextInt(1, 7),
                                            Math.min(10, faker.random().nextInt(-50, 50) + product.getPrice()));

                                }).collect(toList());

                        order.setOrderItems(orderItems);

                        return order;
                    })
                    .collect(toList());
            System.out.println("Saved " + this.ordersService.saveAll(orders).size() + " orders");
        }
    }

    private Product getRandomProductNotIn(List<Product> products, List<Long> ids, Random random) {
        List<Product> filteredProducts = products.stream().filter(p -> !ids.contains(p.getId())).collect(toList());
        return filteredProducts.get(random.nextInt(filteredProducts.size()));
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Override
    public void run(String... args) {
        createAdminFeature();
        seedUsers();
        seedTags();
        seedCategories();
        seedProducts();
        seedOrders();
        seedComments();
        seedFileUploads();
        System.out.println("Seeding finished");
    }

    private void seedFileUploads() {
        faker.internet().image();
        List<Tag> tags = tagService.getAllTags();
        List<Category> categories = categoriesService.getAllTags();

        tags.forEach(tag -> {
            if (tag.getImages() == null || tag.getImages().size() == 0) {

                IntStream.range(0, faker.random().nextInt(1, 3)).forEach(i -> {
                    FileUpload fu = generateFileUpload(TagImage.class);
                    fu.setTag(tag);
                    fileUploadService.save(fu);
                });
            }
        });

        categories.forEach(category -> {
            if (category.getImages() == null || category.getImages().size() == 0) {

                IntStream.range(0, faker.random().nextInt(1, 3)).forEach(i -> {
                    FileUpload fu = generateFileUpload(CategoryImage.class);
                    fu.setCategory(category);
                    fileUploadService.save(fu);
                });
            }
        });
    }

    private void seedComments() {
        int maxCommentsToSeed = this.settingsService.getMaxCommentsToSeed();
        int currentCommentsCount = (int) this.commentsService.getCount();
        Set<Comment> products = IntStream.range(currentCommentsCount, maxCommentsToSeed)
                .mapToObj(i -> {
                    Comment comment = new Comment();
                    comment.setContent(faker.lorem().sentence());
                    comment.setProduct(productsService.getRandom());
                    comment.setRating(faker.random().nextInt(1, 5));
                    comment.setUser(userService.getRandom());

                    return comment;
                })
                .collect(toSet());

        this.commentsService.saveAll(products);
    }

    private <T extends FileUpload> T generateFileUpload(Class<T> c) {
        T fu = null;
        try {
            fu = c.getDeclaredConstructor().newInstance();
            fu.setFileSize(faker.random().nextLong());
            fu.setFileName(faker.bothify("###-###_????.png"));
            fu.setFilePath(faker.internet().image());
            fu.setOriginalFileName(faker.commerce().productName());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return fu;
    }

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void test(String hql) {

    }

    public static OrderStatus getRandom(OrderStatus[] orderStatuses) {
        int random = new Random().nextInt(orderStatuses.length);
        return orderStatuses[random];
    }
}