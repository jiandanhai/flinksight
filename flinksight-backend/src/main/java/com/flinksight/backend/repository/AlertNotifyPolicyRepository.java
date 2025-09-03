package com.flinksight.backend.repository;
import com.flinksight.backend.domain.AlertNotifyPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlertNotifyPolicyRepository extends JpaRepository<AlertNotifyPolicy, Long> {
  Optional<AlertNotifyPolicy> findByTenantIdAndName(Long tenantId, String name);
}
