package com.smile.petpat.user.repository;

import com.smile.petpat.post.category.domain.CategoryGroup;
import com.smile.petpat.post.category.domain.PetCategory;
import com.smile.petpat.post.category.repository.CategoryGroupRepository;
import com.smile.petpat.post.category.repository.PetCategoryRepository;
import com.smile.petpat.post.rehoming.domain.Rehoming;
import com.smile.petpat.post.rehoming.domain.RehomingCommand;
import com.smile.petpat.post.rehoming.repository.RehomingRepository;
import com.smile.petpat.user.domain.User;
import com.smile.petpat.user.dto.ProfileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class getMyRehomingRepositoryTest {
    @Autowired
    private RehomingRepository rehomingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CategoryGroupRepository categoryGroupRepository;
    @Autowired
    private PetCategoryRepository petCategoryRepository;
    Pageable pageable = PageRequest.of(0,10);
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

        User anotherUser = User.builder()
                .userEmail("userEmail_ANOTHER")
                .nickname("nickname_ANOTHER")
                .password(passwordEncoder.encode("test11!!"))
                .profileImgPath("TEST.jpg")
                .loginType(User.loginTypeEnum.NORMAL)
                .build();
        anotherUser = userRepository.save(anotherUser);

        CategoryGroup categoryGroup =categoryGroupRepository.findById(1L).get();
        PetCategory petCategory = petCategoryRepository.findById(1L).get();

        Rehoming rehoming_TEST;

        //사용자가 5개의 Rehoming 게시글 작성
        for(int i=0; i<5; i++){
            rehoming_TEST = Rehoming.builder()
                    .user(user)
                    .title("title_TEST "+i)
                    .description("description_TEST "+i)
                    .petName("petName_TEST "+i)
                    .category(categoryGroup)
                    .type(petCategory)
                    .gender(RehomingCommand.PetGender.BOY)
                    .cityName("cityName_TEST "+i)
                    .cityCountryName("cityCountryName_TEST "+i)
                    .townShipName("townShipName_TEST "+i)
                    .fullAdName("fullAdName_TEST "+i)
                    .build();

            rehomingRepository.save(rehoming_TEST);
        }

        //다른 사용자가 2개의 Rehoming 게시글 작성
        for(int i=0; i<2; i++){
            rehoming_TEST = Rehoming.builder()
                    .user(anotherUser)
                    .title("title_anotherUser "+i)
                    .description("description_anotherUser "+i)
                    .petName("petName_anotherUser "+i)
                    .category(categoryGroup)
                    .type(petCategory)
                    .gender(RehomingCommand.PetGender.BOY)
                    .cityName("cityName_anotherUser "+i)
                    .cityCountryName("cityCountryName_anotherUser "+i)
                    .townShipName("townShipName_anotherUser "+i)
                    .fullAdName("fullAdName_anotherUser "+i)
                    .build();

            rehomingRepository.save(rehoming_TEST);
        }
    }

    @Test
    @DisplayName("내가 작성한 분양글 조회 성공")
    void Success(){
        List<ProfileDto.RehomingResponse> list =
                userRepository.getMyRehoming(user.getId(),pageable).getContent();

        assertEquals(5, list.size());
    }

}
