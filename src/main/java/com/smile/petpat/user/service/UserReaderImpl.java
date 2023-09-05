package com.smile.petpat.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smile.petpat.common.exception.CustomException;

import com.smile.petpat.user.domain.User;
import com.smile.petpat.user.domain.UserReader;
import com.smile.petpat.user.dto.SocialUserDto;
import com.smile.petpat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.smile.petpat.common.response.ErrorCode.*;


@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    HttpHeaders headers = new HttpHeaders();

    @Override
    public void getUserByUserEmail(String userEmail) {
        userRepository.findByUserEmail(userEmail).ifPresent(
                user -> {
                    throw new CustomException(ILLEGAL_USERNAME_DUPLICATION);
                }
        );
    }

    @Override
    public void getUserByNickName(String nickName) {
        userRepository.findByNickname(nickName).ifPresent(
                user -> {
                    throw new CustomException(ILLEGAL_NICKNAME_DUPLICATION);
                }
        );
    }

    @Override
    public void getUser(User initUser) {
        isPwValid(initUser);
    }

    @Override
    public SocialUserDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        return new SocialUserDto(id, email, nickname, User.loginTypeEnum.KAKAO);
    }

    @Override
    public SocialUserDto getGoogleUserInfo(String accessToken) throws JsonProcessingException {
        // 헤더에 엑세스토큰 담기, Content-type 지정
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // POST 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleUser = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo",
                HttpMethod.POST, googleUser,
                String.class
        );

        // response에서 유저정보 가져오기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("sub").asLong();
        String email = jsonNode.get("email").asText();
        String nickname = jsonNode.get("name").asText();

        return new SocialUserDto(id, email, nickname, User.loginTypeEnum.GOOGLE);
    }

    @Override
    public SocialUserDto getGithubUserInfo(String accessToken) throws JsonProcessingException {
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // POST 요청 보내기
        HttpEntity<MultiValueMap<String, String>> githubUser = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.POST, githubUser,
                String.class
        );

        // response에서 유저정보 가져오기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("email").asText();
        String nickname = jsonNode.get("login").asText();

        return new SocialUserDto(id, email, nickname, User.loginTypeEnum.GITHUB);
    }

    public void isPwValid(User initUser) {
        User foundUser = userRepository.findByUserEmail(initUser.getUserEmail())
                .orElseThrow(() -> new CustomException(ILLEGAL_USER_NOT_EXIST));
        if (!passwordEncoder.matches(initUser.getPassword(), foundUser.getPassword())) {
            throw new CustomException(ILLEGAL_PASSWORD_NOT_VALID);
        }
    }

}
