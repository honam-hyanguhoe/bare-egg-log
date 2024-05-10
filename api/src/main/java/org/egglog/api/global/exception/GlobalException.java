package org.egglog.api.global.exception;

import lombok.Getter;
import org.egglog.utility.exception.BaseException;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.global.exception
 * fileName      : GlobalException
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-10|김형민|최초 생성|
 */
@Getter
public class GlobalException  extends BaseException {
    public GlobalException(GlobalErrorCode errorCode){
        super(errorCode);
    }

}
