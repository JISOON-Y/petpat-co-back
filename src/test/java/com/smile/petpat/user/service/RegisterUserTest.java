package com.smile.petpat.user.service;

import com.smile.petpat.common.exception.CustomException;
import com.smile.petpat.common.response.ErrorCode;
import com.smile.petpat.user.domain.User;
import com.smile.petpat.user.domain.UserService;
import com.smile.petpat.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RegisterUserTest {
    @Autowired
    private UserService userService;

    @Nested
    @DisplayName("회원가입 성공")
    class RegisterSuccess{

        @Test
        @DisplayName("Success")
        void registerSuccess(){

            //given
            UserDto.RegisterUserRequest request =
                    UserDto.RegisterUserRequest.builder()
                            .userEmail("userEmail_TEST")
                            .nickname("nickname_TEST")
                            .password("test11!!")
                            .profileImgPath("TEST.jpg")
                            .build();

            //when
            User savedUser =
                    userService.registerUser(request.toCommand());

            //then
            assertNotNull(savedUser.getId());
            assertEquals(savedUser.getUserEmail(),"userEmail_TEST");
            assertEquals(savedUser.getNickname(),"nickname_TEST");
            assertNotEquals(savedUser.getPassword(),"test11!!");
        }
    }

    @Nested
    @DisplayName("회원가입 실패")
    class RegisterFail{

        @Nested
        @DisplayName("증복")
        class duplicatedInfo{

            @BeforeEach
            void setup(){
                UserDto.RegisterUserRequest preUser =
                    UserDto.RegisterUserRequest.builder()
                            .userEmail("userEmail_TEST")
                            .nickname("nickname_TEST")
                            .password("test!!")
                            .profileImgPath("TEST.jpg")
                            .build();

                userService.registerUser(preUser.toCommand());
            }

            @Test
            @DisplayName("이메일 중복")
            void registerFail_Duplicated_Nickname(){

                //given
                UserDto.RegisterUserRequest request =
                        UserDto.RegisterUserRequest.builder()
                                .userEmail("userEmail_TEST")
                                .nickname("nickname_TEST_Another")
                                .password("test!!")
                                .profileImgPath("TEST.jpg")
                                .build();

                //when & then
                Exception ex = assertThrows(CustomException.class,
                    ()->userService.registerUser(request.toCommand()));
                assertEquals(ex.getMessage(),
                        ErrorCode.ILLEGAL_USERNAME_DUPLICATION.getMessage());
            }
            @Test
            @DisplayName("닉네임 중복")
            void registerFail_Duplicated_UserEmail(){

                //given
                UserDto.RegisterUserRequest request =
                        UserDto.RegisterUserRequest.builder()
                                .userEmail("userEmail_TEST_Another")
                                .nickname("nickname_TEST")
                                .password("test!!")
                                .profileImgPath("TEST.jpg")
                                .build();

                //when & then
                Exception ex = assertThrows(CustomException.class,
                        ()->userService.registerUser(request.toCommand()));
            }
        }
    }
}
