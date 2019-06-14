package com.neu.cloud.webapp.user;


import com.neu.cloud.webapp.response.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.Date;
import java.util.regex.Pattern;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ResponseEntity<Void> beginApp(){

        return new ResponseEntity(new CustomResponse(new Date(),"","" ),HttpStatus.OK);

    }

    @PostMapping("/user/register")
    public ResponseEntity<User> register(@RequestBody User user){


        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";

        Pattern passPattern = Pattern.compile(PASSWORD_PATTERN);

        User findUser = userRepository.findUsersByUsername(user.getUsername());

        if(findUser!=null)
            return new ResponseEntity(new CustomResponse(new Date(),"User Already Exist","" ), HttpStatus.CONFLICT);


        if (user.getUsername() == null)
            return new ResponseEntity(new CustomResponse(new Date(),"Username must not be empty","" ),HttpStatus.BAD_REQUEST);

        if(!pat.matcher(user.getUsername()).matches())
            return new ResponseEntity(new CustomResponse(new Date(),"Username must be valid email","" ),HttpStatus.BAD_REQUEST);

        if(!passPattern.matcher(user.getPassword()).matches())
            return new ResponseEntity(new CustomResponse(new Date(),"Enter a valid password "," " +
                    "Be between 8 and 40 characters long  ||  " +
                    "Contain at least one digit.  ||  " +
                    "Contain at least one lower case character.  ||  " +
                    "Contain at least one upper case character.  ||  " +
                    "Contain at least on special character from [ @ # $ % ! . ].  ||  " ),HttpStatus.BAD_REQUEST);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return new ResponseEntity(new CustomResponse(new Date(),"User Created","" ),HttpStatus.CREATED);

    }




}
