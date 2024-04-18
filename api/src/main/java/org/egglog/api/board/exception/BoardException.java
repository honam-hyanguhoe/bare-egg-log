package org.egglog.api.board.exception;

import lombok.Getter;
import org.egglog.utility.exception.BaseException;

@Getter
public class BoardException extends BaseException {

    public BoardException(BoardErrorCode errorCode) {
        super(errorCode);
    }
}
