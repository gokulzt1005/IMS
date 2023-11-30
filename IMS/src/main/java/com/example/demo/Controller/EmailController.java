package com.example.demo.Controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.User;
import com.example.demo.Utils.EmailMessageService;
import com.example.demo.Utils.OTPGenerator;

import jakarta.mail.MessagingException;

@RestController
public class EmailController {


    private final EmailMessageService emailService;

    public EmailController(EmailMessageService emailService) {
        this.emailService = emailService;
    }

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    @ResponseBody
    public String sendMail(@RequestBody User user) throws MessagingException {
        emailService.sendMail(user);
        return "Email Sent Successfully.!";
    }
    
    @PostMapping("/otp")
    public String generateOTP(@RequestParam String email) {
        String otp = OTPGenerator.generateOTP();
        emailService.sendOtpEmail(email, otp);
        return "OTP sent successfully!";
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam String userOTP) {
        if (emailService.verifyOTP(email, userOTP)) {
            return ResponseEntity.ok("OTP verification successful.");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }
    
    @PostMapping("/newpassword")
    public ResponseEntity<String> setNewPassword(@RequestParam String email, @RequestParam String newPassword, @RequestParam String confirmPassword) throws Exception {
        if (newPassword.equals(confirmPassword)) {
        	emailService.updatePassword(email, newPassword);
            return ResponseEntity.ok("Password changed successfully.");
        } else {
            return ResponseEntity.badRequest().body("New password and confirm password do not match.");
        }
    }
   
    
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String userOTP,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword
    ) {
        return emailService.resetPassword(email, userOTP, newPassword, confirmPassword);
    }

}
