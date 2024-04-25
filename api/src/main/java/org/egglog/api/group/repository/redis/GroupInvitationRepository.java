package org.egglog.api.group.repository.redis;


import org.egglog.api.group.model.entity.InvitationCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupInvitationRepository extends CrudRepository<InvitationCode, String> {
    Optional<InvitationCode> findInvitationCodeByGroupId(Long groupId);
    Optional<InvitationCode> findInvitationCodeById(String code);
}
