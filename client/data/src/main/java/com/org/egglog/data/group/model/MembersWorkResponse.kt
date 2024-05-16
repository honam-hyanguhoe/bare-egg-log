//package com.org.egglog.data.group.model
//
//import com.org.egglog.data.main.model.WeeklyWorkReponse
//import com.org.egglog.data.main.model.WorkDTO
//import com.org.egglog.data.main.model.toDomainModel
//import com.org.egglog.domain.group.model.GroupMembersWork
//import com.org.egglog.domain.main.model.WeeklyWork
//import kotlinx.serialization.Serializable
//
//@Serializable
//data class MembersWorkResponse(
//    val workList: List<WorkDTO>,
//)
//
//fun MembersWorkResponse.toDomainModel(): GroupMembersWork {
//    return GroupMembersWork(
//        workList = workList.map { it.toDomainModel() },
//    )
//}
//
//val temp =
//    [
//        {
//            "workId": 9101,
//            "workDate": "2024-05-01",
//            "workType": {
//            "workTypeId": 91,
//            "title": "NIGHT",
//            "color": "#E55555",
//            "workTypeImgUrl": "https://firebasestorage.googleapis.com/v0/b/egglog-422207.appspot.com/o/honam%2Feggs%2FNIGHT.png?alt=media&token=1181cfa8-1e19-402b-a977-eb8d096a8576",
//            "workTag": "NIGHT",
//            "startTime": "02:00:00",
//            "workTime": "08:00:00"
//        }
//        },
//        {
//            "workId": 9102,
//            "workDate": "2024-05-02",
//            "workType": {
//            "workTypeId": 91,
//            "title": "NIGHT",
//            "color": "#E55555",
//            "workTypeImgUrl": "https://firebasestorage.googleapis.com/v0/b/egglog-422207.appspot.com/o/honam%2Feggs%2FNIGHT.png?alt=media&token=1181cfa8-1e19-402b-a977-eb8d096a8576",
//            "workTag": "NIGHT",
//            "startTime": "02:00:00",
//            "workTime": "08:00:00"
//        }
//        },
//        {
//            "workId": 9103,
//            "workDate": "2024-05-03",
//            "workType": {
//            "workTypeId": 89,
//            "title": "DAY",
//            "color": "#18C5B5",
//            "workTypeImgUrl": "https://firebasestorage.googleapis.com/v0/b/egglog-422207.appspot.com/o/honam%2Feggs%2FDAY.png?alt=media&token=34852f61-9513-41ab-a733-f01c3014206a",
//            "workTag": "DAY",
//            "startTime": "06:00:00",
//            "workTime": "08:00:00"
//        }
//        },
//    ]