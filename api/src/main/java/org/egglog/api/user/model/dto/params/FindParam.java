//package org.egglog.api.user.model.dto.params;
//
//import com.nursetest.app.user.model.dto.request.FindEmailRequest;
//import com.nursetest.app.user.model.entity.enums.AuthProvider;
//import com.nursetest.app.user.model.entity.enums.UserRole;
//import com.nursetest.app.user.model.entity.enums.UserStatus;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Builder
//public class FindParam {
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
//
//
//    public static FindParam of(FindEmailRequest request){
//        return FindParam.builder()
//                .userName(request.getUser_name())
//                .hospitalName(request.getHospital_name())
//                .empNo(request.getEmp_no())
//                .build();
//    }
//}
