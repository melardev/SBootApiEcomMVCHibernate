package com.melardev.spring.shoppingcartweb.services.auth;


import com.melardev.spring.shoppingcartweb.admin.forms.RoleCreateEditDto;
import com.melardev.spring.shoppingcartweb.errors.exceptions.ResourceNotFoundException;
import com.melardev.spring.shoppingcartweb.errors.exceptions.UnexpectedStateException;
import com.melardev.spring.shoppingcartweb.models.Order;
import com.melardev.spring.shoppingcartweb.models.Role;
import com.melardev.spring.shoppingcartweb.models.User;
import com.melardev.spring.shoppingcartweb.repository.UsersRepository;
import com.melardev.spring.shoppingcartweb.services.RolesService;
import com.melardev.spring.shoppingcartweb.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsersService implements UserDetailsService {


    /**
     * WARNING: if I use constructor DJ then circular dependecy : userService wants password encoder, which
     * is exposed by SecurityConfig, this triggers the creation of SecurityConfig,
     * at the same time SecurityConfig wants UsersService which is being created and remember he is asking
     * about password encoder, so this is a circular issue and app crashes.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UsersRepository userRepository;

    private final RolesService rolesService;
    @Autowired
    private SettingsService settingsService;


    @Autowired
    public UsersService(UsersRepository userRepository, RolesService rolesService) {
        this.userRepository = userRepository;
        this.rolesService = rolesService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO: this works because properties yaml has lazy_trans = true, change it to use @Transactional instead
//        User user = findByEmailNoException(email);
        // User user = findByUsernameNoException(username);
        User user = findByUsernameNoException(username);

        if (user == null) {

            /* Should I throw? or return a user with ROLE_ANONYMOUS? or with ROLE_USER?
            if (user == null) {
                return new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        getAuthoritiesFromRolesString(Arrays.asList(
                                roleRepository.findByName("ROLE_USER"))));
            }
            */
            // return null;
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        // User exists, we have to return an Implementation of UserDetails, let's use the default
        /*return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                getAuthoritiesFromRoles(user.getRoles()));*/
        return user;
    }


    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = privileges.stream().map(roleStr -> new SimpleGrantedAuthority(roleStr))
                .collect(Collectors.toList());
        return authorities;
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesFromRoles(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesFromRolesString(Collection<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toSet());
    }


    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getCurrentLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // AnonymousAuthenticationToken happens when anonymous authentication is enabled
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }

        String username = ((User) auth.getPrincipal()).getUsername();

        return findByUsernameNoException(username);
    }

    public boolean changePassword(User user, String password, String newPassword) {
        if (password == null || newPassword == null || password.isEmpty() || newPassword.isEmpty())
            return false;

        boolean match = passwordEncoder.matches(password, user.getPassword());
        if (!match)
            return false;

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return true;
    }

    public void resetPassword(String password) {
        resetPassword(getCurrentLoggedInUser(), password);
    }

    public void resetPassword(User user, String password) {
        resetPassword(user.getId(), password);
    }

    public void resetPassword(Long id, String password) {
        this.userRepository.changePassword(id, password);
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken))
            return true;
        return false;
    }

    public void loginManually(User user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                getSpringUserFromUser(user), user.getPassword(), getAuthoritiesFromRoles(user.getRoles()));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
    }


    public boolean isAnonymous(final Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.size() == 0)
            return true;

        String anonymous = this.settingsService.getAnonymousRoleName();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(anonymous));
    }

    private org.springframework.security.core.userdetails.User getSpringUserFromUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthoritiesFromRoles(user.getRoles()));
    }


    public User loggedInUser() {

        if (!isLoggedIn()) {
            return null;
        }

        // Get Spring User, it contains the username, now findById our User model object from the repository
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return userRepository.findByEmail(user.getUsername()).orElse(null);
        //return userRepository.findByUsername(user.getUsername());
    }


    // Edit controls are being showed up if the user is logged in and it's the same user viewing the file
    public boolean canEditProfile(User profileUser) {
        return isLoggedIn() && (profileUser.getId() == loggedInUser().getId());
    }


    public User findByEmailNoException(String email) {
        return findByEmail(email, false);
    }

    public User findByEmail(String email) {
        return findByEmail(email, true);
    }


    public User findByUsernameNoException(String username) {
        return findByUsername(username, false);
    }

    public User findByUsername(String username) {
        return findByUsername(username, true);
    }

    private User findByUsername(String username, boolean throwException) {
        Optional<User> user = userRepository.findByUsername(username);
        if (throwException)
            return user.orElseThrow(ResourceNotFoundException::new);
        return user.orElse(null);
    }

    /**
     * Returns the user using an email as a lookup field, if throwException
     * is true then throws exception if user not found, otherwise
     * returns the user or null if such user does not exist
     *
     * @param email
     * @param throwException
     * @return
     */
    private User findByEmail(String email, boolean throwException) {
        Optional<User> user = userRepository.findByEmail(email);
        if (throwException)
            return user.orElseThrow(ResourceNotFoundException::new);
        return user.orElse(null);
    }

    public User findById(Long id) {
        return findByIdThrowException(id);
    }

    public User findByIdThrowException(Long id) {
        return findById(id, true);
    }

    public User findById(Long id, boolean throwException) {
        Optional<User> user = this.userRepository.findById(id);
        if (throwException)
            return user.orElseThrow(ResourceNotFoundException::new);

        return user.orElse(null);
    }

    public User getById(Long id) {
        return findByIdThrowException(id);
    }

    public User updateUser(User user) {
        return this.userRepository.save(user);
    }

    public void delete(User user) {
        this.userRepository.delete(user);
    }

    public Collection<Order> getOrdersFromUser(int page, int pageSize) {
        User user = getCurrentLoggedInUser();
        if (user == null)
            throw new UnexpectedStateException();

        return user.getOrders().stream().skip((page - 1) * pageSize).limit(pageSize)
                .collect(Collectors.toList());
    }

    public Authentication getCurrentLoggedInAuthenticationObject() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
            return authentication;
        return null;
    }


    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> saveAll(Iterable<User> users) {
        users.forEach(u -> {
            u.setPassword(passwordEncoder.encode(u.getPassword()));
        });
        return this.userRepository.saveAll(users);
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public void loginManually(Long id) {
        User user = findById(id);
        loginManually(user);
    }

    public void loginManually(int id) {
        loginManually((long) id);
    }

    public User getRandom() {
        return userRepository.findRandom();

        /*Query countQuery = em.createNativeQuery("select count(*) from User");
        long count = (Long) countQuery.getSingleResult();

        Random random = new Random();
        int number = random.nextInt((int) count);

        Query selectQuery = em.createQuery("select q from User q");
        selectQuery.setFirstResult(number);
        selectQuery.setMaxResults(1);
        return (User) selectQuery.getSingleResult();*/
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.findByEmail(email).isPresent();
    }

    public boolean existsByUsername(String username) {
        return this.userRepository.findByUsername(username).isPresent();
    }

    public boolean isAnonymous() {
        return this.isAnonymous(SecurityContextHolder.getContext().getAuthentication());
    }

    private boolean isInRole(User user, String authorRoleName) {
        return user.getRoles().stream().anyMatch(r -> r.getName().equals(authorRoleName));
    }

    public User getFromPrincipal(Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            return (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }
        return null;
    }

    public boolean isNotAnounymous() {
        return !isAnonymous();
    }

    public long getAllCount() {
        return userRepository.count();
    }

    public Page<User> getLatest(int page, int count) {
        PageRequest pageRequest = PageRequest.of(page - 1, count, Sort.Direction.DESC, "createdAt");
        Page<User> result = this.userRepository.findAll(pageRequest);
        return result;
    }

    public User getReference(Long id) {
        return userRepository.getOne(id);
    }

    public User updateUser(User user, String firstName, String lastName, String username, String password, Set<RoleCreateEditDto> roles) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Set<Role> rolesSet;
        if (roles != null && roles.size() > 0)
            rolesSet = roles.stream().map(r -> rolesService.findOrCreateByName(r.getName(), r.getDescription())).collect(Collectors.toSet());
        else
            rolesSet = new HashSet<Role>(Collections.singletonList(rolesService.getOrCreate("ROLE_USER")));
        user.setRoles(rolesSet);
        return userRepository.save(user);
    }

    public boolean isUserInRole(User user, String role) {
        return user.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

    public boolean isUserAdmin(User user) {
        return this.isUserInRole(user, settingsService.getAdminRoleName());
    }
}
