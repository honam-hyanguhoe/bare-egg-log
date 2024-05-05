package org.egglog.api.search.domain.document;

import jakarta.persistence.Id;
import lombok.*;
import org.egglog.api.board.model.entity.Board;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "boards")
@Getter
@Setter
@Builder
public class BoardDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "title")
    private String title;   //제목

    @Field(type = FieldType.Text, name = "content")
    private String content;     //내용

    private String tempNickname;    //익명 닉네임

    private long viewCount;     //조회수

    private Long groupId;    //그룹 아이디

    private Long hospitalId;  //병원 아이디

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdAt;    //생성일

    public static BoardDocument from(Board board) {
        BoardDocument boardDocument = null;

        if (board.getHospital() == null && board.getGroup() == null) {
            boardDocument = BoardDocument.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .tempNickname(board.getTempNickname())
                    .createdAt(board.getCreatedAt())
                    .build();

        } else if (board.getHospital() != null) {
            boardDocument = BoardDocument.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .tempNickname(board.getTempNickname())
                    .hospitalId(board.getHospital().getId())
                    .createdAt(board.getCreatedAt())
                    .build();
        } else if (board.getGroup() != null) {
            boardDocument = BoardDocument.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .tempNickname(board.getTempNickname())
                    .groupId(board.getGroup().getId())
                    .createdAt(board.getCreatedAt())
                    .build();
        }
        return boardDocument;
    }
}
