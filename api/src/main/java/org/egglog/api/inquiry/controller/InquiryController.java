package org.egglog.api.inquiry.controller;

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

@RestController
@RequestMapping("/v1/inquiries")
@RequiredArgsConstructor
@Slf4j
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping("")
    public ResponseEntity generateInquiry(InquiryRegistForm registForm, @AuthenticationPrincipal User user){
        inquiryService.generateInquiry(registForm,user);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.CREATE));
    }

    @GetMapping("")
    public ResponseEntity getInquiryList(@RequestParam("noted") String noted, @RequestParam("index") Long index){
        List<InquiryDto> inquiryList = inquiryService.getInquiryList(noted,index);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    @PatchMapping("/status/{inquiry_id}")
    public ResponseEntity updateInquiry(@PathVariable("inquiry_id") Long inquiryId){
        inquiryService.updateInquiry(inquiryId);
        return ResponseEntity.ok().body(MessageUtils.success(SuccessType.NO_CONTENT));
    }

    @GetMapping("/{inquiry_id}")
    public ResponseEntity getInquiry(@PathVariable("inquiry_id") Long inquiryId){
        InquiryDto inquiryDto = inquiryService.getInquiry(inquiryId);
        return ResponseEntity.ok().body(MessageUtils.success(inquiryDto));
    }

}
