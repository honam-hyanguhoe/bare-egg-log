package org.egglog.api.group.model.service;

import com.nursetest.app.fcm.model.service.FcmService;
import com.nursetest.app.global.util.MonthUtils;
import com.nursetest.app.group.exception.GroupErrorCode;
import com.nursetest.app.group.exception.GroupException;
import com.nursetest.app.group.model.dto.OutputSpec.*;
import com.nursetest.app.group.model.dto.form.*;
import com.nursetest.app.group.model.entity.Group;
import com.nursetest.app.group.model.entity.InvitationCode;
import com.nursetest.app.group.model.mapper.GroupMapper;
import com.nursetest.app.group.model.repository.GroupInvitationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService{
    private final GroupMapper groupMapper;
    private final FcmService fcmService;
    private final GroupInvitationRepository groupInvitationRepository;

    /**
     * 그룹을 등록하는 메서드<br/>
     * - groupRegistForm을 기반(그룹명, 그룹 프로필 이미지 지정)으로 함<br/>
     * [로직]<br/>
     *     _ groupRegistForm을 Group 객체로 변경 -> 테이블에 insert 후 생성된 group id를 받아오기 위함<br/>
     *     - userId를 통해 GroupMember 설정 : 그룹의 생성자이기 때문에 관리자 권한을 가진다.<br/>
     *     - 생성된 group member를 group에 등록한다.
     * @param groupRegistForm
     * @param userId
     */
    @Override
    @Transactional
    public void registGroup(GroupRegistForm groupRegistForm, Long userId) {
        Group group=groupRegistForm.toEntity();
        //team 테이블에 insert 한 후 group id가 갱신됨
        try {
            groupMapper.registGroup(group);
            GroupMember groupMember=GroupMember
                    .builder()
                    .groupId(group.getGroupId())
                    .groupAdmin(true)
                    .userId(userId)
                    .build();
            groupMapper.registGroupMember(groupMember);
            fcmService.subscribeMyTokens(userId, groupMember.getGroupId());
        }catch (PersistenceException e) {
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        } catch (DataAccessException e) {
            throw new GroupException(GroupErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GroupException(GroupErrorCode.UNKNOWN_ERROR);
        }

    }

    /**
     * 그룹 삭제 메서드
     * [로직]<br/>
     * - 삭제 권한이 있는 사용자인지 판별해 동작을 수행한다.
     * @param groupId
     * @param userId
     */
    @Override
    public void deleteGroup(Long groupId,Long userId) {
        //삭제 권한이 있는 사용자인지 판별
        hasAuthority(userId,groupId);
        try{
            groupMapper.deleteGroup(groupId);
        }catch (PersistenceException e) {
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        } catch (DataAccessException e) {
            throw new GroupException(GroupErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            throw new GroupException(GroupErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 그룹 멤버 권한 수정 메서드<br/>
     * [로직]<br/>
     * - 수정 요청 사용자 권한 확인 후 수정한다.
     * @param groupModifyMemberForm
     * @param userId
     */
    @Override
    public void modifyGroupMember(GroupModifyMemberForm groupModifyMemberForm, Long userId) {
        //권한이 있는 사용자인지 판별
        hasAuthority(userId, groupModifyMemberForm.getGroupId());
        try{
            groupMapper.modifyGroupMember(groupModifyMemberForm);
        }catch (PersistenceException e) {
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new GroupException(GroupErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            throw new GroupException(GroupErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 그룹원 삭제<br/>
     * [로직]<br/>
     * - 삭제 권한이 있는지 확인 후 삭제
     * @param groupMemberDeleteForm
     * @param userId
     */
    @Override
    public void deleteGroupMember(GroupMemberDeleteForm groupMemberDeleteForm, Long userId) {
        //삭제 권한이 있는 사용자인지 판별
        hasAuthority(userId, groupMemberDeleteForm.getGroupId());
        try{
            groupMapper.deleteGroupMember(groupMemberDeleteForm);
        }catch (PersistenceException e) {
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        } catch (DataAccessException e) {
            throw new GroupException(GroupErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            throw new GroupException(GroupErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 그룹 수정(그룹명, 프로필 사진)<br/>
     * - 개별 수정이 가능하다.<br/>
     * [로직]<br/>
     * - 수정 권한이 있는지 확인 후 수정
     * @param groupModifyForm
     * @param userId
     */
    @Override
    public void modifyGroup(GroupModifyForm groupModifyForm, Long userId) {
        // 권한이 있는 사용자인지 판별
        hasAuthority(userId, groupModifyForm.getGroupId());

        try{
            groupMapper.updateGroup(groupModifyForm);
        }catch (PersistenceException e) {
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        } catch (DataAccessException e) {
            throw new GroupException(GroupErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            throw new GroupException(GroupErrorCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 그룹 상세 데이터 조회
     * - 접근 권한이 있는 사용자(해당 그룹의 그룹원)인 경우 정보 조회
     * @param groupRetrieveForm
     * @return
     */
    @Override
    public GroupOutputSpec findGroup(GroupRetrieveForm groupRetrieveForm, Long userId) {
        GroupOutputSpec groupOutputSpec=null;
        // 접근 권한이 있는 사용자인지 판별
        isMember(userId, groupRetrieveForm.getGroupId());
        try{
            groupOutputSpec=groupMapper.findGroup(groupRetrieveForm);
        }catch (PersistenceException e) {
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        } catch (DataAccessException e) {
            throw new GroupException(GroupErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            throw new GroupException(GroupErrorCode.UNKNOWN_ERROR);
        }
        return groupOutputSpec;
    }

    /**
     * 사용자가 소속된 모든 그룹 조회
     * @param userId
     * @return
     */
    @Override
    public List<GroupOutputSpec> findGroupListByUserId(Long userId) {
        List<GroupOutputSpec> groupOutputSpecList=null;
        try{
            groupOutputSpecList=groupMapper.findGroupListByUserId(userId);
        }catch (PersistenceException e) {
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        } catch (DataAccessException e) {
            throw new GroupException(GroupErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            throw new GroupException(GroupErrorCode.UNKNOWN_ERROR);
        }
        return groupOutputSpecList;
    }

    /**
     * 초대 코드 생성 메서드<br/>
     * - 유효한 코드가 아직 존재한다면 기존 코드를 반환<br/>
     * - 유효한 코드가 존재하지 않는다면 새로운 코드 생성해 반환<br/>
     * - 이미 등록된 사용자는 등록하지않는다.
     * @param groupInvitationForm
     * @return
     */
    @Override
    public String generateInvitation(GroupInvitationForm groupInvitationForm) {
        //save
        Optional<InvitationCode> existInvitation=groupInvitationRepository.findInvitationCodeByGroupId(groupInvitationForm.getGroupId());
        if(existInvitation.isPresent()){
            return existInvitation.get().getCode();
        }
        //groupInvitationForm 기반으로 새로운 invitation code 생성
        InvitationCode newInvitation=InvitationCode.create(groupInvitationForm.getGroupId());
        log.debug(newInvitation.toString());
        InvitationCode invitationCode=groupInvitationRepository.save(newInvitation);
        return newInvitation.getCode();
    }

    /**
     * 초대 수락 메서드<br/>
     * - 해당 코드가 존재하는지 체크한다.<br/>
     * - 코드 정보를 바탕으로 그룹원을 그룹에 등록<br/>
     * @param invitationCode
     * @param userId
     */
    @Override
    @Transactional
    public InvitationAcceptOutputSpec acceptInvitation(String invitationCode, Long userId) {
        Optional<InvitationCode> invitation=groupInvitationRepository.findById(invitationCode);
        if(invitation.isPresent()){
            //그룹원 등록 폼을 기반으로 그룹원 생성
            GroupMember groupMember=GroupMember.builder()
                    .userId(userId)
                    .groupId(invitation.get().getGroupId())
                    .groupAdmin(false) //일반 그룹원
                    .build();
            InvitationAcceptOutputSpec groupName=null;
            try {
                groupMapper.registGroupMember(groupMember);
                fcmService.subscribeMyTokens(groupMember.getUserId(), groupMember.getGroupId());
                groupName=InvitationAcceptOutputSpec.builder()
                        .groupName(groupMapper.findGroupById(groupMember.getGroupId()).getGroupName())
                        .build();
            }catch (PersistenceException e) {
                throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
            }catch (DuplicateKeyException e) {
                // 중복 키 예외 처리
                throw new GroupException(GroupErrorCode.DUPLICATED_MEMBER);
            }  catch (DataAccessException e) {
                throw new GroupException(GroupErrorCode.DATABASE_CONNECTION_FAILED);
            } catch (Exception e) {
                throw new GroupException(GroupErrorCode.UNKNOWN_ERROR);
            }
            return groupName;
        }else{
            throw new GroupException(GroupErrorCode.NOT_FOUND_INVITATION);
        }
    }

    @Override
    public GroupDutyRegistExcel registDuty(GroupDutyRegistRequestForm groupDutyRegistForm, Long userId) {
        //사용자에게 그룹에 소속되었는지 체크
        //소속되어있다면
        GroupDutyRegistExcel groupDutyRegistExcel=null;
        // 접근 권한이 있는 사용자인지 판별
        isMember(userId, groupDutyRegistForm.getGroupId());
        try{
            List<SimpleGroupUser> groupUserList=groupMapper.getGroupUserInfo(groupDutyRegistForm.getGroupId());
            MonthUtils.Month month= MonthUtils.getDates(groupDutyRegistForm.getYear(), groupDutyRegistForm.getMonth());
            groupDutyRegistExcel=GroupDutyRegistExcel.builder()
                    .monthLength(month.dateLength)
                    .title(month.getMonth()+"월 근무표")
                    .userList(groupUserList)
                    .build();

        }catch (PersistenceException e) {
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        } catch (DataAccessException e) {
            throw new GroupException(GroupErrorCode.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            throw new GroupException(GroupErrorCode.UNKNOWN_ERROR);
        }
        return groupDutyRegistExcel;
    }

    /**
     * 관리자 권한을 가지고 있는지 체크하는 유틸리티 메서드
     * @param userId
     * @param groupId
     */
    public void hasAuthority(Long userId, Long groupId){

        GroupMember groupUser=groupMapper.findGroupMember(userId, groupId);
        if(groupUser==null){
            throw new GroupException(GroupErrorCode.NOT_FOUND);
        }
        if(!groupUser.getGroupAdmin()){
            System.out.println("authority : "+groupUser.getGroupAdmin());
            throw new GroupException(GroupErrorCode.GROUP_ROLE_NOT_MATCH);
        }
    }

    /**
     * 그룹에 소속되어있는지 체크하는 유틸리티 메서드
     * @param userId
     * @param groupId
     */
    public void isMember(Long userId, Long groupId){
        GroupMember groupUser=groupMapper.findGroupMember(userId, groupId);
        if(groupUser==null){
            throw new GroupException(GroupErrorCode.GROUP_ROLE_NOT_MATCH);
        }
    }
}

