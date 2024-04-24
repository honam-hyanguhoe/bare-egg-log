package org.egglog.api.group.repository.jpa;

import org.egglog.api.group.model.dto.response.GroupPreviewDto;

import java.util.List;
import java.util.Optional;

public interface GroupCustomQuery {
    Optional<List<GroupPreviewDto>> findGroupByUserId(Long userId);
}
