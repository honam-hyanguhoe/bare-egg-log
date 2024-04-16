//package org.egglog.api.group.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.egglog.api.global.util.MessageUtils;
//import org.egglog.api.group.model.dto.OutputSpec.GroupDutyRegistExcel;
//import org.egglog.api.group.model.dto.OutputSpec.GroupInvitationOutputSpec;
//import org.egglog.api.group.model.dto.OutputSpec.GroupOutputSpec;
//import org.egglog.api.group.model.dto.form.*;
//import org.egglog.api.group.model.service.GroupService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/v1/group")
//@RequiredArgsConstructor
//@Slf4j
//public class GroupController {
//    private final GroupService groupService;
//
//    @PostMapping("/invitation")
//    ResponseEntity generateInvitationCode(@RequestBody GroupInvitationForm groupInvitationForm){
//        log.debug(groupInvitationForm.toString());
//        GroupInvitationOutputSpec invitationCode= GroupInvitationOutputSpec.builder()
//                .invitationCode(groupService.generateInvitation(groupInvitationForm))
//                .build();
//        return ResponseEntity.ok().body(MessageUtils.success(invitationCode));
//    }
//    @PostMapping("/invitation/accept")
//    ResponseEntity acceptInvitation(@RequestBody InvitationAcceptForm invitationAcceptForm){
//        log.debug(invitationAcceptForm.toString());
//        Long userId=2L;
//        InvitationAcceptOutputSpec groupName=groupService.acceptInvitation(invitationAcceptForm.getInvitationCode(),userId);
//        return ResponseEntity.ok().body(MessageUtils.success(groupName));
//    }
//
//    @GetMapping("/retrieve/{groupId}")
//    ResponseEntity findGroup(@PathVariable Long groupId){
//        Long userId=1L;
//        GroupRetrieveForm groupRetrieveForm= GroupRetrieveForm.builder()
//                .groupId(groupId)
//                .userId(userId)
//                .build();
//        log.debug(groupRetrieveForm.toString());
//        GroupOutputSpec group=groupService.findGroup(groupRetrieveForm,userId);
//        return ResponseEntity.ok().body(
//                MessageUtils.success(group));
//    }
//    @GetMapping("/list")
//    ResponseEntity findGroupList(){
//        Long userId=1L;
//        List<GroupOutputSpec> groupList=groupService.findGroupListByUserId(userId);
//        return ResponseEntity.ok().body(MessageUtils.success(groupList));
//    }
//    @PostMapping("/regist")
//    ResponseEntity regist(@RequestBody GroupRegistForm groupRegistForm) {
////    ResponseEntity<Message> register(@RequestBody GroupRegistForm groupRegistForm,
////                                     @AuthenticationPrincipal User user) {
//        log.debug(groupRegistForm.toString());
//        Long userId=1L;
//        groupService.registGroup(groupRegistForm,userId);
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
//
//    @DeleteMapping("/delete/{groupId}")
//    ResponseEntity<MessageUtils> delete(@PathVariable Long groupId){
//        log.debug(groupId.toString());
//        Long userId=1L;
//        groupService.deleteGroup(groupId,userId);
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
//
//    @PatchMapping("/modify/member")
//    ResponseEntity modifyMember(@RequestBody GroupModifyMemberForm groupModifyMemberForm){
//        log.debug(groupModifyMemberForm.toString());
//        Long userId=1l;
//        groupService.modifyGroupMember(groupModifyMemberForm,userId);
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
//    @DeleteMapping("/delete/member")
//    ResponseEntity deleteMember(@RequestBody GroupMemberDeleteForm groupMemberDeleteForm){
//        log.debug(groupMemberDeleteForm.toString());
//        Long userId=1l;
//        groupService.deleteGroupMember(groupMemberDeleteForm,userId);
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
//
//
//    @PatchMapping("/modify")
//    ResponseEntity modify(@RequestBody GroupModifyForm groupModifyForm){
//        log.debug(groupModifyForm.toString());
//        Long userId=1l;
//        groupService.modifyGroup(groupModifyForm,userId);
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
//
//    @PostMapping("/regist/duty")
//    ResponseEntity registDuty(@RequestBody GroupDutyRegistRequestForm groupDutyRegistForm){
//        log.debug(groupDutyRegistForm.toString());
//        Long userId=1l;
//        GroupDutyRegistExcel groupDutyRegistExcel=groupService.registDuty(groupDutyRegistForm,userId);
//        return ResponseEntity.ok().body(
//                MessageUtils.success(groupDutyRegistExcel)
//        );
//    }
//}
