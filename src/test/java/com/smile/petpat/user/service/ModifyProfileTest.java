package com.smile.petpat.user.service;

import com.smile.petpat.common.exception.CustomException;
import com.smile.petpat.common.response.ErrorCode;
import com.smile.petpat.image.repository.ImageRepository;
import com.smile.petpat.user.domain.User;
import com.smile.petpat.user.domain.UserModify;
import com.smile.petpat.user.dto.UserDto;
import com.smile.petpat.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ModifyProfileTest {
    @Autowired
    private UserModify userModify;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;

    MultipartFile multipartFile;
    MultipartFile multipartFile_Not_image;
    User loginUser;

    @BeforeEach
    void setup() throws Exception{
        User user = User.builder()
                .userEmail("userEmail_TEST")
                .nickname("nickname_TEST")
                .password("test11!!")
                .profileImgPath("TEST.jpg")
                .loginType(User.loginTypeEnum.NORMAL)
                .build();
        loginUser = userRepository.save(user);
        multipartFile= image();
        multipartFile_Not_image = notImage();
    }

    @Nested
    @DisplayName("프로필 수정 성공")
    class ModifySuccess{

        @Test
        @DisplayName("Success")
        void modifySuccess(){

            //given
            UserDto.ModifyUserRequest request=
                    UserDto.ModifyUserRequest.builder()
                            .username("nickname_modified")
                            .profileImgUrl("TEST.jpg")
                            .profileImgFile(multipartFile)
                            .build();

            //when
            userModify.modifyProfile(request,loginUser);

            //then
            User modifiedUser = userRepository.findById(loginUser.getId()).orElseThrow(
                    ()-> new CustomException(ErrorCode.ILLEGAL_USER_NOT_EXIST)
            );

            assertEquals(modifiedUser.getNickname(),"nickname_modified");
            assertNotEquals(modifiedUser.getProfileImgPath(),"TEST.jpg");
        }



    }

    @Nested
    @DisplayName("프로필 수정 실패")
    class ModifyFail{

        @Test
        @DisplayName("실패_이미지 파일 오류")
        void modifyFail_Wrong_Image_Format(){
            //given
            UserDto.ModifyUserRequest request=
                    UserDto.ModifyUserRequest.builder()
                            .username("nickname_modified")
                            .profileImgUrl("TEST.jpg")
                            .profileImgFile(multipartFile_Not_image)
                            .build();

            //when&then
            Exception ex = assertThrows(CustomException.class,
                    ()->userModify.modifyProfile(request,loginUser));
            assertEquals(ex.getMessage(),
                    ErrorCode.WRONG_TYPE_IMAGE.getMessage());

        }

        @Test
        @DisplayName("실패_닉네임 중복")
        void modifyFail_Nickname_Duplicated(){
            //given
            User user = User.builder()
                    .userEmail("userEmail_TEST_existed")
                    .nickname("nickname_TEST_existed")
                    .password("test11!!")
                    .profileImgPath("TEST.jpg")
                    .loginType(User.loginTypeEnum.NORMAL)
                    .build();

            userRepository.save(user);

            UserDto.ModifyUserRequest request=
                    UserDto.ModifyUserRequest.builder()
                            .username("nickname_TEST_existed")
                            .profileImgUrl("TEST.jpg")
                            .profileImgFile(multipartFile)
                            .build();

            //when&then
            Exception ex = assertThrows(CustomException.class,
                    ()->userModify.modifyProfile(request,loginUser));
            assertEquals(ex.getMessage(),
                    ErrorCode.ILLEGAL_NICKNAME_DUPLICATION.getMessage());

        }
    }

    MultipartFile image() throws Exception{
        return new MockMultipartFile("image","logo-1.png","image/png",
                new FileInputStream("C:/Users/장윤희/Desktop/petpat-co-back/build/resources/test/images/logo-1.png"));
    }

    MultipartFile notImage() throws Exception{
        return new MockMultipartFile("image","test.txt","image/png",
                new FileInputStream("C:/Users/장윤희/Desktop/petpat-co-back/build/resources/test/images/test.txt"));
    }
}
