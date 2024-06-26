package org.egglog.api.group.repository.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.group.exception.GroupErrorCode;
import org.egglog.api.group.exception.GroupException;
import org.egglog.api.group.model.dto.request.CustomWorkTag;
import org.egglog.api.group.model.dto.request.DutyFormat;
import org.egglog.api.group.model.dto.request.GroupDutyData;
import org.egglog.api.group.model.dto.request.GroupDutySaveFormat;
import org.egglog.api.group.model.dto.response.GroupDutyDataDto;
import org.egglog.api.group.model.dto.response.GroupDutyDto;
import org.egglog.api.group.model.dto.response.Index;
import org.egglog.api.group.model.dto.response.UserDutyDataDto;
import org.egglog.api.worktype.model.entity.WorkTag;
import org.egglog.api.worktype.model.entity.WorkType;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
@Slf4j
public class GroupDutyRepository {
    private final Firestore firestore;


    public void saveDuty(String userName, Long groupId, GroupDutyData groupDutyData, String nowDay) throws ExecutionException, InterruptedException {
        CollectionReference collection = firestore.collection("duty")
                .document(String.valueOf(groupId))
                .collection(groupDutyData.getDate());

        log.debug("====== countQuery start ======");
        Long count = collection.count().get().get().getCount();

        CustomWorkTag customWorkTag = groupDutyData.getCustomWorkTag();
        CustomWorkTag currentCustomWorkTag = getGroupWorkTag(groupId);
        log.debug(customWorkTag.toString());
        //custom work tag 관련 작업
        //기존 값 유지
        if(currentCustomWorkTag!=null){
            if(customWorkTag.getDay().isEmpty()) customWorkTag.setDay(currentCustomWorkTag.getDay());
            if(customWorkTag.getEve().isEmpty()) customWorkTag.setEve(currentCustomWorkTag.getEve());
            if(customWorkTag.getNight().isEmpty()) customWorkTag.setNight(currentCustomWorkTag.getNight());
            if(customWorkTag.getOff().isEmpty()) customWorkTag.setOff(currentCustomWorkTag.getOff());
        }
        saveGroupWorkTag(groupId, customWorkTag);
        groupDutyData.setCustomWorkTag(customWorkTag);

        //duty list 가공
        List<DutyFormat> dutyFormatList = groupDutyData.getDutyList();
        Map<String, DutyFormat> dutyList = new HashMap<>();
        for(DutyFormat dutyFormat : dutyFormatList){
            if(dutyFormat.getEmployeeId()!=null && !dutyFormat.getEmployeeId().isEmpty()){
                dutyList.put(dutyFormat.getEmployeeId(), dutyFormat);
            }
        }


        //데이터 삽입 쿼리
        log.debug("===== save data =====");
        try {
            GroupDutySaveFormat groupDutySaveFormat = GroupDutySaveFormat.builder()
                    .day(nowDay)
                    .userName(userName)
                    .customWorkTag(groupDutyData.getCustomWorkTag())
                    .dutyList(dutyList)
                    .build();

            log.debug("path {}/{}/{}/{}","duty",groupId,groupDutyData.getDate(),count);
            String group = String.valueOf(groupId);
            String date = groupDutyData.getDate();
            String countVal = String.valueOf(count);


            log.debug("{} {}",group==null,date==null);
            if(group==null || date==null || group.isEmpty() || date.isEmpty()){
                log.warn("null 등장 ~~~~~~~~~~~~~~~~~~~~");
                throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
            }

            // groupDutySaveFormat이 null인지 체크
            if (groupDutySaveFormat == null) {
                log.warn("groupDutySaveFormat is null ~~~~~~~~~~~~~~~~~~~~");
                throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
            }
            log.debug(groupDutySaveFormat.toString());
            try {
                ApiFuture<WriteResult> apiFuture = firestore
                        .collection("duty")
                        .document(group)
                        .collection(date)
                        .document(countVal)
                        .set(groupDutySaveFormat);

                // Firestore 호출 결과 로깅
                WriteResult result = apiFuture.get();
                log.debug("Document written at: {}", result.getUpdateTime());
            } catch (Exception e) {
                log.warn("Firestore operation failed: ", e);
                e.printStackTrace();
                throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
            }
        } catch (Exception e) {
            log.error("Error saving document", e);
        }

    }

    /**
     * 그룹에 커스텀된 work tag 저장하는 메서드
     *
     * @param groupId
     * @param customWorkTag
     */
    private void saveGroupWorkTag(Long groupId, CustomWorkTag customWorkTag) throws ExecutionException, InterruptedException {
        log.debug("===== update work tag ====");
        //데이터 삽입 쿼리
        ApiFuture<WriteResult> apiFuture = firestore
                .collection("work-tag")
                .document(String.valueOf(groupId))
                .set(customWorkTag);
        log.debug(apiFuture.get().getUpdateTime().toString());
    }

    /**
     * group에 저장된 worktag를 가져옴
     *
     * @param groupId
     * @return
     */
    public CustomWorkTag getGroupWorkTag(Long groupId) throws ExecutionException, InterruptedException {
        DocumentReference workTagDoc = firestore.collection("work-tag").document(String.valueOf(groupId));
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = workTagDoc.get();
        // block on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            // convert document to POJO
            return document.toObject(CustomWorkTag.class);
        }else {
            return null;
        }
    }

    public List<GroupDutyDto> getDutyListByGroupIdList(List<Long> groupList, String date) throws ExecutionException, InterruptedException {
        log.debug("=== find group duty list ===");
        List<GroupDutyDto> groupDutyList=new ArrayList<>();
        for (Long groupId : groupList) {
            CollectionReference dutyDataDoc = firestore.collection("duty")
                    .document(String.valueOf(groupId))
                    .collection(date);
            log.debug("querying...");
            ApiFuture<QuerySnapshot> future = dutyDataDoc.get();
            log.debug("find all documents...");
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            log.debug("found!!");
            for(DocumentSnapshot doc : documents){
                log.debug("processing...");
                GroupDutySaveFormat duty = doc.toObject(GroupDutySaveFormat.class);
                if(duty != null){
                    GroupDutyDto dutyDto = GroupDutyDto.builder()
                            .date(duty.getDay())
                            .userName(duty.getUserName())
                            .index(new Index(String.valueOf(groupId),date, doc.getId()))
                            .build();
                    groupDutyList.add(dutyDto);
                }
            }
        }
        return groupDutyList;
    }

    public UserDutyDataDto getUserDutyData(Index index, String empNo){
        Map<String,String> userDutyData = new HashMap<>();
        CustomWorkTag customWorkTag = new CustomWorkTag();
        try {
            DocumentSnapshot document = firestore.collection("duty")
                    .document(index.getGroupId())
                    .collection(index.getDate())
                    .document(index.getIndex())
                    .get().get();

            if (document.exists()) {
                log.debug("=== === === === 데이터 가져오기 성공 === === === ===");
                // dutyList 맵에서 empNo를 키로 사용하여 WorkType 데이터를 가져옴
                GroupDutySaveFormat dutyData = document.toObject(GroupDutySaveFormat.class);
                Map<String,DutyFormat> dutyList = dutyData.getDutyList();
                if (dutyList.containsKey(empNo)) {
                    userDutyData = dutyList.get(empNo).getWork();
                    customWorkTag = dutyData.getCustomWorkTag();
                }else{
                    log.debug(">>>> 사용자의 데이터가 존재하지않습니다. {}",empNo);
                }
            }else {
                log.debug("해당하는 데이터가 없습니다.");
            }
            return new UserDutyDataDto(userDutyData,customWorkTag);
        } catch (InterruptedException | ExecutionException e) {
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        }
    }
}
