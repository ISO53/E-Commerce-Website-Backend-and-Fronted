package com.perfume.perfume.controller;

import java.util.Random;
import com.google.common.hash.Hashing;
import com.perfume.perfume.model.User;
import com.perfume.perfume.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ISO53
 */
@CrossOrigin
@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getAll")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/getById") // getByID?id=12
    public User getById(@RequestParam("id") Integer id) {
        User user = findById(id);

        if (user == null) {
            return null;
        } else {
            return user;
        }
    }

    @PostMapping("/create") // getByID?User= obj???
    public ResponseEntity<User> create(@RequestBody User user) {

        try {
            User _user = userRepository
                    .save(new User(
                            user.getID(),
                            user.getName_surname(),
                            user.getLocation(),
                            sha256Encrypt(user.getPassword()),
                            user.getMail(),
                            user.getPhone_number(),
                            user.getFavorites(),
                            user.getShopping_cart()));
            return new ResponseEntity<>(_user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateByID") // updateByID?id=12
    public ResponseEntity<User> update(@RequestParam("id") Integer id, @RequestBody User user) {
        User findedUser = findById(id);

        if (findedUser != null) {
            findedUser.setID(user.getID());
            findedUser.setName_surname(user.getName_surname());
            findedUser.setLocation(user.getLocation());
            findedUser.setPassword(sha256Encrypt(user.getPassword()));
            findedUser.setMail(user.getMail());
            findedUser.setPhone_number(user.getPhone_number());
            findedUser.setFavorites(new ArrayList<>());
            findedUser.setShopping_cart(new ArrayList<>());
            return new ResponseEntity<>(userRepository.save(findedUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteById")   // deleteByID?id=12
    public ResponseEntity<HttpStatus> delete(@RequestParam("id") Integer id) {

        User user = findById(id);

        if (user != null) {
            userRepository.delete(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<HttpStatus> deleteAll() {
        try {
            userRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {

        if ("".equals(user.getPhone_number())
                || "".equals(user.getName_surname())
                || "".equals(user.getMail())
                || "".equals(user.getPassword())
                || "".equals(user.getLocation())) {
            return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
        }

        if (findByMail(user.getMail()) != null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
        }

        return create(new User(
                generateUniqueID(),
                user.getName_surname(),
                user.getLocation(),
                user.getPassword(),
                user.getMail(),
                user.getPhone_number(),
                user.getFavorites(),
                user.getShopping_cart()));
    }

    @GetMapping("/login")
    public ResponseEntity<User> login(@RequestParam("mail") String mail,
            @RequestParam("password") String password) {

        User foundedUser = findByMail(mail);

        if (foundedUser == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (!foundedUser.getPassword().equals(sha256Encrypt(password))) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(foundedUser, HttpStatus.ACCEPTED);
    }

    @GetMapping("/addProductToFavorites")
    public ResponseEntity<User> addProductToFavorites(@RequestParam("userId") Integer userId, @RequestParam("productId") Integer productId) {
        User findedUser = findById(userId);

        if (findedUser != null) {
            if (findedUser.getFavorites().contains(productId)) {
                return new ResponseEntity<>(userRepository.save(findedUser), HttpStatus.NOT_IMPLEMENTED);
            } else {
                findedUser.getFavorites().add(productId);
                return new ResponseEntity<>(userRepository.save(findedUser), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/addProductToCart")
    public ResponseEntity<User> addProductToCart(@RequestParam("userId") Integer userId, @RequestParam("productId") Integer productId) {
        User findedUser = findById(userId);

        if (findedUser != null) {
            if (findedUser.getShopping_cart().contains(productId)) {
                return new ResponseEntity<>(userRepository.save(findedUser), HttpStatus.NOT_IMPLEMENTED);
            } else {
                findedUser.getShopping_cart().add(productId);
                return new ResponseEntity<>(userRepository.save(findedUser), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/removeProductFromFavorites")
    public ResponseEntity<User> removeProductFromFavorites(@RequestParam("userId") Integer userId, @RequestParam("productId") Integer productId) {
        User findedUser = findById(userId);

        if (findedUser != null) {
            findedUser.getFavorites().remove(productId);
            return new ResponseEntity<>(userRepository.save(findedUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/removeProductFromCart")
    public ResponseEntity<User> removeProductFromCart(@RequestParam("userId") Integer userId, @RequestParam("productId") Integer productId) {
        User findedUser = findById(userId);

        if (findedUser != null) {
            findedUser.getShopping_cart().remove(productId);
            return new ResponseEntity<>(userRepository.save(findedUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/isInFavorites")
    public Boolean isInFavorites(@RequestParam("userId") Integer userId, @RequestParam("productId") Integer productId) {
        User findedUser = findById(userId);
        if (findedUser != null) {
            return findedUser.getFavorites().contains(productId);
        } else {
            return false;
        }
    }

    public User findById(Integer id) {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (Objects.equals(user.getID(), id)) {
                return user;
            }
        }
        return null;
    }

    public User findByMail(String mail) {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getMail().equals(mail)) {
                return user;
            }
        }

        return null;
    }

    public Integer generateUniqueID() {

        while (true) {
            int random = new Random().nextInt(Integer.MAX_VALUE - 1);

            if (findById(random) == null) {
                return random;
            }
        }
    }

    public String sha256Encrypt(String stringToEncode) {
        return Hashing.sha256().hashString(stringToEncode, StandardCharsets.UTF_8).toString();
    }
}
