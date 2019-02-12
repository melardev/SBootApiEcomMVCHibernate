package com.melardev.spring.shoppingcartweb.controllers;


import com.melardev.spring.shoppingcartweb.dtos.request.products.CreateOrEditProductDto;
import com.melardev.spring.shoppingcartweb.dtos.response.base.AppResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.base.ErrorResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.comments.partials.CommentPartialDto;
import com.melardev.spring.shoppingcartweb.dtos.response.products.ProductListResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.products.SingleProductResponse;
import com.melardev.spring.shoppingcartweb.enums.CrudOperation;
import com.melardev.spring.shoppingcartweb.errors.exceptions.PermissionDeniedException;
import com.melardev.spring.shoppingcartweb.forms.CommentForm;
import com.melardev.spring.shoppingcartweb.models.*;
import com.melardev.spring.shoppingcartweb.services.*;
import com.melardev.spring.shoppingcartweb.services.auth.AuthorizationService;
import com.melardev.spring.shoppingcartweb.services.auth.UsersService;
import com.melardev.spring.shoppingcartweb.services.interfaces.ISettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("products")
public class ProductsController {

    private final ProductsService productsService;

    protected ISettingsService settingsService;
    private AuthorizationService authorizationService;


    @Autowired
    private UsersService usersService;
    private CommentsService commentsService;
    @Autowired
    private CategoryService categoriesService;
    @Autowired
    private TagService tagService;
    @Autowired
    private StorageService storageService;


    @Autowired
    public ProductsController(ProductsService productsService, AuthorizationService authorizationService,
                              CommentsService commentsService) {
        super();
        this.authorizationService = authorizationService;
        this.productsService = productsService;
        this.commentsService = commentsService;
    }

    @GetMapping
    public ProductListResponse index(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "page_size", defaultValue = "8") int pageSize,
                                     HttpServletRequest request) {

        Page<Product> productsPage = productsService.findAllForSummary(page, pageSize);

        return ProductListResponse.build(productsPage, request.getRequestURI());

    }

    @GetMapping("by_tag/{tag_name}")
    public ProductListResponse getProductsByTag(@PathVariable(name = "tag_name", required = false) String tagName,
                                                HttpServletRequest request,
                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "page_size", defaultValue = "5") int pageSize) {


        Page<Product> productsPage = productsService.findByTagName(tagName, page, pageSize);
        return ProductListResponse.build(productsPage, request.getRequestURI());

    }

    @GetMapping("by_category/{category_name}")
    public ProductListResponse getByCategory(@PathVariable(name = "category_name", required = false) String categoryName,
                                                HttpServletRequest request,
                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "page_size", defaultValue = "5") int pageSize) {

        Page<Product> productsPage = productsService.getByCategory(categoryName, page, pageSize);
        return ProductListResponse.build(productsPage, request.getRequestURI());

    }

    @GetMapping("{slug}")
    public SingleProductResponse show(@PathVariable("slug") String slug) {

        Product product = productsService.findById(slug);
        return SingleProductResponse.build(product);

        /*
        SingleProductResponse.AuthInfo authInfo = new SingleProductResponse.AuthInfo();

        authInfo.setCanEdit(this.authorizationService.canEditProducts());
        authInfo.setCanDelete(this.authorizationService.canEditProducts());
*/

    }

    @GetMapping("by_id/{id}")
    public SingleProductResponse show(@PathVariable("id") Long id) {
        Product product = productsService.findById(id);
        return SingleProductResponse.build(product);

/*
        authInfo.setCanEdit(this.authorizationService.canEditProducts());
        authInfo.setCanDelete(this.authorizationService.canEditProducts());
*/

    }

    // @InitBinder("productForm")
    public void initBinder(WebDataBinder binder) {
        //binder.setValidator(new ProductValidator());
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<AppResponse> create(HttpServletRequest request,
                                              @RequestParam("images[]") MultipartFile[] uploadingFiles) throws IOException {

        /* // TODO: fix this, the regex does not seem to work
        Enumeration<String> paramNames = request.getParameterNames();
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");

        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            Matcher matchPattern = pattern.matcher(paramName);
            int count = matchPattern.groupCount();

            if (count > 1) {
                if (paramName.contains("tags")) {
                    String lol = matchPattern.group(0);
                }
            }
        }
        */

        User user = usersService.getCurrentLoggedInUser();
        if (!this.authorizationCheckOnProducts(CrudOperation.CREATE, user))
            throw new PermissionDeniedException("You are not allowed to create products");
/*
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ErrorResponse(bindingResult.getModel()), HttpStatus.BAD_REQUEST);
        } else {
*/

        final Product product = new Product();
        request.getParameterMap().forEach((key, value) -> {
            if (key.equalsIgnoreCase("name"))
                product.setName(value[0]);
            if (key.equalsIgnoreCase("description"))
                product.setDescription(value[0]);
            if (key.equalsIgnoreCase("price"))
                product.setPrice(Double.parseDouble(value[0]));
            if (key.equalsIgnoreCase("stock"))
                product.setStock(Integer.parseInt(value[0]));

            // TODO: improve this, I tried to use regex but it does not work, someone knows why?
            if (key.indexOf("tags[") != -1) {
                key = key.replace("tags[", "").replace("]", "");
                product.getTags().add(new Tag(key, value[0]));
            }

            if (key.indexOf("categories[") != -1) {
                key = key.replace("categories[", "").replace("]", "");
                product.getCategories().add(new Category(key, value[0]));
            }
        });

        List<File> files = storageService.upload(uploadingFiles, "/iamges/products");

        productsService.createWithDettachedTagsAndCategories(product, files);
        return new ResponseEntity<>(SingleProductResponse.build(product), HttpStatus.CREATED);

        //}

    }

    // This method is not used, it was just a curiosity, it ended up being a nightmare, base64 images are huge,
    // indeed some frameworks refuse to handle a so big request, for uploading always use multipart request
    public ResponseEntity<AppResponse> createFromJson(
            @RequestBody CreateOrEditProductDto form, BindingResult bindingResult,
            HttpServletRequest request) throws IOException {
        User user = usersService.getCurrentLoggedInUser();
        if (!this.authorizationCheckOnProducts(CrudOperation.CREATE, user))
            throw new PermissionDeniedException("You are not allowed to create products");
/*
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ErrorResponse(bindingResult.getModel()), HttpStatus.BAD_REQUEST);
        } else {
*/
        Set<Tag> tags = new HashSet<>(form.getTags().size());
        Set<Category> categories = new HashSet<>(form.getCategories().size());
        form.getTags().forEach(t -> tags.add(new Tag(t.getName(), t.getDescription())));
        form.getCategories().forEach(t -> categories.add(new Category(t.getName(), t.getDescription())));
        List<File> images = storageService.uploadBase64Images(form.getImages());
        Product product = productsService.create(
                form.getName(),
                form.getDescription(),
                tags,
                categories,
                images
        );
        return new ResponseEntity<>(SingleProductResponse.build(product), HttpStatus.CREATED);
        //}

    }


    @PostMapping("{slug}/comments")
    public ResponseEntity<AppResponse> createComment(@PathVariable("slug") String slug,
                                                     @Valid @RequestBody CommentForm form, BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new ErrorResponse(bindingResult.getModel()), HttpStatus.BAD_REQUEST);

        if (form.getContent() == null)
            return new ResponseEntity<AppResponse>(new ErrorResponse("No sorry, "), HttpStatus.BAD_REQUEST);

        if (!this.authorizationCheck(CrudOperation.CREATE, null))
            throw new PermissionDeniedException();

        Product product = this.productsService.findById(slug);


        User user = this.usersService.getCurrentLoggedInUser();
        Comment comment = this.commentsService.save(form.getContent(), product, user);
        return new ResponseEntity<>(CommentPartialDto.build(comment), HttpStatus.CREATED);

    }

    private boolean authorizationCheck(CrudOperation operation, Long id) {
        return this.authorizationCheck(this.commentsService.findByIdNotThrow(id), operation);
    }

    private boolean authorizationCheck(Comment comment, CrudOperation operation) {
        switch (operation) {
            case CREATE:
                return this.authorizationService.canCreateComments(usersService.getCurrentLoggedInUser());
            case UPDATE:
                return this.authorizationService.canUpdateComments(comment, usersService.getCurrentLoggedInUser());
            case DELETE:
                return this.authorizationService.canDeleteComments(comment, usersService.getCurrentLoggedInUser());
            default:
                return false;
        }
    }

    private boolean authorizationCheckOnProducts(CrudOperation operation, User user) {
        if (user == null)
            user = usersService.getCurrentLoggedInUser();

        switch (operation) {
            case CREATE:
                return this.authorizationService.canCreateProducts(user);
            case UPDATE:
                return this.authorizationService.canUpdateProducts(user);
            case DELETE:
                return this.authorizationService.canDeleteProducts(user);
            default:
                return false;
        }
    }
}