package org.egglog.api.groupboard.exception;

import lombok.Getter;

@Getter
public class CommentException extends RuntimeException {
    private final CommentErrorCode errorCode;

    public CommentException(CommentErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
