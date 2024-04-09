package org.egglog.api.user.model.dto.params;

import com.nursetest.app.user.model.dto.request.EditUserRequest;
import com.nursetest.app.user.model.dto.request.RefreshPasswordRequest;
import com.nursetest.app.user.model.entity.enums.AuthProvider;
import com.nursetest.app.user.model.entity.enums.UserRole;
import com.nursetest.app.user.model.entity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ModifyParam {
    private Long id;
    private String email;
    private String password;
    private String userName;
    private AuthProvider provider;
    private String hospitalName;
    private String empNo;
    private String profileImgUrl;
    private UserRole role;
    private UserStatus status;


    public static ModifyParam requestToParam(Long userId, EditUserRequest request){
        ModifyParam result = new ModifyParam();
        result.setId(userId);
        if (request.getEmpNo()!=null) result.setEmpNo(request.getEmpNo());
        if (request.getHospitalName()!=null) result.setHospitalName(request.getHospitalName());
        if (request.getUserName()!=null) result.setUserName(request.getUserName());
        if (request.getProfileImgUrl()!=null) result.setProfileImgUrl(request.getProfileImgUrl());
        return result;
    }

    public static ModifyParam requestToParam(Long userId, RefreshPasswordRequest request){
        return ModifyParam.builder()
                .id(userId)
                .password(request.getNewPassword())
                .build();
    }


}
