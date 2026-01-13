package com.loja_livros.lojalivros.controllers;

import com.loja_livros.lojalivros.dtos.RequestRecordDto;
import com.loja_livros.lojalivros.dtos.ResponseRecordDto;
import com.loja_livros.lojalivros.dtos.UserRecordDto;
import com.loja_livros.lojalivros.models.UserModel;
import com.loja_livros.lojalivros.repositories.UserRepository;
import com.loja_livros.lojalivros.services.UserService;
import com.loja_livros.lojalivros.services.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookstore/auth")
public class UserController {
    // This controller can be used to handle authentication-related endpoints.
    // For example, you can implement login, registration, and token validation endpoints here.
    // Currently, it is empty, but you can add methods as needed for your authentication logic.
    @Autowired
    UserService userService;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserRecordDto userRecordDto){
        UserModel userModel = this.userRepository.findByEmail(userRecordDto.email()).orElseThrow(
                                            ()  -> new RuntimeException("User not found: " + userRecordDto.email()));
        if (passwordEncoder.matches(userRecordDto.password(), userModel.getPassword())) {
            String token = this.tokenService.generateToken(userModel);
            return ResponseEntity.ok().body(new ResponseRecordDto(userModel.getName(), token)); // Return the token in the response
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RequestRecordDto requestRecordDto) {
        Optional<UserModel> userModelOptional = userRepository.findByEmail(requestRecordDto.email());
        if (userModelOptional.isEmpty()) {
            var newUserModel = userService.saveUser(requestRecordDto);

            String token = this.tokenService.generateToken(newUserModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseRecordDto(newUserModel.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllUsers(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}