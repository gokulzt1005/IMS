package com.example.demo.Controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.ImsRegisterDto;
import com.example.demo.Entity.ImsEntity;
import com.example.demo.Repository.ImsRepository;
import com.example.demo.Dto.ImsLoginDto;
import com.example.demo.Service.ImsService;

@RestController
@RequestMapping("/v1/api")
public class ImsController {

	@Autowired
	private ImsService imsService;
	
	
	@Autowired
	private ImsRepository customerRepository;
	
	
	@PostMapping(path="/register")
	 public ResponseEntity<Map<String, Object>> registerCustomer(@RequestBody ImsRegisterDto customerDto) {
		return imsService.CustomerRegister(customerDto);

    }
	
	
	@PostMapping(path="/login")
	 public ResponseEntity<Map<String, Object>> UserLogin(@RequestBody ImsLoginDto userLoginDto) {
	        return imsService.Customerlogin(userLoginDto);
	    }
	
	
	@GetMapping("ims/{id}")
	 public ResponseEntity<Map<String, Object>> getUserDataById(
			 @PathVariable Integer id
		    ) {
		        return imsService.getUserData(id, null, null, null);
		    }
	
	  @GetMapping("/user/{email}")
	  public ResponseEntity<Map<String, Object>> getUserDataByEmail(
			  @PathVariable String email
		    ) {
		        return imsService.getUserEmail(email, null, null, null);
		    }
	  
	  @GetMapping("/logout")
	    public String logoutApi(HttpServletRequest request, HttpServletResponse response) {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        if (auth != null) {
	            new SecurityContextLogoutHandler().logout(request, response, auth);
	        }
	        return "Logged out successfully";
	    }
	  
	  
}
