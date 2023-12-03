package com.acmecorp.developeriq.repository;

import com.acmecorp.developeriq.model.PR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PRRepository extends JpaRepository<PR, String> {
}
