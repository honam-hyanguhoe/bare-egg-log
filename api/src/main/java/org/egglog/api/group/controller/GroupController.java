package org.egglog.api.group.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.group.model.dto.request.GroupDutyData;
import org.egglog.api.group.model.dto.request.GroupForm;
import org.egglog.api.group.model.dto.request.GroupUpdateForm;
import org.egglog.api.group.model.dto.request.InvitationAcceptForm;
import org.egglog.api.group.model.dto.response.*;
import org.egglog.api.group.model.service.GroupService;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.egglog.api.user.model.entity.User;

import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.group.controller
 * fileName      : GroupController
 * description    : 유저의 그룹 관리하는 컨트롤러
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-24|김다희|최초 생성|
 * |2024-05-10|김형민|검증 추가|
 */
@RestController
@RequestMapping("/v1/groups")
@RequiredArgsConstructor
@Slf4j
public class GroupController {
    private final GroupService groupService;

    //TODO kafka 적용해 이벤트 큐에 파싱 요청 송신하는 형태로 작성할 것

    /**
     * 그룹 근무 엑셀 파일(json 변환 데이터) 업로드
     * @param user 로그인한 유저(JWT 토큰)
     * @param groupDutyData 근무 엑셀 파일 데이터
     * @return
     * @author 김다희
     */
    @PostMapping("/duty")
    public ResponseEntity<MessageUtils> generateGroupDuty(@AuthenticationPrincipal User user, @RequestBody GroupDutyData groupDutyData){
        Long userId=1L;
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }


    /**
     * 그룹 승인
     * @param user 로그인한 유저(JWT 토큰)
     * @param acceptForm invitationCode, password
     * @return
     * @author 김다희
     */
    @PostMapping("/invitation/accept")
    public ResponseEntity<MessageUtils> acceptInvitaion(
            @RequestBody @Valid InvitationAcceptForm acceptForm,
            @AuthenticationPrincipal User user){
        groupService.acceptInvitation(acceptForm,user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.NO_CONTENT));
    }


    /**
     * 그룹 초대 링크 생성
     * @param user 로그인한 유저(JWT 토큰)
     * @param groupId 생성할 groupID
     * @return
     * @author 김다희
     */
    @GetMapping("/invitaion/{group_id}")
    public ResponseEntity<MessageUtils<String>> getInvitation(
            @PathVariable("group_id") Long groupId,
            @AuthenticationPrincipal User user
    ){
        String inviteCode = groupService.getOrGenerateInvitation(groupId,user);
        return ResponseEntity.ok().body(MessageUtils.success(inviteCode));
    }


    /**
     * 그룹 강퇴
     * @param user 로그인한 유저(JWT 토큰)
     * @param groupId 강퇴시킬 그룹 ID
     * @param memberId 강퇴시킬 유저 ID
     * @return
     * @author 김다희
     */
    @DeleteMapping("/{group_id}/{member_id}")
    public ResponseEntity<MessageUtils> deleteGroupMember(
            @PathVariable("group_id") Long groupId,
            @PathVariable("member_id") Long memberId,
            @AuthenticationPrincipal User user
    ){
        groupService.deleteGroupMember(groupId,memberId,user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }


    /**
     * 그룹 리스트 조회
     * @param user 로그인한 유저(JWT 토큰)
     * @return
     * @author 김다희
     */
    @GetMapping("/list")
    public ResponseEntity<MessageUtils> getGroupList(
            @AuthenticationPrincipal User user
    ){
        List<GroupPreviewDto> groupList = groupService.getGroupList(user);
        return ResponseEntity.ok().body(MessageUtils.success(groupList));
    }


    /**
     * 그룹 상세 조회
     * @param user 로그인한 유저(JWT 토큰)
     * @param groupId 조회할 그룹 ID
     * @return
     * @author 김다희
     */
    @GetMapping("/{group_id}")
    public ResponseEntity<MessageUtils<GroupDto>> retrieveGroup(@PathVariable("group_id") Long groupId,
            @AuthenticationPrincipal User user
    ){
        GroupDto group = groupService.retrieveGroup(groupId,user);
        return ResponseEntity.ok().body(MessageUtils.success(group));
    }


    /**
     * 그룹 수정
     * @param user 로그인한 유저(JWT 토큰)
     * @param groupUpdateForm newName, newPassword
     * @param groupId 수정할 그룹 ID
     * @return
     * @author 김다희
     */
    //수정 데이터 확인용 전송
    @PatchMapping("/{group_id}")
    public ResponseEntity<MessageUtils> updateGroup(
            @PathVariable("group_id") Long groupId,
            @RequestBody GroupUpdateForm groupUpdateForm,
            @AuthenticationPrincipal User user
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(groupService.updateGroup(groupId,groupUpdateForm,user.getId())));
    }


    /**
     * 그룹 멤버 수정
     * @param user 로그인한 유저(JWT 토큰)
     * @param groupId 그룹 멤버 수정할 그룹 ID
     * @param memberId 수정할 멤버
     * @return
     * @author 김다희
     */
    @PatchMapping("/{group_id}/{member_id}")
    public ResponseEntity<MessageUtils<BossChangeDto>> updateGroupMember(
            @PathVariable("group_id") Long groupId,
            @PathVariable("member_id") Long memberId,
            @AuthenticationPrincipal User user
    ){
        groupService.updateGroupMember(groupId,memberId,user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.NO_CONTENT));
    }


    /**
     * 그룹 탈퇴
     * @param user 로그인한 유저(JWT 토큰)
     * @param groupId 탈퇴할 그룹 ID
     * @return
     * @author 김다희
     */
    @DeleteMapping("/exit/{group_id}")
    public ResponseEntity<MessageUtils> exitGroup(
            @PathVariable("group_id") Long groupId,
            @AuthenticationPrincipal User user
    ){
        groupService.exitGroup(groupId,user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }


    /**
     * 그룹 생성
     * @param user 로그인한 유저(JWT 토큰)
     * @param groupForm groupImage, groupPassword, groupName 다 필수
     * @return
     * @author 김다희
     */
    @PostMapping("")
    public ResponseEntity<MessageUtils> generateGroup(
            @RequestBody @Valid GroupForm groupForm,
            @AuthenticationPrincipal User user
    ){
        groupService.generateGroup(groupForm,user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }


    /**
     * 그룹 근무 형태별 근무자 목록
     * @param user 로그인한 유저(JWT 토큰)
     * @param groupId 조회를 희망하는 그룹 아이디
     * @param date 조회를 희망하는 날짜
     * @return
     * @author 김다희
     */
    @GetMapping("/duty/{groupId}")
    public ResponseEntity<MessageUtils<GroupDutySummary>> getGroupDuty(
            @AuthenticationPrincipal User user,
            @PathVariable("groupId") Long groupId,
            @RequestParam("date") String date
    ){
        return ResponseEntity.ok().body(MessageUtils.success(groupService.getGroupDuty(groupId,user,date)));
    }
}
