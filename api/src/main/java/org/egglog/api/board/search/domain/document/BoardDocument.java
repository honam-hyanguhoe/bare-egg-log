package org.egglog.api.board.search.domain.document;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "title")
    private String title;   //제목

    @Field(type = FieldType.Text, name = "content")
    private String content;     //내용

    private Long groupId;    //그룹 아이디

    private Long hospitalId;  //병원 아이디

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdAt;    //생성일

    public static BoardDocument from(Board board) {
        return BoardDocument.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .hospitalId(board.getHospital().getId())
                .groupId(board.getGroup().getGroupId())
                .createdAt(board.getCreatedAt())
                .build();
    }

}
