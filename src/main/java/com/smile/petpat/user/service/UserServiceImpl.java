package com.smile.petpat.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smile.petpat.jwt.TokenProvider;
import com.smile.petpat.user.domain.*;
import com.smile.petpat.user.dto.SocialUserDto;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserReader userReader;
    private final UserStore userStore;
    private final UserAuth userAuth;
    private final TokenProvider tokenProvider;

    // 회원가입
    @Override
    @Transactional
    public User registerUser(UserCommand command) {

        userReader.getUserByUserEmail(command.getUserEmail());
        return userStore.store(command.toEntity());
    }

    // 로그인
    @Override
    @Transactional
    public String loginUser(UserCommand command) {

        User initUser = command.toLogin();
        userReader.getUser(initUser);

        return userAuth.getToken(initUser);
    }

    @Override
    public String kakaoUserLogin(String code) throws JsonProcessingException {
        String accessToken = userAuth.getKakaoAccessToken(code);
        SocialUserDto kakaoUserInfo = userReader.getKakaoUserInfo(accessToken);
        System.out.println("kakaoUserInfo: " + kakaoUserInfo);
        User kakaoUser = userStore.socialStore(kakaoUserInfo);

        return userAuth.forceLogin(kakaoUser);
    }

    @Override
    public void userIdValidChk(String userId) {
        userReader.getUserByUserEmail(userId);
    }

}
