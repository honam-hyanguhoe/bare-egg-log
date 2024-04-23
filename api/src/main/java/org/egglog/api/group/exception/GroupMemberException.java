package org.egglog.api.group.exception;


import lombok.Getter;
import org.egglog.utility.exception.BaseException;

@Getter
public class GroupMemberException extends BaseException {
    public GroupMemberException(GroupMemberErrorCode errorCode){
        super(errorCode);
    }
}
