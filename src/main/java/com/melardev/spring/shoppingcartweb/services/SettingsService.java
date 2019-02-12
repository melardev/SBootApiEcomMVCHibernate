package com.melardev.spring.shoppingcartweb.services;


import com.melardev.spring.shoppingcartweb.enums.AuthorizationPolicy;
import com.melardev.spring.shoppingcartweb.services.interfaces.ISettingsService;
import org.springframework.stereotype.Service;

@Service
public class SettingsService implements ISettingsService {


    // =============================
    //  Paging
    // =============================

    @Override
    public int getPageSize() {
        return 5;
    }

    // =============================
    //  Seeding
    // =============================

    @Override
    public int getMaxCommentsToSeed() {
        return 17;
    }

    @Override
    public int getMaxCategoriesToSeed() {
        return 5;
    }

    @Override
    public int getMaxUsersToSeed() {
        return 10;
    }

    @Override
    public int getMaxProductsToSeed() {
        return 25;
    }

    @Override
    public int getMaxTagsToSeed() {
        return 5;
    }

    @Override
    public int getOrdersToSeedCount() {
        return 30;
    }

    // =============================
    //  Authentication/Authorization
    // =============================



    @Override
    public AuthorizationPolicy getWhoCanEditProducts() {
        return AuthorizationPolicy.ONLY_ADMIN;
    }

    @Override
    public String getAdminRoleName() {
        return "ROLE_ADMIN";
    }

    @Override
    public String getAnonymousRoleName() {
        return "ROLE_ANONYMOUS";
    }

    @Override
    public AuthorizationPolicy getWhoCanDeleteProducts() {
        return AuthorizationPolicy.ONLY_ADMIN;
    }


    @Override
    public String getDefaultAdminEmail() {
        return "admin@admin.com";
    }

    @Override
    public String getDefaultAdminUsername() {
        return "admin";
    }

    @Override
    public String getDefaultAdminPassword() {
        return "password";
    }

    @Override
    public String getDefaultAdminLastName() {
        return "admin";
    }

    @Override
    public String getDefaultAdminFirstName() {
        return "admin";
    }

    @Override
    public AuthorizationPolicy getWhoCanCreateComments() {
        return AuthorizationPolicy.ADMIN_AND_OWNER;
    }

    @Override
    public AuthorizationPolicy getWhoCanUpdateComments() {
        return AuthorizationPolicy.ADMIN_AND_OWNER;
    }

    @Override
    public AuthorizationPolicy getWhoCanDeleteComments() {
        return AuthorizationPolicy.ADMIN_AND_OWNER;
        //return config.getCommentPolicies().getDelete();
    }

    @Override
    public String getAuthenticatedRoleName() {
        return "ROLE_USER";
    }

    @Override
    public AuthorizationPolicy getWhocanCheckout() {
        return AuthorizationPolicy.AUTHENTICATED_USER;
    }

    @Override
    public AuthorizationPolicy getWhoCanEditComments() {
        return AuthorizationPolicy.ONLY_ADMIN;
    }

    @Override
    public AuthorizationPolicy getWhoCanCreateProducts() {
        return AuthorizationPolicy.ONLY_ADMIN;
    }

    @Override
    public AuthorizationPolicy getWhoCanUpdateProducts() {
        return AuthorizationPolicy.ONLY_ADMIN;
    }


    @Override
    public String getUploadsDirectory() {
        return System.getProperty("user.dir") + "/uploads";
    }


}