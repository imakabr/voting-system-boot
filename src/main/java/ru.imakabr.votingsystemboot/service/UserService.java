package ru.imakabr.votingsystemboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.imakabr.votingsystemboot.AuthorizedUser;
import ru.imakabr.votingsystemboot.model.User;
import ru.imakabr.votingsystemboot.repository.UserRepository;
import ru.imakabr.votingsystemboot.to.UserTo;
import ru.imakabr.votingsystemboot.util.UserUtil;


import java.util.List;

import static ru.imakabr.votingsystemboot.util.UserUtil.prepareToSave;
import static ru.imakabr.votingsystemboot.util.ValidationUtil.*;


@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {

    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(prepareToSave(user, passwordEncoder));
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public User get(int id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        return repository.findAll(SORT_NAME_EMAIL);
    }

    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        repository.save(prepareToSave(user, passwordEncoder));
    }

    @Transactional
    public void update(UserTo userTo) {
        User user = get(userTo.getId());
        repository.save(prepareToSave(UserUtil.updateFromTo(user, userTo), passwordEncoder));
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }

}