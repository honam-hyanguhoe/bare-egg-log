//package org.egglog.api.group.model.mapper;
//
//import com.nursetest.app.group.model.dto.OutputSpec.GroupMember;
//import com.nursetest.app.group.model.dto.OutputSpec.GroupOutputSpec;
//import com.nursetest.app.group.model.dto.OutputSpec.SimpleGroupUser;
//import com.nursetest.app.group.model.dto.form.GroupMemberDeleteForm;
//import com.nursetest.app.group.model.dto.form.GroupModifyForm;
//import com.nursetest.app.group.model.dto.form.GroupModifyMemberForm;
//import com.nursetest.app.group.model.dto.form.GroupRetrieveForm;
//import com.nursetest.app.group.model.entity.Group;
//import org.apache.ibatis.annotations.Mapper;
//
//import java.util.List;
//
//@Mapper
//public interface GroupMapper {
//    void registGroup(Group group);
//    void registGroupMember(GroupMember groupMember);
//    void updateGroup(GroupModifyForm groupModifyForm);
//    void deleteGroup(Long groupId);
//    void modifyGroupMember(GroupModifyMemberForm groupModifyMemberForm);
//    void deleteGroupMember(GroupMemberDeleteForm groupMemberDeleteForm);
//    GroupOutputSpec findGroup(GroupRetrieveForm groupRetrieveForm);
//    Group findGroupById(Long groupId);
//    GroupMember findGroupMember(Long userId, Long groupId);
//    List<GroupOutputSpec> findGroupListByUserId(Long userId);
//
//
//    List<SimpleGroupUser> getGroupUserInfo(Long groupId);
//}
