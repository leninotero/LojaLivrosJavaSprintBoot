package com.loja_livros.lojalivros.services;

import com.loja_livros.lojalivros.dtos.RequestRecordDto;
import com.loja_livros.lojalivros.models.UserModel;
import com.loja_livros.lojalivros.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
*    Mockito
*     doReturn: É um stub do Mockito que faz o mock do repositório devolver um
*        objeto concreto quando o método de salvar for chamado.
*     Semantica: instrui o mock para que, ao invocar o método save no userRepository com
*        qualquer argumento (o matcher any()), o retorno seja o objeto user.
*     Componentes:
*       doReturn(...) — valor que será retornado.
*       when(mock).metodo(...) — especifica em qual mock e em qual chamada aplicar o stub.
*       any() — matcher do Mockito que aceita qualquer argumento.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Captor
    private ArgumentCaptor<UserModel> userArgumentCaptor;
    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("Validate test of method saveUser to UserService")
    class SaveUserTest {
        /*
        1) stuba passwordEncoder e userRepository;
        2) chama userService.saveUser(...);
        3) verifica que save foi chamado uma vez e captura o objeto persistido para asserções.
         */
        @Test
        @DisplayName("Should save user with success")
        void testShouldSaveUserWithSuccess() {
            // Arrange
            doReturn("encoded-password").when(passwordEncoder).encode(any());
            var savedUser = new UserModel();
                savedUser.setId(UUID.randomUUID());
                savedUser.setName("Name Test");
                savedUser.setEmail("test@email.com");
                savedUser.setPassword("encoded-password");

            doReturn(savedUser).when(userRepository).save(any(UserModel.class));

            var inputData = new RequestRecordDto(
                    "Name Test",
                    "test@email.com",
                    "123456"
            );

            // Act
            userService.saveUser(inputData);

            // Verify if save() method was called correctly
            verify(userRepository, times(1)).save(userArgumentCaptor.capture());
            var captured = userArgumentCaptor.getValue();       //get the capture values

            assertAll(
                    () -> assertEquals(inputData.name(), captured.getName()),
                    () -> assertEquals(inputData.email(), captured.getEmail()),
                    () -> assertEquals("encoded-password", captured.getPassword())
            );
        }
        @Test
        @DisplayName("Should try exception when error occurs")
        void testShouldTryExceptionWhenError(){
            doThrow(new RuntimeException()).when(userRepository).save(any());

            var inputData = new RequestRecordDto("Name Test",
                    "test@email.com",
                    "123456"
            );
            assertThrows(RuntimeException.class, () -> userService.saveUser(inputData));

        }
    }

    @Nested
    @DisplayName("Validate test of method listAllUsers to UserService")
    class ListAllUsers{
        @Test
        @DisplayName("Should list all user resgistered")
        void testShouldListAllUserResgistered() {

            //Arrange
            var savedUser = new UserModel();
                savedUser.setId(UUID.randomUUID());
                savedUser.setName("Name Test");
                savedUser.setEmail("test@email.com");
                savedUser.setPassword("encoded-password");

            doReturn(List.of(savedUser)).when(userRepository).findAll();

            //Act
            var result = userService.getAllUsers();

            //Assert
            assertNotNull(result);
            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Should throw exception when repository fails")
        void testShouldThrowWhenFindAllFails() {
            doThrow(new RuntimeException()).when(userRepository).findAll();

            assertThrows(RuntimeException.class, () -> userService.getAllUsers());

            verify(userRepository, times(1)).findAll();
        }
    }
}