package com.smile.petpat.user.controller;

import com.smile.petpat.common.response.SuccessResponse;
import com.smile.petpat.user.domain.ProfileService;
import com.smile.petpat.user.domain.User;
import com.smile.petpat.user.domain.UserModify;
import com.smile.petpat.user.dto.UserDto;
import com.smile.petpat.user.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final UserModify userModify;
    private final ProfileService profileService;

    /**
     * 프로필 조회
     * @return 성공 시 200 Success 반환
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public SuccessResponse getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return SuccessResponse.success(profileService.getProfile(userDetails.getUser()));
    }

    /**
     * 프로필 수정
     * @return 성공 시 200 Success 반환
     */
    @RequestMapping(value="",method = RequestMethod.PUT)
    public SuccessResponse modifyProfile(@ModelAttribute UserDto.ModifyUserRequest request,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        userModify.modifyProfile(request,userDetails.getUser());
        return SuccessResponse.noDataSuccess("OK");
    }

    /**
     * 비밀번호 확인
     * @return 성공 시 200 Success 반환
     */
    @RequestMapping(value ="/password",method = RequestMethod.POST)
    public SuccessResponse checkPassword(@RequestBody UserDto.CheckPasswordRequest request,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        userModify.passwordCheck(request.getPassword(),userDetails.getUser());
        return SuccessResponse.noDataSuccess("OK");
    }


    /**
     * 비밀번호 수정
     * @return 성공 시 200 Success 반환
     */
    @RequestMapping(value ="/password",method = RequestMethod.PUT)
    public SuccessResponse modifyPassword(@RequestBody UserDto.ModifyPasswordRequest request,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        userModify.modifyPassword(request,userDetails.getUser());
        return SuccessResponse.noDataSuccess("OK");
    }

    /**
     * 내가 쓴 분양 글 조회
     */
    @RequestMapping(value = "/rehoming", method = RequestMethod.GET)
    public SuccessResponse getMyRehoming(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PageableDefault Pageable pageable){
        return SuccessResponse.success(profileService.getMyRehoming(userDetails.getUser(),pageable));
    }

    /**
     * 내가 쓴 판매 글 조회
     */
    @RequestMapping(value = "/trade", method = RequestMethod.GET)
    public SuccessResponse getMyTrade(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PageableDefault Pageable pageable){
        return SuccessResponse.success(profileService.getMyTrade(userDetails.getUser(),pageable));
    }
    /**
     * 내가 쓴 질문 게시글
     */
    @RequestMapping(value = "/qna",method = RequestMethod.GET)
    public SuccessResponse getMyQna(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                    @PageableDefault Pageable pageable){
        return SuccessResponse.success(profileService.getMyQna(userDetails.getUser(),pageable));
    }


    /**
     * 내가 남긴 댓글 조회
     */
    @RequestMapping(value = "/comment",method = RequestMethod.GET)
    public SuccessResponse getMyComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @PageableDefault Pageable pageable){
        return SuccessResponse.success(profileService.getMyComment(userDetails.getUser(),pageable));
    }

    /**
     * 내가 북마크한 글 조회
     */
    @RequestMapping(value = "/bookmark",method = RequestMethod.GET)
    public SuccessResponse getPostsByBookmark(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PageableDefault Pageable pageable,
                                                 @RequestParam String postType){
        return SuccessResponse.success(profileService.getPostsByBookmark(userDetails.getUser(),pageable,postType));
    }
    /**
     * 내가 좋아요한 글 조회
     */
    @RequestMapping(value = "/like", method = RequestMethod.GET)
    public SuccessResponse getPostsByLike(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @PageableDefault Pageable pageable,
                                          @RequestParam String postType){
        return SuccessResponse.success(profileService.getPostsByLike(userDetails.getUser(),pageable,postType));
    }

    /**
     * 나의 최근 거래
     */
    @RequestMapping(value = "/recent",method = RequestMethod.GET)
    public SuccessResponse getMyRecentDeal(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return SuccessResponse.success(profileService.getMyRecentDeal());
    }

    /**
     * 회원 탈퇴
     */
    @RequestMapping(value = "",method = RequestMethod.DELETE)
    public SuccessResponse deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        profileService.deleteUser(userDetails.getUser());
        return SuccessResponse.noDataSuccess("삭제 완료!");
    }
}
