package org.egglog.api.notification.exception;

import org.egglog.api.inquiry.exception.InquiryErrorCode;
import org.egglog.utility.exception.BaseException;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.notification.exception
 * fileName      : NotificationException
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-13|김형민|최초 생성|
 */
public class NotificationException extends BaseException {
    public NotificationException(NotificationErrorCode errorCode){
        super(errorCode);
    }
}
