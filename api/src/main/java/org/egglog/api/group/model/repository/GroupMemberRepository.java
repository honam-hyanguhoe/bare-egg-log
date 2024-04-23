package org.egglog.api.group.model.repository;

import org.egglog.api.group.model.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember,Long>,GroupMemberCustomQuery {
}
