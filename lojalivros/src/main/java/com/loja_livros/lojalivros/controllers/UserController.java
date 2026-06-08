package com.loja_livros.lojalivros.controllers;

import com.loja_livros.lojalivros.dtos.RequestRecordDto;
import com.loja_livros.lojalivros.dtos.ResponseRecordDto;
import com.loja_livros.lojalivros.dtos.UserRecordDto;
import com.loja_livros.lojalivros.models.UserModel;
import com.loja_livros.lojalivros.repositories.UserRepository;
import com.loja_livros.lojalivros.services.UserService;
import com.loja_livros.lojalivros.services.infra.security.TokenService;
import com.loja_livros.lojalivros.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.loja_livros.lojalivros.exceptions.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookstore/auth")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UserController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseRecordDto> login(@RequestBody UserRecordDto userRecordDto){
        if (userRecordDto.email() == null || userRecordDto.email().isBlank()) {
            throw new BadRequestException("Email is required");
        }
        if (userRecordDto.password() == null || userRecordDto.password().isBlank()) {
            throw new BadRequestException("Password is required");
        }

        UserModel userModel = this.userRepository.findByEmail(userRecordDto.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(userRecordDto.password(), userModel.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = this.tokenService.generateToken(userModel);
        return ResponseEntity.ok().body(new ResponseRecordDto(userModel.getName(), token));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseRecordDto> register(@RequestBody RequestRecordDto requestRecordDto) {
        if (requestRecordDto.email() == null || requestRecordDto.email().isBlank()) {
            throw new BadRequestException("Email is required");
        }
        if (requestRecordDto.password() == null || requestRecordDto.password().isBlank()) {
            throw new BadRequestException("Password is required");
        }

        Optional<UserModel> userModelOptional = userRepository.findByEmail(requestRecordDto.email());
        if (userModelOptional.isPresent()) {
            throw new ConflictException("User with this email already exists");
        }

        UserModel newUserModel = userService.saveUser(requestRecordDto);
        String token = this.tokenService.generateToken(newUserModel);
        return ResponseEntity.created(URI.create("/api/bookstore/auth/register"))
                .body(new ResponseRecordDto(newUserModel.getName(), token));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllUsers(){
        List<UserModel> users = userService.getAllUsers();
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }
        return ResponseEntity.ok(users);
    }
}