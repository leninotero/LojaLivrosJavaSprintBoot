package com.loja_livros.lojalivros.services;

import com.loja_livros.lojalivros.dtos.RequestRecordDto;
import com.loja_livros.lojalivros.models.UserModel;
import com.loja_livros.lojalivros.repositories.UserRepository;
import com.loja_livros.lojalivros.services.infra.security.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public UserModel saveUser(RequestRecordDto requestRecordDto) {
        UserModel userModel = new UserModel();
        userModel.setName(requestRecordDto.name());
        userModel.setEmail(requestRecordDto.email());
        userModel.setPassword(passwordEncoder.encode(requestRecordDto.password()));
        userRepository.save(userModel);

        return userRepository.save(userModel);
    }
}
