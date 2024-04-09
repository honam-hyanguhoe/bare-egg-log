package org.egglog.api.user.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindUserRequest {
    private boolean id;
    private boolean email;
    private boolean user_name;
    private boolean provider;
    private boolean hospital_name;
    private boolean emp_no;
    private boolean profile_img_url;
    private boolean role;
    private boolean status;
    private boolean created_at;
    private boolean updated_at;
}
