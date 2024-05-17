package org.egglog.api.group.repository.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.group.model.dto.request.CustomWorkTag;
import org.egglog.api.group.model.dto.request.DutyFormat;
import org.egglog.api.group.model.dto.request.GroupDutyData;
import org.egglog.api.group.model.dto.request.GroupDutySaveFormat;
import org.egglog.api.group.model.dto.response.GroupDutyDataDto;
import org.egglog.api.group.model.dto.response.GroupDutyDto;
import org.egglog.api.group.model.dto.response.Index;
import org.egglog.api.worktype.model.entity.WorkTag;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
            if(dutyFormat.getEmployeeId()!=null){
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

            ApiFuture<WriteResult> apiFuture = firestore
                    .collection("duty")
                    .document(String.valueOf(groupId))
                    .collection(groupDutyData.getDate())
                    .document(String.valueOf(count))
                    .set(groupDutySaveFormat);

            // 쿼리 실행 결과를 기다림
            WriteResult writeResult = apiFuture.get();
            log.info("Document successfully written at: {}", writeResult.getUpdateTime());

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
    private void saveGroupWorkTag(Long groupId, CustomWorkTag customWorkTag) {
        //데이터 삽입 쿼리
        ApiFuture<WriteResult> apiFuture = firestore
                .collection("work-tag")
                .document(String.valueOf(groupId))
                .set(customWorkTag);
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
        }
        return null;
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
                log.debug(doc.toString());
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
}
