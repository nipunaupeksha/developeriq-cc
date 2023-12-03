package com.acmecorp.developeriq.repository;

import com.acmecorp.developeriq.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, String> {
}
