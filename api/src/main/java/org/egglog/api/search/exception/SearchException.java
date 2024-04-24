package org.egglog.api.search.exception;

import lombok.Getter;
import org.egglog.utility.exception.BaseException;

@Getter
public class SearchException extends BaseException {

    public SearchException(SearchErrorCode errorCode) {
        super(errorCode);
    }
}
