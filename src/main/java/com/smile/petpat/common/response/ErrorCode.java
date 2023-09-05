package com.smile.petpat.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {

    // USER
    ILLEGAL_USERNAME_DUPLICATION(HttpStatus.BAD_REQUEST,  "중복된 아이디입니다."),

    ILLEGAL_NICKNAME_DUPLICATION(HttpStatus.BAD_REQUEST, "중복된 닉네임입니다."),

    ILLEGAL_USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "가입되지 않은 아이디입니다."),
    ILLEGAL_PASSWORD_NOT_VALID (HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD_PATTERN(HttpStatus.BAD_REQUEST,  "비밀번호는 8자 이상 12자 이하의 길이의 영문자, 숫자, 특수문자(!@#$%^&*)만 사용 가능합니다."),

    ILLEGAL_PASSWORD_NOT_CORRECT(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // IMAGE
    FAILED_UPLOAD_IMAGE(HttpStatus.BAD_REQUEST,  "이미지 업로드에 실패했습니다."),
    BELOW_MIN_IMAGE_COUNT(HttpStatus.BAD_REQUEST, "최소 1개이상의 이미지를 등록해야합니다."),
    WRONG_TYPE_IMAGE(HttpStatus.BAD_REQUEST,  "잘못된 형식의 파일입니다"),
    EXCEEDED_MAX_IMAGE_COUNT(HttpStatus.BAD_REQUEST, "업로드가능한 최대 이미지갯수를 초과하였습니다."),
    EXCEEDED_MAX_IMAGE_SIZE(HttpStatus.BAD_REQUEST,"업로드가능한 최대 이미지용량을 초과하였습니다.")

    // POST


    ;



    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus,  String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
