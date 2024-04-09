package org.egglog.api.groupboard.model.mapper;

import com.nursetest.app.groupboard.model.dto.response.CommentListOutputSpec;
import com.nursetest.app.groupboard.model.dto.response.RecommentOutputSpec;
import com.nursetest.app.groupboard.model.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<CommentListOutputSpec> getCommentList(Long boardId);
    List<RecommentOutputSpec> getRecommentsByCommentId(Long commentId);
    void registComment(Comment comment);
    void modifyComment(Comment comment);
    void deleteComment(Long commentId, Long userId);
}
