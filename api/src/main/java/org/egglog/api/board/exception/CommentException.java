package org.egglog.api.board.exception;

import lombok.Getter;
import org.egglog.utility.exception.BaseException;

@Getter
public class CommentException extends BaseException {

    public CommentException(CommentErrorCode errorCode) {
        super(errorCode);
    }
}
