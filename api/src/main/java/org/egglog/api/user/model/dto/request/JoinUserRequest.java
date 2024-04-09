package org.egglog.api.user.model.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class JoinUserRequest {
    private String email;
    private String password;
}
