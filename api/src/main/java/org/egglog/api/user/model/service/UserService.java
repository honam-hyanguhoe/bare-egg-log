//package org.egglog.api.user.model.service;
//
//import com.nursetest.app.user.model.dto.request.FindEmailRequest;
//import com.nursetest.app.user.model.dto.request.RefreshPasswordRequest;
//import com.nursetest.app.user.model.dto.request.EditUserRequest;
//import com.nursetest.app.user.model.dto.request.JoinUserRequest;
//import com.nursetest.app.user.model.entity.User;
//import com.nursetest.app.user.model.entity.enums.UserStatus;
//
//public interface UserService {
//
//    void join(JoinUserRequest joinUser);
//    User find(String email);
//    User find(Long id);
//    User findEmail(FindEmailRequest request);
//    void modify(Long userId, EditUserRequest editUser);
//    void editPassword(Long userId, RefreshPasswordRequest refreshPasswordRequest);
//    void modifyUserState(Long userId, UserStatus newState);
//    void deleteUser(Long userId);
//}
