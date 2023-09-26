package com.smile.petpat.user.service;

import com.smile.petpat.common.exception.CustomException;
import com.smile.petpat.common.response.ErrorCode;
import com.smile.petpat.post.category.domain.CategoryGroup;
import com.smile.petpat.post.category.domain.PetCategory;
import com.smile.petpat.post.category.domain.PostType;
import com.smile.petpat.post.category.repository.CategoryGroupRepository;
import com.smile.petpat.post.category.repository.PetCategoryRepository;
import com.smile.petpat.post.rehoming.domain.Rehoming;
import com.smile.petpat.post.rehoming.domain.RehomingCommand;
import com.smile.petpat.post.rehoming.repository.RehomingRepository;
import com.smile.petpat.user.domain.ProfileService;
import com.smile.petpat.user.domain.User;
import com.smile.petpat.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
public class DeleteUserTest {
    @Autowired
    private ProfileService     profileService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RehomingRepository rehomingRepository;
    @Autowired
    private CategoryGroupRepository categoryGroupRepository;
    @Autowired
    private PetCategoryRepository petCategoryRepository;

    Rehoming rehoming;
    @BeforeEach
    void setup(){
        User user = User.builder()
                .userEmail("userEmail_TEST")
                .nickname("nickname_TEST")
                .password(passwordEncoder.encode("test11!!"))
                .profileImgPath("TEST.jpg")
                .loginType(User.loginTypeEnum.NORMAL)
                .build();

        user = userRepository.save(user);

        CategoryGroup categoryGroup =categoryGroupRepository.findById(1L).get();
        PetCategory petCategory = petCategoryRepository.findById(1L).get();


        rehoming =Rehoming.builder()
                .user(user)
                .title("title_TEST")
                .description("description_TEST")
                .petName("petName_TEST")
                .category(categoryGroup)
                .type(petCategory)
                .gender(RehomingCommand.PetGender.BOY)
                .cityName("cityName_TEST")
                .cityCountryName("cityCountryName_TEST")
                .townShipName("townShipName_TEST")
                .fullAdName("fullAdName_TEST")
                .build();

        rehoming = rehomingRepository.save(rehoming);
    }

    @Nested
    @DisplayName("아이디 삭제 성공")
    class DeleteSuccess{

        @Test
        @DisplayName("Success")
        void deleteSuccess() {
            //given
            User user = userRepository.findByUserEmail("userEmail_TEST").get();

            //when
            profileService.deleteUser(user);

            //then
            Rehoming rehoming_UserDeleted = rehomingRepository.findById(rehoming.getRehomingId()).get();
            Exception ex =assertThrows(NoSuchElementException.class,
                    ()->userRepository.findById(user.getId()).get());

            assertNotEquals(rehoming_UserDeleted.getUser().getNickname(),
                    "nickname_TEST");
            assertTrue(rehoming_UserDeleted.getUser().getNickname().contains("del"));
        }
    }

    @Nested
    @DisplayName("아이디 삭제 실패")
    class DeleteFail{

        @Test
        @DisplayName("실패 _ 존재하지 않는(이미 삭제된) 아이디")
        void deleteFail_User_Not_Exist(){
            //given
            User user = userRepository.findByUserEmail("userEmail_TEST").get();
            profileService.deleteUser(user);

            //when&&then
            Exception ex = assertThrows(CustomException.class,
                    ()-> profileService.deleteUser(user));
            assertEquals(ex.getMessage(),
                    ErrorCode.ILLEGAL_USER_NOT_EXIST.getMessage());
        }
    }
}
