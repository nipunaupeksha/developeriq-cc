package com.acmecorp.developeriq.repository;

import com.acmecorp.developeriq.model.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface RepositoryRepository extends JpaRepository<Repository, Long> {
}
