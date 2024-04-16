//package org.egglog.api.group.model.service;
//
//import com.nursetest.app.group.model.dto.OutputSpec.GroupDutyRegistExcel;
//import com.nursetest.app.group.model.dto.OutputSpec.GroupOutputSpec;
//import com.nursetest.app.group.model.dto.OutputSpec.InvitationAcceptOutputSpec;
//import com.nursetest.app.group.model.dto.form.*;
//
//import java.util.List;
//
//public interface GroupService {
//    void registGroup(GroupRegistForm groupRegistForm, Long userId);
//    void modifyGroup(GroupModifyForm groupModifyForm, Long userId);
//    void deleteGroup(Long groupId,Long userId);
//    void modifyGroupMember(GroupModifyMemberForm groupModifyMemberForm, Long userId);
//    void deleteGroupMember(GroupMemberDeleteForm groupMemberDeleteForm, Long userId);
//    GroupOutputSpec findGroup(GroupRetrieveForm groupRetrieveForm, Long userId);
//    List<GroupOutputSpec> findGroupListByUserId(Long userId);
//
//    String generateInvitation(GroupInvitationForm groupInvitationForm);
//
//    InvitationAcceptOutputSpec acceptInvitation(String invitationCode, Long userId);
//
//    GroupDutyRegistExcel registDuty(GroupDutyRegistRequestForm groupDutyRegistForm, Long userId);
//}
