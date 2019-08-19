package com.app.controller;

import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.Job;
import com.app.entity.User;
import com.app.security.auth.JwtUtil;
import com.app.service.JobService;
import com.app.service.UserService;
import com.app.util.PaginationUtil;

@RestController
public class MainController extends BaseController {
    
    @Value("${auth.header}")
    private String tokenHeader;
    
    @Autowired
    private JwtUtil jwtUtil;
    
	@Autowired
	private UserService userService;
    @Autowired
	private JobService jobService;
	
    @PreAuthorize("hasAnyRole('admin')")
	@GetMapping({"/api/users"}) 
	public ResponseEntity<?>  getUsers(HttpServletRequest request) { 
		  List<User> list = userService.findAll(); 
		  //System.out.println(list.size()); 
		  return ResponseEntity.ok(list); 
	}
    
    @PreAuthorize("hasAnyRole('user', 'admin')")
	@GetMapping({"/api/users/me"}) 
	public ResponseEntity<?>  getProfile(HttpServletRequest request) { 
    	String token = request.getHeader(tokenHeader);
		String username = jwtUtil.getUsernameFromToken(token);
		User user = userService.findByName(username);
		return ResponseEntity.ok(user); 
	}
	 
	@GetMapping({"/api/jobs"})
    public ResponseEntity<?> getJobs(Pageable pageable) throws URISyntaxException {
		Page<Job> page = jobService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jobs");
		//System.out.println(list.size());
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
