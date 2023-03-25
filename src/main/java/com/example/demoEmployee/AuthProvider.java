package com.example.demoEmployee;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demoDataBase.DatabaseConnection;
import com.example.demoRestController.*;

@Component 
public class AuthProvider implements AuthenticationProvider { 
	 Logger logger = LoggerFactory.getLogger(AuthProvider.class);
	 
	 
   @Autowired
	DatabaseConnection db;
   @Autowired 
   private SecurityUserDetailsService userDetailsService; 
   @Autowired 
   private PasswordEncoder passwordEncoder; 


   @Override 
   public Authentication authenticate(Authentication authentication)  throws AuthenticationException { 
	   String username = authentication.getName();
       String password = authentication.getCredentials().toString();
       logger.info("Authentication:"+password);
       Login user = db.getUser(username);
       if (user == null) {
    	   logger.error("Authentication failed");
           throw new BadCredentialsException("Details not found");
       }
       logger.info("Userpassword:"+user.getPassword());
logger.info("encoded password"+passwordEncoder.encode(user.getPassword()));
       if (passwordEncoder.matches(password, user.getPassword())) {
           logger.info("Successfully Authenticated the user");
           return new UsernamePasswordAuthenticationToken(username, password, null);
       } else {
           throw new BadCredentialsException("Password mismatch");
       }
   
   } 
  
   @Override 
   public boolean supports(Class<?> authentication) { 
      return true; 
   }
}