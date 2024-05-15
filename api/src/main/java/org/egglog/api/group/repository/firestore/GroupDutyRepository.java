package org.egglog.api.group.repository.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import org.egglog.api.group.model.dto.request.GroupDutyData;

import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
public class GroupDutyRepository {
    private final Firestore firestore;

    public void saveDuty(Long groupId, GroupDutyData groupDutyData) throws ExecutionException, InterruptedException {
        CollectionReference collection = firestore.collection("String.valueOf(groupId)");
        Query query = collection.whereEqualTo(String.valueOf(groupId), groupDutyData.getDate());
        //해당 그룹에 기존 데이터가 있는지 확인
        AggregateQuerySnapshot snapshot = query.count().get().get();
        System.out.println("Count: " + snapshot.getCount());

        //데이터 삽입 쿼리
        ApiFuture<WriteResult> apiFuture = firestore
                .collection(collectionName)
                .document(groupDutyData.getDate())
                .set(groupDutyData.getDutyList());
    }
}
