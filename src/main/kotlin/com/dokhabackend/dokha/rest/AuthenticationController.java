package com.dokhabackend.dokha.rest;

import com.dokhabackend.dokha.config.security.JwtTokenUtil;
import com.dokhabackend.dokha.dto.AuthenticationRequest;
import com.dokhabackend.dokha.entity.User;
import com.dokhabackend.dokha.entity.constant.RoleEnum;
import com.dokhabackend.dokha.service.UserService;
import kotlin.collections.SetsKt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@CrossOrigin
public class AuthenticationController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Autowired
    public AuthenticationController(BCryptPasswordEncoder passwordEncoder,
                                    AuthenticationManager authenticationManager,
                                    JwtTokenUtil jwtTokenUtil,
                                    UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getLogin(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final User user = userService.findByLogin(authenticationRequest.getLogin());

        return ResponseEntity.ok(jwtTokenUtil.generateToken(user));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest authenticationRequest) {

        User user = new User(null,
                authenticationRequest.getLogin(),
                passwordEncoder.encode(authenticationRequest.getPassword()),
                SetsKt.setOf(RoleEnum.ADMIN));

        userService.createUser(user);

        return ResponseEntity.ok(null);
    }
}
