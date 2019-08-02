package com.neu.cloud.webapp.user;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.Topic;
import com.neu.cloud.webapp.response.CustomResponse;
import com.timgroup.statsd.StatsDClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import java.util.List;
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

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody User user){
        statsDClient.incrementCounter("endpoint.user.resetPassword.http.post");

        String parsedEmail = user.getUsername();

//        JSONParser parser = new JSONParser();
//        try {
//            JSONObject jo = (JSONObject) parser.parse(email);
//            parsedEmail = (String)jo.get("email");
//            logger.info("JSON parsed email: " + parsedEmail);
//
//            JSONObject body = new JSONObject(request.getRecords().get(0).getSNS().getMessage());
//
//        }
//        catch(ParseException ex){
//            logger.error("Error parsing email JSON for reset password" + ex.toString());
//        }

//      JsonObject jsonObject = new JsonObject();
        User exUser =  userRepository.findUsersByUsername(parsedEmail);

        if(exUser != null)
        {
//            AmazonSNS snsClient = AmazonSNSAsyncClientBuilder.standard()
//                    .withCredentials(new InstanceProfileCredentialsProvider(false))
//                    .build();

            AmazonSNS snsClient = AmazonSNSAsyncClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .build();

            List<Topic> topics = snsClient.listTopics().getTopics();

            for(Topic topic: topics)
            {
                if(topic.getTopicArn().endsWith("password_reset")){
//                    System.out.print(user.getUsername());
                    PublishRequest req = new PublishRequest(topic.getTopicArn(),exUser.getUsername());
                    snsClient.publish(req);
                    break;
                }
            }
            return new ResponseEntity(new CustomResponse(new Date(),"","" ),HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity(new CustomResponse(new Date(),"","" ),HttpStatus.BAD_REQUEST);
        }
    }
}
