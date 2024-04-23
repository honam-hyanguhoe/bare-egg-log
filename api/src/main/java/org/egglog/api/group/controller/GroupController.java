package org.egglog.api.group.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.group.model.dto.request.GroupForm;
import org.egglog.api.group.model.dto.request.GroupUpdateForm;
import org.egglog.api.group.model.dto.request.InvitationAcceptForm;
import org.egglog.api.group.model.dto.response.GroupDto;
import org.egglog.api.group.model.dto.response.GroupMemberDto;
import org.egglog.api.group.model.dto.response.GroupPreviewDto;
import org.egglog.api.group.model.service.GroupService;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.egglog.api.user.model.entity.Users;

import java.util.List;


@RestController
@RequestMapping("/v1/groups")
@RequiredArgsConstructor
@Slf4j
public class GroupController {
    private final GroupService groupService;

    //TODO kafka 적용해 이벤트 큐에 파싱 요청 송신하는 형태로 작성할 것
    @PostMapping("/duty")
    public ResponseEntity generateGroupDuty(){
        Long userId=1L;
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    @PostMapping("/invitation/accept")
    public ResponseEntity acceptInvitaion(
            @RequestBody InvitationAcceptForm acceptForm
//            TODO @AuthenticationPrincipal User user){
    ){
        Users user=null;
        groupService.acceptInvitation(acceptForm,user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.NO_CONTENT));
    }

    @GetMapping("/invitaion/{group_id}")
    public ResponseEntity getInvitation(
            @PathVariable("group_id") Long groupId
//            TODO @AuthenticationPrincipal User user
    ){
        Users user=null;
        String inviteCode = groupService.getOrGenerateInvitation(groupId,user);
        return ResponseEntity.ok().body(MessageUtils.success(inviteCode));
    }

    @DeleteMapping("/{group_id}/{member_id}")
    public ResponseEntity deleteGroupMember(
            @PathVariable("group_id") Long groupId,
            @PathVariable("member_id") Long memberId
//            TODO @AuthenticationPrincipal User user
    ){
        Users user=null;
        groupService.deleteGroupMember(groupId,memberId,user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

    @GetMapping("/list")
    public ResponseEntity getGroupList(
//            TODO @AuthenticationPrincipal User user
    ){
        Users user=null;
        List<GroupPreviewDto> groupList = groupService.getGroupList(user);
        return ResponseEntity.ok().body(MessageUtils.success(groupList));
    }

    @GetMapping("/{group_id}")
    public ResponseEntity retrieveGroup(@PathVariable("group_id") Long groupId
//            TODO @AuthenticationPrincipal User user
    ){
        Users user=null;
        GroupDto group = groupService.retrieveGroup(groupId,user);
        return ResponseEntity.ok().body(MessageUtils.success(group));
    }

    //TODO 수정 데이터 확인용 전송
    @PatchMapping("/{group_id}")
    public ResponseEntity updateGroup(
            @PathVariable("group_id") Long groupId,
            @RequestBody GroupUpdateForm groupUpdateForm
//            TODO @AuthenticationPrincipal User user
    ){
        Long userId=1L;
        return ResponseEntity.ok().body(
                MessageUtils.success(groupService.updateGroup(groupId,groupUpdateForm,userId)));
    }

    @PatchMapping("/{group_id}/{member_id}")
    public ResponseEntity updateGroupMember(
            @PathVariable("group_id") Long groupId,
            @PathVariable("member_id") Long memberId
//            TODO @AuthenticationPrincipal User user
    ){
        Users user=null;
        return ResponseEntity.ok().body(
                MessageUtils.success(
                        groupService.updateGroupMember(groupId, memberId, user)));
    }

    @DeleteMapping("/exit/{group_id}")
    public ResponseEntity exitGroup(
            @PathVariable("group_id") Long groupId
//            TODO @AuthenticationPrincipal User user
    ){
        Users user=null;
        groupService.exitGroup(groupId,user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

    @PostMapping("/")
    public ResponseEntity generateGroup(
            @RequestBody GroupForm groupForm
//            TODO @AuthenticationPrincipal User user
    ){
        Users user=null;
        groupService.generateGroup(groupForm,user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }
}
