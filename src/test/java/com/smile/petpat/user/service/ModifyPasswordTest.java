package com.smile.petpat.user.service;

import com.smile.petpat.common.exception.CustomException;
import com.smile.petpat.common.response.ErrorCode;
import com.smile.petpat.user.domain.User;
import com.smile.petpat.user.domain.UserModify;
import com.smile.petpat.user.dto.UserDto;
import com.smile.petpat.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class ModifyPasswordTest {
    @Autowired
    private UserModify userModify;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    User user;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .userEmail("userEmail_TEST")
                .nickname("nickname_TEST")
                .password(passwordEncoder.encode("test11!!"))
                .profileImgPath("TEST.jpg")
                .loginType(User.loginTypeEnum.NORMAL)
                .build();

        user = userRepository.save(user);
    }

    @Nested
    @DisplayName("비밀번호 수정 성공")
    class ModifyPasswordSuccess{

        @Test
        @DisplayName("Success")
        void modifySuccess(){
            //given
            UserDto.ModifyPasswordRequest request =
                    UserDto.ModifyPasswordRequest.builder()
                            .newPassword("newpassword11!!")
                            .newPasswordChk("newpassword11!!")
                            .build();
            //when
            userModify.modifyPassword(request,user);

            //then
            User modifiedUser = userRepository.findById(user.getId()).get();
            assertTrue(passwordEncoder.matches(request.getNewPassword(),
                    modifiedUser.getPassword()));
        }
    }

    @Nested
    @DisplayName("비밀번호 수정 실패")
    class ModifyPasswordFail{

        @Test
        @DisplayName("실패 _ 비밀번호 확인 불일치")
        void modifyFail_Password_Not_Correct(){
            //given
            UserDto.ModifyPasswordRequest request =
                    UserDto.ModifyPasswordRequest.builder()
                            .newPassword("newpassword11!!")
                            .newPasswordChk("wrongpassowrd11!!")
                            .build();

            //when&&then
            Exception ex = assertThrows(CustomException.class,
                    ()->userModify.modifyPassword(request,user));
            assertEquals(ex.getMessage(),
                    ErrorCode.ILLEGAL_PASSWORD_NOT_CORRECT.getMessage());
            assertTrue(passwordEncoder.matches("test11!!",
                    user.getPassword()));
        }

    }
}
