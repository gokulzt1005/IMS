package com.example.demo.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.demo.Entity.ImsEntity;
import com.example.demo.Model.User;
import com.example.demo.Repository.ImsRepository;




@Service
public class EmailMessageService {
	
	 private final TemplateEngine templateEngine;

	    private final JavaMailSender javaMailSender;
	    
	    @Autowired
		ImsRepository RegisterRepository;
	    
	    @Autowired
	    PasswordEncoder passwordEncoder;
		

	    
	    @Autowired
	    public EmailMessageService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
	        this.templateEngine = templateEngine;
	        this.javaMailSender = javaMailSender;
	    }
	    
	    public void sendMail(User user)  {
    	try {
	        Context context = new Context();
	        context.setVariable("user", user);
	        
	        String message = "Successfully Registered<br>" +
                    "NAME: " + user.getUsername() + "<br>" +
                    "EMAIL: " + user.getEmail() + "<br>" +
                    "MOBILE NUMBER: " + user.getMobile() + "<br>" +
                    "Successful registration, an email has been sent to your email address for verification<br>" +
                    "Best regards,<br>" +
                    "Inventory Management Services";
	        
	        javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
	        helper.setSubject("Welcome To Inventory Management System " + user.getUsername());
	        helper.setText(message, true);
	        helper.setTo(user.getEmail());
	        javaMailSender.send(mimeMessage);
    	}catch(Exception e) {
    		System.err.print(e);
    	}
    }
	    
	  public void sendOtpEmail(String toEmail, String otp) {
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(toEmail);
      mailMessage.setSubject("OTP Verification");
      mailMessage.setText("Congratulations! Your OTP verification was successful. You now have full access to your Inventory Management System. Start managing your inventory efficiently.\n"+"Your OTP is: " + otp);
      javaMailSender.send(mailMessage);
      
      ImsEntity pet = RegisterRepository.findByEmail(toEmail);
      if (pet != null) {
          pet.setOtp(otp);
          RegisterRepository.save(pet);
      }
  }
  
  
  public boolean verifyOTP(String email, String userOTP) {
	  ImsEntity pet = RegisterRepository.findByEmail(email);
      if (pet != null) {
          String storedOTP = pet.getOtp();
          if (storedOTP != null && storedOTP.equals(userOTP)) {
              pet.setOtp(null);
              RegisterRepository.save(pet);
              return true;
          }
      }
      return false;
  }
  
  public void updatePassword(String email, String newPassword) throws Exception {
	  ImsEntity pet = RegisterRepository.findByEmail(email);
      if (pet != null) {
          String encodedPassword = passwordEncoder.encode(newPassword);
          pet.setPassword(encodedPassword);
          RegisterRepository.save(pet);
      } else {
          throw new Exception("User not found for email: " + email);
      }
  }
  
  public ResponseEntity<String> resetPassword(String email, String userOTP, String newPassword, String confirmPassword) {
      ImsEntity user = RegisterRepository.findByEmail(email);

      if (user != null && user.getOtp().equals(userOTP)) {
          if (newPassword.equals(confirmPassword)) {
              if (isValidPassword(newPassword)) {
                  // Encode and update the user's password
                  String encodedPassword = passwordEncoder.encode(newPassword);
                  user.setPassword(encodedPassword);
                  RegisterRepository.save(user);
                  return ResponseEntity.ok("Password changed successfully.");
              } else {
                  return ResponseEntity.badRequest().body("Invalid password format. Password must meet the criteria.");
              }
          } else {
              return ResponseEntity.badRequest().body("New password and confirm password do not match.");
          }
      } else {
          return ResponseEntity.badRequest().body("Invalid OTP.");
      }
  }


  private boolean isValidPassword(String password) {
      String regex = "^(?=.*[0-9])"
                     + "(?=.*[a-z])(?=.*[A-Z])"
                     + "(?=.*[@#$%^&+=])"
                     + "(?=\\S+$).{8,20}$";
      return password.matches(regex);
  }
}
