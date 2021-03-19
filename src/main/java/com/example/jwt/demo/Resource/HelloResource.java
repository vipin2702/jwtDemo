package com.example.jwt.demo.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt.demo.model.AuthenticationRequest;
import com.example.jwt.demo.model.AuthenticationResponse;
import com.example.jwt.demo.security.MyUserDetailsService;
import com.example.jwt.demo.security.jwt.JwtUtil;

@RestController
public class HelloResource {

	@Autowired
	AuthenticationManager manager;

	@Autowired
	MyUserDetailsService myUserDetailsService;

	@Autowired
	JwtUtil jwtTokenUtil;

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest authRequest)
			throws Exception {

		try {
			manager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
		} catch (BadCredentialsException ex) {
			throw new Exception("Bad username or password");

		}

		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authRequest.getUserName());
		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}
}
