package com.smile.petpat.user.service;

import com.smile.petpat.common.exception.CustomException;
import com.smile.petpat.common.response.ErrorCode;
import com.smile.petpat.image.domain.ImageUploadManager;
import com.smile.petpat.user.domain.User;
import com.smile.petpat.user.domain.UserCommand;
import com.smile.petpat.user.domain.UserModify;
import com.smile.petpat.user.domain.UserReader;
import com.smile.petpat.user.dto.UserDto;
import com.smile.petpat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserModifyImpl implements UserModify {

    private final UserRepository userRepository;
    private final UserReader userReader;
    private final ImageUploadManager imageUploadManager;
    private final PasswordEncoder passwordEncoder;

    /**프로필 수정
     * userDetails 확인 필요
     */
    @Override
    @Transactional
    public User modifyProfile(UserDto.ModifyUserRequest request, User loginUser) {
        User user = userRepository.findById(loginUser.getId()).orElseThrow(
                ()->new CustomException(ErrorCode.ILLEGAL_USER_NOT_EXIST)
        );
        userReader.getUserByNickName(request.getUsername());
        String filepath = imageUploadManager.saveProfileImage(request.getProfileImgFile(),
                request.getProfileImgUrl());

        UserCommand userCommand = new UserCommand(request,filepath);
        user.modifyProfile(userCommand);
        return userRepository.save(user);
    }

    /**비밀번호 확인
     *
     * @param password
     * @param user
     * @return
     */
    @Override
    public Boolean passwordCheck(String password, User user) {
        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new CustomException(ErrorCode.ILLEGAL_PASSWORD_NOT_VALID);
        return null;
    }

    /**
     * 비밀번호 수정
     * userDetails 확인 필요
     */
    @Override
    @Transactional
    public User modifyPassword(UserDto.ModifyPasswordRequest request,User loginUser) {
        if (!request.getNewPassword().equals(request.getNewPasswordChk()))
            throw new CustomException(ErrorCode.ILLEGAL_PASSWORD_NOT_CORRECT);

        User user = userRepository.findById(loginUser.getId()).orElseThrow(
                ()->new CustomException(ErrorCode.ILLEGAL_USER_NOT_EXIST)
        );

        user.modifyPassword(passwordEncoder.encode(request.getNewPassword()));
        return userRepository.save(user);
    }
}
