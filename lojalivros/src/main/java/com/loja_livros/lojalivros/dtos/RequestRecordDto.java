package com.loja_livros.lojalivros.dtos;

public record RequestRecordDto(String name, String email, String password) {
    // This record can be used to encapsulate the request data for user registration or login.
    // It contains fields for the user's name, email, and password.
    // You can add validation annotations if needed.
}
