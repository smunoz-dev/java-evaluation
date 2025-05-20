package com.evaluation.register.register_api.repository;

import com.evaluation.register.register_api.model.entities.UserAudit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuditRepository extends CrudRepository<UserAudit, Long> {
}