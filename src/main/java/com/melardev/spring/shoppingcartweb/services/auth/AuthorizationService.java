package com.melardev.spring.shoppingcartweb.services.auth;

import com.melardev.spring.shoppingcartweb.enums.AuthorizationPolicy;
import com.melardev.spring.shoppingcartweb.models.Category;
import com.melardev.spring.shoppingcartweb.models.Comment;
import com.melardev.spring.shoppingcartweb.models.Tag;
import com.melardev.spring.shoppingcartweb.models.User;
import com.melardev.spring.shoppingcartweb.services.SettingsService;
import com.melardev.spring.shoppingcartweb.services.auth.delegates.CategoriesAuthorizationDelegate;
import com.melardev.spring.shoppingcartweb.services.auth.delegates.CommentsAuthorizationDelegate;
import com.melardev.spring.shoppingcartweb.services.auth.delegates.ProductsAuthorizationDelegate;
import com.melardev.spring.shoppingcartweb.services.auth.delegates.TagsAuthorizationDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AuthorizationService {

    SettingsService settingsService;
    private CommentsAuthorizationDelegate commentsAuthorizationDelegate;
    private ProductsAuthorizationDelegate productsAuthorizationDelegate;
    @Autowired
    private UsersService usersService;

    private TagsAuthorizationDelegate tagsAuthorizationDelegate;
    private CategoriesAuthorizationDelegate categoriesAuthorizationDelegate;

    public AuthorizationService(SettingsService settingsService, UsersService userService) {
        this.settingsService = settingsService;
    }

    @PostConstruct
    public void init() {
        this.commentsAuthorizationDelegate = new CommentsAuthorizationDelegate(this);
        this.productsAuthorizationDelegate = new ProductsAuthorizationDelegate(this);
        this.tagsAuthorizationDelegate = new TagsAuthorizationDelegate(this);
        this.categoriesAuthorizationDelegate = new CategoriesAuthorizationDelegate(this);
    }

    @Autowired
    public AuthorizationService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public boolean canEditProducts() {
        if (this.settingsService.getWhoCanEditProducts() == AuthorizationPolicy.ONLY_ADMIN)
            return isCurrentUserAdmin();
        else
            return false; // TODO: work more on roles, for now only admin
    }

    public boolean isCurrentUserAdmin() {
        return isCurrentUserInRole(this.settingsService.getAdminRoleName());
    }

    private boolean isCurrentUserAnonymous() {
        return this.usersService.isAnonymous(SecurityContextHolder.getContext().getAuthentication());
    }

    public boolean isUserAuthenticated() {
        return this.usersService.isLoggedIn();
    }


    public boolean canDeleteProducts(User user) {
        if (this.settingsService.getWhoCanDeleteProducts() == AuthorizationPolicy.ONLY_ADMIN)
            return isCurrentUserAdmin();

        return false;
    }

    public boolean canCreateProducts(User user) {
        return productsAuthorizationDelegate.canCreateProducts(user);
    }

    public boolean canCreateComments(User user) {
        return commentsAuthorizationDelegate.canCreateComments(user);
    }

    public boolean canUpdateComments(Comment comment, User user) {
        return commentsAuthorizationDelegate.canUpdateComments(comment, user);
    }


    public boolean canDeleteComments(Comment comment) {
        return canDeleteComments(comment, this.usersService.getCurrentLoggedInUser());
    }

    public boolean canDeleteComments(Comment comment, User user) {
        return commentsAuthorizationDelegate.canDeleteComments(comment, user);
    }

    public SettingsService getSettingsService() {
        return settingsService;
    }

    private boolean canOperateOnComments(Comment comment, AuthorizationPolicy policy) {
        if (policy == AuthorizationPolicy.ONLY_ADMIN)
            return isCurrentUserAdmin();
        else if (policy == AuthorizationPolicy.ADMIN_AND_OWNER) {
            if (isCurrentUserAdmin() || (isUserAuthenticated() // Authenticated
                    && // And
                    ((comment == null) || // Comment null OR
                            // It is null when the operation requested is to create a comment, hence no owner his availble
                            // for that comment, so authenticated chck is enough
                            (comment.getUser().getId().intValue() // Comment not null and owned by current user
                                    == this.usersService.getCurrentLoggedInUser().getId().intValue())))) {
                return true;
            }
        }

        return false;
    }

    public boolean canCheckout() {
        switch (settingsService.getWhocanCheckout()) {
            case AUTHENTICATED_USER:
                return usersService.isLoggedIn();
            case ANY:
                return true;
            default:
                return false;
        }
    }

    public User getCurrentLoggedInUser() {
        return usersService.getCurrentLoggedInUser();
    }


    public boolean isLoggedIn() {
        return usersService.isLoggedIn();
    }

    private boolean isCurrentUserInRole(String authority) {
        Authentication authentication = this.usersService.getCurrentLoggedInAuthenticationObject();
        if (authentication == null) return false;
        return isUserInRole(authentication, authority);
    }

    private boolean isUserInRole(Authentication authentication, String authority) {
        if (authentication == null)
            return false;
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }

    private boolean isUserInRole(String authority) {
        return usersService.isUserInRole(usersService.getCurrentLoggedInUser(), authority);
    }

    private boolean isUserInRole(User user, String authority) {
        return usersService.isUserInRole(user, authority);
    }

    public boolean canUpdateProducts(User user) {
        return false;
    }

    public boolean canCreateTags(User user) {
        return tagsAuthorizationDelegate.canCreateTags(user);
    }

    public boolean canUpdateTags(User user, Tag tag) {
        return tagsAuthorizationDelegate.canUpdateTag(user, tag);
    }

    public boolean canDeleteTags(User user, Tag tag) {
        return tagsAuthorizationDelegate.canDeleteTag(user);
    }

    public boolean canReadTags(User user) {
        return tagsAuthorizationDelegate.canReadTag(user);
    }


    public boolean canReadCategories(User user) {
        return categoriesAuthorizationDelegate.canReadCategories(user);
    }

    public boolean canCreateCategories(User user) {
        return categoriesAuthorizationDelegate.canCreateCategories(user);
    }

    public boolean canDeleteCategories(User user) {
        return categoriesAuthorizationDelegate.canDeleteCategories(user);
    }

    public boolean canUpdateCategories(User user, Category category) {
        return categoriesAuthorizationDelegate.canUpdateCategories(user, category);
    }


    public boolean isUserAdmin(User user) {
        return this.usersService.isUserAdmin(user);
    }
}