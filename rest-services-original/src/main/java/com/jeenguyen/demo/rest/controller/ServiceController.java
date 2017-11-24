package com.jeenguyen.demo.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jeenguyen.demo.bean.Random;

import java.security.Principal;
import java.security.SecureRandom;

/**
 * Created by jeebb on 20/11/2014.
 */
@RestController
@RequestMapping("/services")
public class ServiceController {

    @RequestMapping(value = "jsonrandom")
    public Random me() {
    	Random random = new Random();
    	random.setNumber(new SecureRandom().nextInt());
        return random;
    }
    
    @RequestMapping(value = "random")
    public Integer random() {
    	return new SecureRandom().nextInt();        
    }
    
    @RequestMapping(path = "account", method = RequestMethod.GET)
    public Principal oauth(Principal principal) {
        /*
         * Translate the incoming request, which has an access token
         * Spring security takes the incoming request and injects the Java Security Principal
         * The converter inside Spring Security will handle the to json method which the Spring Security
         * Oauth client will know how to read
         *
         * The @EnableResourceServer on the application entry point is what makes all this magic happen.
         * If there is an incoming request token it will check the token validity and handle it accordingly
         */
        return principal;
    }

}
