package com.smile.petpat.user.service;

import com.smile.petpat.common.exception.CustomException;
import com.smile.petpat.common.response.ErrorCode;
import com.smile.petpat.common.response.SuccessResponse;
import com.smile.petpat.user.domain.User;
import com.smile.petpat.user.domain.UserModify;
import com.smile.petpat.user.dto.ProfileDto;
import com.smile.petpat.user.dto.UserDto;
import com.smile.petpat.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class checkPasswordTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserModify userModify;
    @Autowired
    private PasswordEncoder passwordEncoder;

    User user;

    @BeforeEach
    void setup(){
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
    @DisplayName("비밀번호 확인 성공")
    class CheckSuccess{

        @Test
        @DisplayName("Success")
        void checkSuccess(){

            //given
            UserDto.CheckPasswordRequest request=
                    UserDto.CheckPasswordRequest.builder()
                            .password("test11!!")
                            .build();

            //when&&then
            assertTrue(userModify.passwordCheck(request.getPassword(), user));
        }
    }

    @Nested
    @DisplayName("비밀번호 오류")
    class CheckFail {

        @Test
        @DisplayName("실패_ 비밀번호 불일치")
        void checkFail_Wrong_Password() {

            //given
            UserDto.CheckPasswordRequest request =
                    UserDto.CheckPasswordRequest.builder()
                            .password("wrongpassword1!!")
                            .build();

            //when&&then
            Exception ex = assertThrows(CustomException.class,
                    ()->userModify.passwordCheck(request.getPassword(),user));
            assertEquals(ex.getMessage(),
                    ErrorCode.ILLEGAL_PASSWORD_NOT_VALID.getMessage());

        }
    }
}
