//package org.egglog.api.group.model.entity;
//
//import lombok.Builder;
//import lombok.Data;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.redis.core.RedisHash;
//import org.springframework.data.redis.core.index.Indexed;
//
//@RedisHash(value = "invitation_code",timeToLive = 86400)
//@Data
//@Builder
//public class InvitationCode{
//    private static final int INVITE_CODE_LENGTH = 32;
//
//    @Id
//    private String code;
//    @Indexed
//    private Long groupId;
//    public static InvitationCode create(final Long groupId)  {
//        String code = RandomStringUtils.randomAlphanumeric(INVITE_CODE_LENGTH);
//        return InvitationCode.builder()
//                .code(code)
//                .groupId(groupId)
//                .build();
//    }
//
//}
