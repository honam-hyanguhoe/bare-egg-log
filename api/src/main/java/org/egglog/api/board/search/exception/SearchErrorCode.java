package org.egglog.api.board.search.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egglog.utility.exception.ErrorFormat;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SearchErrorCode implements ErrorFormat {

    NO_EXIST_SEARCH("검색결과가 없습니다.", HttpStatus.BAD_REQUEST)

    ;

    private final String message;
    private HttpStatus httpStatus;
}
