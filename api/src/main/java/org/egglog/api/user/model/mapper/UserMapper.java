package org.egglog.api.user.model.mapper;

import com.nursetest.app.user.model.dto.params.FindParam;
import com.nursetest.app.user.model.dto.params.ModifyParam;
import com.nursetest.app.user.model.dto.request.JoinUserRequest;
import com.nursetest.app.user.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    void join(JoinUserRequest joinUser);
    Optional<User> find(FindParam param);
    void modify(ModifyParam modifyParam);
    void delete(String email);
}
