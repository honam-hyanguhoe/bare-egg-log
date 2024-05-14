package org.egglog.api.testdata;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.egglog.api.group.model.dto.request.GroupForm;
import org.egglog.api.group.model.dto.request.InvitationAcceptForm;
import org.egglog.api.security.exception.AuthException;
import org.egglog.api.security.exception.JwtErrorCode;
import org.egglog.api.security.exception.JwtException;
import org.egglog.api.testdata.dto.request.TestLoginRequest;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.model.entity.enums.AuthProvider;
import org.egglog.api.user.model.service.UserService;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login/{provider}")
    public ResponseEntity<MessageUtils> testLogin(
            @RequestBody @Valid TestLoginRequest request,
            @PathVariable AuthProvider provider
            ){
        return ResponseEntity.ok().body(MessageUtils.success(testService.testLogin(request, provider)));
    }

    @PostMapping("/create/user")
    public ResponseEntity<MessageUtils> createUserMockData(){
        return ResponseEntity.ok().body(MessageUtils.success(testService.createUserMockData()));
    }

    @DeleteMapping("/delete/user")
    public ResponseEntity<MessageUtils> deleteUserMockData(){
        testService.deleteUserMockData();
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.DELETE));
    }

    @PostMapping("/create/work")
    public ResponseEntity<MessageUtils> creatWorkMockData(){
        testService.makeWorkList();
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    @PostMapping("/create/work-user")
    public ResponseEntity<MessageUtils> creatWorkMockData(
            @AuthenticationPrincipal User loginUser
            ){
        if (loginUser==null) throw new JwtException(JwtErrorCode.NO_TOKEN_FOR_TEST);
        testService.makeWorkListForUser(loginUser);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    @PostMapping("/need/group-users")
    public ResponseEntity<MessageUtils> createGroupUsers(
            @RequestBody @Valid InvitationAcceptForm request
    ){
        testService.giveGroupUsers(request);
        return ResponseEntity.ok().body(MessageUtils.success((SuccessType.CREATE)));
    }
}
