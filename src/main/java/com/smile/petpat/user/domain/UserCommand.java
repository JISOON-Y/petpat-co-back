package com.smile.petpat.user.domain;

import com.smile.petpat.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@ToString
public class UserCommand {

    private String userEmail;
    private String nickname;
    private String password;
    private String profileImgPath;


    public UserCommand(String userEmail, String nickname, String password, String profileImgPath) {
        this.userEmail = userEmail;
        this.nickname = nickname;
        this.password = password;
        this.profileImgPath = profileImgPath;
    }

    public UserCommand(UserDto.ModifyUserRequest request, String profileImgPath){
        this.nickname = request.getUsername();
        this.profileImgPath = profileImgPath;
    }

    // 회원가입
    public User toEntity(){
        return User.builder()
                .userEmail(userEmail)
                .nickname(nickname)
                .password(password)
                .profileImgPath(profileImgPath)
                .build();
    }

    // 로그인
    public User toLogin(){
        return User.builder()
                .userEmail(userEmail)
                .password(password)
                .build();
    }
}
