package org.egglog.api.user.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindEmailRequest {
    private String user_name;
    private String hospital_name;
    private String emp_no;
}
