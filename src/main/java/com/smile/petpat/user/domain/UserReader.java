package com.smile.petpat.user.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smile.petpat.user.dto.SocialUserDto;

public interface UserReader {
    void getUserByUserEmail(String userEmail);
    void getUserByNickName(String nickName);
    void getUser(User user);

    SocialUserDto getKakaoUserInfo(String accessToken) throws JsonProcessingException;
    SocialUserDto getGoogleUserInfo(String accessToken) throws JsonProcessingException;
    SocialUserDto getGithubUserInfo(String accessToken) throws JsonProcessingException;
}
