package org.egglog.api.inquiry.exception;

import org.egglog.api.group.exception.GroupErrorCode;
import org.egglog.utility.exception.BaseException;

public class InquiryException extends BaseException {
    public InquiryException(InquiryErrorCode errorCode){
        super(errorCode);
    }
}
