package org.egglog.api.testdata;

import lombok.RequiredArgsConstructor;
import org.egglog.api.user.model.service.UserService;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.testdata
 * fileName      : TestController
 * description    : 목데이터 삭제 생성을 관리하는 컨트롤러입니다.
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-07|김형민|최초 생성|
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @PostMapping("/create/user")
    public ResponseEntity<MessageUtils> createUserMockData(){
        return ResponseEntity.ok().body(MessageUtils.success(testService.createUserMockData()));
    }

    @DeleteMapping("/delete/user")
    public ResponseEntity<MessageUtils> deleteUserMockData(){
        testService.deleteUserMockData();
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

//    @PostMapping("/work")
//    public ResponseEntity<MessageUtils> creatWorkMockData(){
//
//    }
}
