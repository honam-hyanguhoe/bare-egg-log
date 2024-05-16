package org.egglog.api.group.repository.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.group.model.dto.request.CustomWorkTag;
import org.egglog.api.group.model.dto.request.GroupDutyData;
import org.egglog.api.group.model.dto.request.GroupDutySaveFormat;
import org.egglog.api.group.model.dto.response.GroupDutyDataDto;
import org.egglog.api.group.model.dto.response.GroupDutyDto;
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

    public void saveDuty(Long groupId, GroupDutyData groupDutyData) throws ExecutionException, InterruptedException {
        CollectionReference collection = firestore.collection("duty")
                .document(String.valueOf(groupId))
                .collection(groupDutyData.getDate());
        log.debug("====== countQuery start ======");
        Long count = collection.count().get().get().getCount();
        CustomWorkTag customWorkTag = groupDutyData.getCustomWorkTag();
        //custom work tag 관련 작업
        if (customWorkTag.getDay().isEmpty()&&customWorkTag.getEve().isEmpty()&&customWorkTag.getNight().isEmpty()&&customWorkTag.getOff().isEmpty()) {
            log.debug("getCustomWorkTag");
            groupDutyData.setCustomWorkTag(getGroupWorkTag(groupId));
        } else {
            log.debug("saveCustomWorkTag");
            saveGroupWorkTag(groupId, groupDutyData.getCustomWorkTag());
        }
        log.debug("===== save data =====");

        //데이터 삽입 쿼리
        try {
            GroupDutySaveFormat groupDutySaveFormat = GroupDutySaveFormat.builder()
                    .day(groupDutyData.getDay())
                    .userName(groupDutyData.getUserName())
                    .customWorkTag(groupDutyData.getCustomWorkTag())
                    .dutyList(groupDutyData.toMap())
                    .build();

            log.debug(groupDutySaveFormat.toString());

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
        return new CustomWorkTag();
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
                            .day(duty.getDay())
                            .userName(duty.getUserName())
                            .dutyData(GroupDutyDataDto.builder()
                                    .dutyList(duty.getDutyList())
                                    .customWorkTag(duty.getCustomWorkTag())
                                    .groupId(String.valueOf(groupId))
                                    .date(date)
                                    .index(doc.getId())
                                    .build())
                            .build();
                    groupDutyList.add(dutyDto);
                }
            }
        }
        return groupDutyList;
    }
}
