package org.egglog.api.board.search.exception;

import lombok.Getter;
import org.egglog.api.board.exception.BoardErrorCode;
import org.egglog.utility.exception.BaseException;

@Getter
public class SearchException extends BaseException {

    public SearchException(SearchErrorCode errorCode) {
        super(errorCode);
    }
}
