package org.egglog.api.group.model.repository;

import com.nursetest.app.group.model.entity.InvitationCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupInvitationRepository extends CrudRepository<InvitationCode, String> {
    Optional<InvitationCode> findInvitationCodeByGroupId(Long groupId);
}
