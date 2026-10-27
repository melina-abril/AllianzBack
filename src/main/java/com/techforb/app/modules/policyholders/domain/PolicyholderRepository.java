package com.techforb.app.modules.policyholders.domain;

import java.util.List;
import java.util.Optional;

public interface PolicyholderRepository {
    List<Policyholder> findAll();
    Optional<Policyholder> findById(String id);
    Policyholder save(Policyholder policyholder);
}
