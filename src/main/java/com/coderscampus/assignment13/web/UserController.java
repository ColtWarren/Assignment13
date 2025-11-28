package com.coderscampus.assignment13.web;

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountRepository accountRepo;

    @GetMapping("/register")
    public String getCreateUser (ModelMap model) {
        model.put("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String postCreateUser (User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String getAllUsers (ModelMap model) {
        Set<User> users = userService.findAll();

        model.put("users", users);
        if (users.size() == 1) {
            model.put("user", users.iterator().next());
        }

        return "users";
    }

    @GetMapping("/users/{userId}")
    public String getOneUser (ModelMap model, @PathVariable Long userId) {
        User user = userService.findById(userId);

        if (user.getAddress() == null) {
            Address address = new Address();
            address.setUser(user);
            user.setAddress(address);
        }

        model.put("users", Arrays.asList(user));
        model.put("user", user);
        return "users";
    }

    @PostMapping("/users/{userId}")
    public String postOneUser(User user) {
        User existingUser = userService.findById(user.getUserId());

        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());
        existingUser.setPassword(user.getPassword());

        if (existingUser.getAddress() == null) {
            existingUser.setAddress(new Address());
        }

        existingUser.getAddress().setAddressLine1(user.getAddress().getAddressLine1());
        existingUser.getAddress().setAddressLine2(user.getAddress().getAddressLine2());
        existingUser.getAddress().setCity(user.getAddress().getCity());
        existingUser.getAddress().setRegion(user.getAddress().getRegion());
        existingUser.getAddress().setCountry(user.getAddress().getCountry());
        existingUser.getAddress().setZipCode(user.getAddress().getZipCode());
        existingUser.getAddress().setUser(existingUser);

        userService.saveUser(existingUser);

        return "redirect:/users/" + existingUser.getUserId();
    }

    @PostMapping("/users/{userId}/delete")
    public String deleteOneUser (@PathVariable Long userId) {
        userService.delete(userId);
        return "redirect:/users";
    }

    @GetMapping("/users/{userId}/accounts/{accountId}")
    public String getAccount(@PathVariable Long userId, @PathVariable Long accountId, ModelMap model) {
        User user = userService.findById(userId);
        Account account = accountRepo.findById(accountId).orElse(new Account());

        int accountNumber = 0;
        for (int i = 0; i < user.getAccounts().size(); i++) {
            if (user.getAccounts().get(i).getAccountId().equals(accountId)) {
                accountNumber = i + 1;
                break;
            }
        }

        model.put("user", user);
        model.put("account", account);
        model.put("accountNumber", accountNumber);

        return "account";
    }

    @PostMapping("/users/{userId}/accounts/{accountId}")
    public String postAccount(@PathVariable Long userId, @PathVariable Long accountId, Account account) {
        account.setAccountId(accountId);
        accountRepo.save(account);

        return "redirect:/users/" + userId;
    }

    @PostMapping("/users/{userId}/accounts")
    public String createAccount(@PathVariable Long userId) {
        User user = userService.findById(userId);

        // Calculate the next account number
        int nextAccountNumber = user.getAccounts().size() + 1;

        Account newAccount = new Account();
        newAccount.setAccountName("Account #" + nextAccountNumber);
        newAccount.getUsers().add(user);
        user.getAccounts().add(newAccount);

        accountRepo.save(newAccount);
        userService.saveUser(user);

        return "redirect:/users/" + userId;

    }
}