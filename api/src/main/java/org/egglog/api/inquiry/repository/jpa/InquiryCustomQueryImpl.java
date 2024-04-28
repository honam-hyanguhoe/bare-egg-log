package org.egglog.api.inquiry.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.inquiry.model.dto.response.InquiryDto;
import org.egglog.api.inquiry.model.entity.Inquiry;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.egglog.api.inquiry.model.entity.QInquiry.inquiry;

@Repository
@RequiredArgsConstructor
public class InquiryCustomQueryImpl implements InquiryCustomQuery {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<InquiryDto>> searchInquiryList(String noted, Long index) {
        List<Inquiry> results = null;
        if (noted.equals("all")) {
            if (index == -1) {
                results = jpaQueryFactory
                        .selectFrom(inquiry)
                        .limit(15)
                        .fetch();
            } else {
                results = jpaQueryFactory
                        .selectFrom(inquiry)
                        .where(inquiry.id.lt(index))
                        .limit(15)
                        .fetch();
            }
        } else {
            if (index == -1) {
                results = jpaQueryFactory
                        .selectFrom(inquiry)
                        .where(inquiry.isNoted.eq(false))
                        .limit(15)
                        .fetch();
            } else {
                results = jpaQueryFactory
                        .selectFrom(inquiry)
                        .where(inquiry.isNoted.eq(false).and(inquiry.id.lt(index)))
                        .limit(15)
                        .fetch();
            }
        }
        List<InquiryDto> inquiryList = results.stream()
                .map(inquiry -> inquiry.toDto()).collect(Collectors.toList());

        return Optional.ofNullable(inquiryList.isEmpty() ? null : inquiryList);
    }

    @Override
    public void setNotedTrue(Long inquiryId){
        Long result = jpaQueryFactory
                .update(inquiry)
                .set(inquiry.isNoted,true)
                .where(inquiry.isNoted.eq(false).and(inquiry.id.eq(inquiryId)))
                .execute();
    }
}
