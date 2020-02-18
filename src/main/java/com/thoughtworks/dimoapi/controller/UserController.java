package com.thoughtworks.dimoapi.controller;

import com.thoughtworks.dimoapi.entity.User;
import com.thoughtworks.dimoapi.model.LoginRequest;
import com.thoughtworks.dimoapi.model.Response;
import com.thoughtworks.dimoapi.service.DashboardService;
import com.thoughtworks.dimoapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    DashboardService dashboardService;

    @GetMapping(value="/hello")
    public  String sayHello(){
        return  "hello";
    }

    @PostMapping(value = "/signUp")
    public ResponseEntity createUser(@RequestBody User user) {
        try {
            if (userService.findByEmail(user.getEmail()) == null) {
                userService.save(user);
                return new ResponseEntity<>(new Response(true, "user created successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Response(false, "user already registered"), HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception occurred during user creation");
        }
    }

    @PostMapping(value = "/login")
    public  ResponseEntity doLogin(@RequestBody LoginRequest credential){
        try {
            User user = userService.findByEmail(credential.getEmail());
            if (user != null) {
                if (user.getPassword().equals(credential.getPassword())) {
                    return new ResponseEntity<>(new Response(true, "Login successfully"), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new Response(false, "Invalid user"), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(new Response(false, "Email id is not registered"), HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception occurred during user creation");
        }

    }

    @GetMapping(path = "/movie-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List> allMovieTypes() {
        return dashboardService.getMovieTypes();
    }
}
