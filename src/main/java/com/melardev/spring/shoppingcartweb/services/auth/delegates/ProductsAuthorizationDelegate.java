package com.melardev.spring.shoppingcartweb.services.auth.delegates;


import com.melardev.spring.shoppingcartweb.enums.AuthorizationPolicy;
import com.melardev.spring.shoppingcartweb.models.Product;
import com.melardev.spring.shoppingcartweb.models.User;
import com.melardev.spring.shoppingcartweb.services.auth.AuthorizationService;

public class ProductsAuthorizationDelegate {
    private final AuthorizationService authorizationService;

    public ProductsAuthorizationDelegate(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public boolean canCreateProducts(User user) {
        AuthorizationPolicy whoCan = authorizationService.getSettingsService().getWhoCanCreateProducts();
        return can(whoCan, null, user);
    }

    public boolean canUpdateProducts(Product product, User user) {
        AuthorizationPolicy crudPolicy = this.authorizationService.getSettingsService().getWhoCanUpdateProducts();
        return can(crudPolicy, product, user);
    }

    private boolean can(AuthorizationPolicy crudPolicy, Product product, User user) {
        switch (crudPolicy) {
            case ONLY_ADMIN:
                return authorizationService.isCurrentUserAdmin();
            case ANY:
                return true;
            default:
                return false;
        }
    }

    public boolean canDeleteProducts(Product product, User user) {
        AuthorizationPolicy whoCan = authorizationService.getSettingsService().getWhoCanDeleteProducts();
        return can(whoCan, product, user);
    }
}

