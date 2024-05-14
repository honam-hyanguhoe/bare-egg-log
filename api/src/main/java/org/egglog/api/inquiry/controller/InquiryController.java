package org.egglog.api.inquiry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.inquiry.model.dto.request.InquiryRegistForm;
import org.egglog.api.inquiry.model.dto.response.InquiryDto;
import org.egglog.api.inquiry.model.service.InquiryService;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.inquiry.controller
 * fileName      : InquiryController
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-01|김다희|최초 생성|
 */
@RestController
@RequestMapping("/v1/inquiries")
@RequiredArgsConstructor
@Slf4j
public class InquiryController {
    private final InquiryService inquiryService;

    /**
     * 문의 생성
     * @param user 로그인한 유저(JWT 토큰)
     * @param registForm 이메일, 제목, 본문
     * @return
     * @author 김다희
     */
    @PostMapping("")
    public ResponseEntity<MessageUtils> generateInquiry(
            @RequestBody @Valid InquiryRegistForm registForm,
            @AuthenticationPrincipal User user){
        inquiryService.generateInquiry(registForm,user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    /**
     * 문의 리스트 조회
     * @param noted
     * @param index
     * @return
     * @author 김다희
     */
    @GetMapping("")
    public ResponseEntity<MessageUtils<List<InquiryDto>>> getInquiryList(@RequestParam("noted") String noted, @RequestParam("index") Long index){
        List<InquiryDto> inquiryList = inquiryService.getInquiryList(noted,index);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    /**
     * 문의 수정
     * @param inquiryId 업데이트할 조회 ID
     * @return
     * @author 김다희
     */
    @PatchMapping("/status/{inquiry_id}")
    public ResponseEntity<MessageUtils> updateInquiry(@PathVariable("inquiry_id") Long inquiryId){
        inquiryService.updateInquiry(inquiryId);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.NO_CONTENT));
    }

    /**
     * 문의 단건 조회
     * @param inquiryId 조회할 문의 ID
     * @return
     * @author 김다희
     */
    @GetMapping("/{inquiry_id}")
    public ResponseEntity<MessageUtils<InquiryDto>> getInquiry(@PathVariable("inquiry_id") Long inquiryId){
        InquiryDto inquiryDto = inquiryService.getInquiry(inquiryId);
        return ResponseEntity.ok().body(MessageUtils.success(inquiryDto));
    }

}
