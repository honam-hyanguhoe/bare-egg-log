package org.egglog.api.group.exception;

import lombok.Getter;
import org.egglog.utility.exception.BaseException;

@Getter
public class GroupException extends BaseException {
    public GroupException(GroupErrorCode errorCode){
        super(errorCode);
    }
}
