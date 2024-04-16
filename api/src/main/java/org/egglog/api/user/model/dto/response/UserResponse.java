//package org.egglog.api.user.model.dto.response;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.PropertyNamingStrategies;
//import com.fasterxml.jackson.databind.annotation.JsonNaming;
//import com.nursetest.app.user.model.dto.request.FindUserRequest;
//import com.nursetest.app.user.model.entity.enums.AuthProvider;
//import com.nursetest.app.user.model.entity.enums.UserRole;
//import com.nursetest.app.user.model.entity.enums.UserStatus;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//
//import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
//
//@Builder
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@JsonInclude(NON_NULL)
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//public class UserResponse {
//    private Long id;
//    private String email;
//    private String password;
//    private String userName;
//    private AuthProvider provider;
//    private String hospitalName;
//    private String empNo;
//    private String profileImgUrl;
//    private UserRole role;
//    private UserStatus status;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//
//    public static UserResponse of(UserDto user, FindUserRequest request) {
//        UserResponse responseUser = new UserResponse();
//        if (request.isId()) responseUser.setId(user.getId());
//        if (request.isEmail()) responseUser.setEmail(user.getEmail());
//        if (request.isRole()) responseUser.setRole(user.getRole());
//        if (request.isUser_name()) responseUser.setUserName(user.getRealUserName());
//        if (request.isEmp_no()) responseUser.setEmpNo(user.getEmpNo());
//        if (request.isHospital_name()) responseUser.setHospitalName(user.getHospitalName());
//        if (request.isProvider()) responseUser.setProvider(user.getProvider());
//        if (request.isStatus()) responseUser.setStatus(user.getStatus());
//        if (request.isProfile_img_url()) responseUser.setProfileImgUrl(user.getProfileImgUrl());
//        if (request.isCreated_at()) responseUser.setCreatedAt(user.getCreatedAt());
//        if (request.isUpdated_at()) responseUser.setUpdatedAt(user.getUpdatedAt());
//        return responseUser;
//    }
//    public static UserResponse of(String email) {
//        return UserResponse.builder()
//                .email(email)
//                .build();
//    }
//
//    public static UserResponse of(OAuth2User oAuth2User){
//        Map<String, Object> map = oAuth2User.getAttributes();
//        return UserResponse.builder()
//                .email((String) map.get("email"))
//                .userName((String) map.get("name"))
//                .profileImgUrl((String) map.get("picture"))
//                .provider((AuthProvider) map.get("provider"))
//                .build();
//    }
//
//}