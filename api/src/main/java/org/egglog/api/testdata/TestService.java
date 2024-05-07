package org.egglog.api.testdata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.model.dto.request.LoginRequest;
import org.egglog.api.security.model.service.AuthService;
import org.egglog.api.user.model.dto.request.JoinUserRequest;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.model.entity.enums.AuthProvider;
import org.egglog.api.user.model.service.UserService;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
import org.egglog.api.work.model.dto.request.CreateWorkListRequest;
import org.egglog.api.work.model.dto.request.CreateWorkRequest;
import org.egglog.api.work.model.service.WorkService;
import org.egglog.api.worktype.model.dto.response.WorkTypeResponse;
import org.egglog.api.worktype.model.service.WorkTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.testdata
 * fileName      : TestService
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-07|김형민|최초 생성|
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {
    private final UserService userService;
    private final UserJpaRepository userJpaRepository;
    private final WorkService workService;
    private final WorkTypeService workTypeService;
    public List<User> createUserMockData(){
        List<User> users = userJpaRepository.saveAll(makeUsers());
        Long hospitalId = 1L;
        int cnt = 0;
        for (User user : users) {
            cnt++;
            if (cnt==10||cnt==20||cnt==30) hospitalId++;
            log.debug("=====start user Join hospitalId : {} =====",hospitalId);
            userService.joinUser(user, JoinUserRequest.builder()
                    .userName(user.getName())
                    .empNo("TEST-"+cnt)
                    .fcmToken(UUID.randomUUID().toString())
                    .hospitalId(hospitalId).build());
            log.debug("user = {}", user);
            log.debug("=====end user Join=====");

        }
        return users;
    }

    @NotNull
    private static ArrayList<User> makeUsers() {
        ArrayList<User> mockUsers = new ArrayList<>();
        mockUsers.add(User.builder().name("진갑용").email("test1@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/MTUwNDQyMA.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("이범호").email("test2@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/5ub.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("박기남").email("test3@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/MTUwMDg4NQ.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("최희섭").email("test4@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/bvG8QGrnU.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("이해창").email("test5@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/MTc2NDc3OQ.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("곽도규").email("test6@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/aon.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("유지성").email("test7@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/e1e.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("유승철").email("test8@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/BZ59tdOo6.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("크로우").email("test9@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/dev.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("윤영철").email("test10@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/0Vz3XgaPM.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("임기영").email("test11@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/acb.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("윤중현").email("test12@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/11g.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("김사윤").email("test13@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/40b.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("김현수").email("test14@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/OTQ16PV9k.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("전상현").email("test15@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/bcv.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("김유신").email("test16@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/bga.png").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("양현종").email("test18@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/MTk3OTQ4Ng.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("장민기").email("test19@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/bpg.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("정해영").email("test20@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/eij.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("박건우").email("test21@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/luT74s8zp.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("주효상").email("test22@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/MTYyNjY5Ng.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("한승택").email("test23@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/TuXvdAni3.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("김태군").email("test24@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/3hd.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("한준수").email("test25@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/WAwazYKhH.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("박찬호").email("test26@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/12a.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("김선빈").email("test27@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/c7j.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("김도영").email("test28@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/4fb.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("서건창").email("test29@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/9um.jpg").provider(AuthProvider.EGGLOG).build());
        mockUsers.add(User.builder().name("최형우").email("test30@ssafy.com").profileImgUrl("https://cdn2.thecatapi.com/images/bpa.jpg").provider(AuthProvider.EGGLOG).build());
        return mockUsers;
    }

    public void deleteUserMockData(){
        userJpaRepository.deleteAll(userJpaRepository.findListByEggLogWithHospital());
    }


    public void makeWorkList(){
        List<User> listByEggLogWithHospital = userJpaRepository.findListByEggLogWithHospital();
        for (User user : listByEggLogWithHospital) {
            List<WorkTypeResponse> workTypeList = workTypeService.getWorkTypeList(user);
            workService.createWork(user, CreateWorkListRequest.builder()
                            .calendarGroupId(user.getWorkGroupId())
                            .workTypes(makeWorkTypes(workTypeList))
                            .build());
        }
    }
    private List<CreateWorkRequest> makeWorkTypes(List<WorkTypeResponse> workTypeList) {
        List<CreateWorkRequest> workRequests = new ArrayList<>();
        LocalDate startDate = LocalDate.of(2023, 5, 1); // 시작 날짜
        LocalDate endDate = LocalDate.of(2024, 5, 31); // 끝 날짜

        while (!startDate.isAfter(endDate)) {
            int randomIndex = (int) (Math.random() * workTypeList.size()); // 랜덤한 워크 타입 선택
            WorkTypeResponse workType = workTypeList.get(randomIndex);

            workRequests.add(CreateWorkRequest.builder()
                    .workDate(startDate)
                    .workTypeId(workType.getWorkTypeId())
                    .build());

            startDate = startDate.plusDays(1); // 다음 날짜로
        }

        return workRequests;
    }

}
