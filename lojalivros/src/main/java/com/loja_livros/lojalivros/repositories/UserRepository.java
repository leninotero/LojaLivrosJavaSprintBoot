package com.loja_livros.lojalivros.repositories;

import com.loja_livros.lojalivros.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    // This interface will automatically provide CRUD operations for UserModel
    // and can be extended with custom query methods if needed.
    Optional<UserModel> findByEmail(String email);
}
