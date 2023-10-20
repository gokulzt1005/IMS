package com.example.demo.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.ImsRegisterDto;
import com.example.demo.Dto.ImsLoginDto;
import com.example.demo.Entity.ImsEntity;
import com.example.demo.Repository.ImsRepository;

@Service
public class ImsService {
	
	@Autowired
	private ImsRepository customerRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
//	@Autowired
//    private EmailService emailService;
	
    
    
    public boolean checkAlreadyRegistered(String email) {
    	ImsEntity carbageData = customerRepository.findByEmail(email);
        if(carbageData != null) {
        	return true;
        }else {
        	return false;
        }
    }

    public boolean isValidPhoneNumber(String s) {
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }  
    
    public boolean isValidUsername(String username) {
        String unRegex = "^[A-Z0-9._%+-/!#$%&'*=?^_`{|}~]+$";
        Pattern unPattern = Pattern.compile(unRegex);
        Matcher unMatcher = unPattern.matcher(username.toUpperCase());
        return unMatcher.matches();
    }
    
    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])"
                       + "(?=.*[a-z])(?=.*[A-Z])"
                       + "(?=.*[@#$%^&+=])"
                       + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public ResponseEntity<Map<String, Object>> CustomerRegister(ImsRegisterDto customerDto) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();

        String email = customerDto.getEmail();
        String mobileNumber = customerDto.getMobile();
        String username = customerDto.getUsername();
        String password = customerDto.getPassword();

        boolean isValidEmail = isValidEmail(email);
        boolean isEmailNotRegistered = !checkAlreadyRegistered(email);
        boolean isValidUsername = isValidUsername(username);
        boolean isValidPassword = isValidPassword(password);

        Map<String, Object> userData = new LinkedHashMap<>();

        if (!isValidEmail) {
            userData.put("status_code", 400);
            userData.put("boolean", false);
            userData.put("message", "Invalid Email Id");
            dataList.add(userData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (!isEmailNotRegistered) {
            userData.put("status_code", 400);
            userData.put("boolean", false);
            userData.put("message", "Email already registered");
            dataList.add(userData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (!isValidUsername) {
            userData.put("status_code", 400);
            userData.put("boolean", false);
            userData.put("message", "Invalid username");
            dataList.add(userData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (!isValidPassword) {
            userData.put("status_code", 400);
            userData.put("boolean", false);
            userData.put("message", "Invalid password");
            dataList.add(userData);
            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            ImsEntity customerEntity = new ImsEntity();
            customerEntity.setUsername(username);
            customerEntity.setEmail(email);
            customerEntity.setMobile(mobileNumber);
            customerEntity.setPassword(passwordEncoder.encode(password));
            customerRepository.save(customerEntity);

            userData.put("userName", customerEntity.getUsername());
            userData.put("boolean", true);
            dataList.add(userData);
            
            Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("status_code", 200);
            meta.put("message", "Successfully registered");
            
            response.put("data", dataList);
            response.put("meta", meta);
            response.put("pagination", new LinkedHashMap<>());

            return ResponseEntity.status(HttpStatus.OK).body(response);
            
         
        }
   
    }
    
    
    public ResponseEntity<Map<String, Object>> Customerlogin(ImsLoginDto userLoginDto) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();
        
       ImsEntity user = null;
        
        if (userLoginDto.getEmail() != null) {
        	user = customerRepository.findByEmail(userLoginDto.getEmail());
        } else if (userLoginDto.getMobile() != null) {
        	user = customerRepository.findByMobile(userLoginDto.getMobile());
        }

        if (user != null) {
            String password = userLoginDto.getPassword();
            String encodedPassword = user.getPassword();
            boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);

            if (isPwdRight) {
                Map<String, Object> userData = new LinkedHashMap<>();
                userData.put("userName", user.getUsername());
                userData.put("boolean", true);
                dataList.add(userData);

                
                Map<String, Object> meta = new LinkedHashMap<>();
                meta.put("status_code", 200);
                meta.put("message", "Successfully registered");
               
                response.put("data", dataList);
                response.put("meta", meta);
                response.put("pagination", new LinkedHashMap<>());

                return ResponseEntity.ok(response);
            }
        }

        Map<String, Object> userData = new LinkedHashMap<>();
        userData.put("status_code", 401);
        userData.put("boolean", false);
        userData.put("message", "Login Failed");
        dataList.add(userData);

        response.put("data", dataList);
        response.put("meta", new LinkedHashMap<>());
        response.put("pagination", new LinkedHashMap<>());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    
    public boolean performLogoutAction(String mobile) {
        try {
            ImsEntity user = customerRepository.findByMobile(mobile);

            if (user != null) {
                return true; 
            } else {
                return false; 
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }
    }

    
    
    public ResponseEntity<Map<String, Object>> getUserData(int id, String username, String mobile, String email) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();

        try {
        	ImsEntity imsEntity = null;

            if (id > 0) {
            	imsEntity = customerRepository.findById((int) id).orElse(null);
            } else if (username != null) {
            	imsEntity = customerRepository.findByUsername(username);
            } else if (mobile != null) {
            	imsEntity = customerRepository.findByMobile(mobile);
            } else if (email != null) {
            	imsEntity = customerRepository.findByEmail(email);
            }

            if (imsEntity != null) {
                Map<String, Object> userData = new LinkedHashMap<>();
                userData.put("userId", imsEntity.getId()); 
                userData.put("userName", imsEntity.getUsername());
                userData.put("email", imsEntity.getEmail());
                userData.put("mobile", imsEntity.getMobile());

                dataList.add(userData);
                
                Map<String, Object> meta = new LinkedHashMap<>();
                meta.put("status_code", 200);
                meta.put("message", "Successfully registered");
                
                response.put("data", dataList);
                response.put("meta", meta);
                response.put("pagination", new LinkedHashMap<>());
                
                return ResponseEntity.ok(response); 
                
            } else {
                response.put("status_code", 404);
                response.put("boolean", false);
                response.put("message", "User not found");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Return a 404 Not Found response for user not found

            }
        } catch (Exception e) {
            response.put("status_code", 500);
            response.put("boolean", false);
            response.put("message", "Error fetching user data");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Return a 500 Internal Server Error response for errors

        }

    }
    
    public ResponseEntity<Map<String, Object>> getUserEmail(String email, Integer userId, String username, String mobile) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();

        try {
            ImsEntity imsEntity = null;

            if (email != null) {
                imsEntity = customerRepository.findByEmail(email);
            } else if (userId != null) {
                imsEntity = customerRepository.findById(userId).orElse(null);
            } else if (username != null) {
                imsEntity = customerRepository.findByUsername(username);
            } else if (mobile != null) {
                imsEntity = customerRepository.findByMobile(mobile);
            }

            if (imsEntity != null) {
                Map<String, Object> userData = new LinkedHashMap<>();
                userData.put("userId", imsEntity.getId());
                userData.put("userName", imsEntity.getUsername());
                userData.put("email", imsEntity.getEmail());
                userData.put("mobile", imsEntity.getMobile());

                dataList.add(userData);
                
                Map<String, Object> meta = new LinkedHashMap<>();
                meta.put("status_code", 200);
                meta.put("message", "Successfully registered");
                
                response.put("data", dataList);
                response.put("meta", meta);
                response.put("pagination", new LinkedHashMap<>());
                
                return ResponseEntity.ok(response); // Return a 200 OK response for success
                
            } else {
                response.put("status_code", 404);
                response.put("boolean", false);
                response.put("message", "User not found");
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // Return a 404 Not Found response for user not found

            }
        } catch (Exception e) {
            response.put("status_code", 500);
            response.put("boolean", false);
            response.put("message", "Error fetching user data");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Return a 500 Internal Server Error response for errors

        }

    }


}
