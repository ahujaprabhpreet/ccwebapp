package com.neu.cloud.webapp.user;

import com.neu.cloud.webapp.response.CustomResponse;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StatsDClient statsDClient;


    @GetMapping("/")
    public ResponseEntity<Void> beginApp(){
        statsDClient.incrementCounter("endpoint.user.home.http.get");
        LocalTime localTime= LocalTime.now();
        return new ResponseEntity(new CustomResponse(new Date(), localTime.toString() ,"" ),HttpStatus.OK);

    }

    @PostMapping("/user/register")
    public ResponseEntity<User> register(@RequestBody User user){

        statsDClient.incrementCounter("endpoint.user.register.http.post");

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";

        Pattern passPattern = Pattern.compile(PASSWORD_PATTERN);

        User findUser = userRepository.findUsersByUsername(user.getUsername());

        if(findUser!=null) {
            logger.warn("User Already Exists");
            return new ResponseEntity(new CustomResponse(new Date(), "User Already Exist", ""), HttpStatus.CONFLICT);
        }

        if (user.getUsername() == null){
            logger.warn("Username must not be empty");
            return new ResponseEntity(new CustomResponse(new Date(),"Username must not be empty","" ),HttpStatus.BAD_REQUEST);
        }

        if(!pat.matcher(user.getUsername()).matches()){
            logger.warn("Username must be valid email");
            return new ResponseEntity(new CustomResponse(new Date(),"Username must be valid email","" ),HttpStatus.BAD_REQUEST);
        }

        if(!passPattern.matcher(user.getPassword()).matches()){
            logger.warn("Enter a valid password");
            return new ResponseEntity(new CustomResponse(new Date(),"Enter a valid password "," " +
                    "Be between 8 and 40 characters long  ||  " +
                    "Contain at least one digit.  ||  " +
                    "Contain at least one lower case character.  ||  " +
                    "Contain at least one upper case character.  ||  " +
                    "Contain at least on special character from [ @ # $ % ! . ].  ||  " ),HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        logger.info("User created Successfully!");
        return new ResponseEntity(new CustomResponse(new Date(),"User Created","" ),HttpStatus.CREATED);

    }




}
