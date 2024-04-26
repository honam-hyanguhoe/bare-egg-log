package org.egglog.api.inquiry.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.inquiry.exception.InquiryErrorCode;
import org.egglog.api.inquiry.exception.InquiryException;
import org.egglog.api.inquiry.model.dto.request.InquiryRegistForm;
import org.egglog.api.inquiry.model.dto.response.InquiryDto;
import org.egglog.api.inquiry.model.entity.Inquiry;
import org.egglog.api.inquiry.model.entity.InquiryType;
import org.egglog.api.inquiry.repository.jpa.InquiryRepository;
import org.egglog.api.user.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InquiryService {
    private final InquiryRepository inquiryRepository;

    @Transactional
    public void generateInquiry(InquiryRegistForm registForm, User user) {
        Inquiry inquiry = Inquiry.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .title(registForm.getTitle())
                .content(registForm.getContent())
                .inquiryType(InquiryType.DEFAULT)
                .isNoted(false)
                .build();
        try {
            inquiryRepository.save(inquiry);
        }catch (Exception e){
            throw new InquiryException(InquiryErrorCode.TRANSACTION_ERROR);
        }
    }

    @Transactional
    public void updateInquiry(Long inquiryId) {
        try {
            inquiryRepository.setNotedTrue(inquiryId);
        }catch (Exception e){
            throw new InquiryException(InquiryErrorCode.TRANSACTION_ERROR);
        }
    }

    public List<InquiryDto> getInquiryList(String noted, Long index) {
        List<InquiryDto> inquiryList = null;
        try {
            inquiryList = inquiryRepository.searchInquiryList(noted,index).orElse(null);
        }catch (Exception e){
            throw new InquiryException(InquiryErrorCode.TRANSACTION_ERROR);
        }
        return inquiryList;
    }

    public InquiryDto getInquiry(Long inquiryId) {
        Inquiry inquiry = null;
        try {
            inquiry = inquiryRepository.findById(inquiryId).orElse(null);
        }catch (Exception e){
            throw new InquiryException(InquiryErrorCode.TRANSACTION_ERROR);
        }
        return inquiry.toDto();
    }
}
