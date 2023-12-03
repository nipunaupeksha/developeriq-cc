package com.acmecorp.developeriq.repository;

import com.acmecorp.developeriq.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitRepository extends JpaRepository<Commit, String> {
}
