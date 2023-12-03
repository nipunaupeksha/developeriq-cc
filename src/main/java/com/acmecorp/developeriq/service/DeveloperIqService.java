package com.acmecorp.developeriq.service;

import com.acmecorp.developeriq.dto.CommitDTO;
import com.acmecorp.developeriq.dto.IssueDTO;
import com.acmecorp.developeriq.dto.PRDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeveloperIqService {
    List<CommitDTO> getDeveloperCommitsCount(String developerId);

    IssueDTO getDeveloperIssueCount(String developerId);

    PRDTO getDeveloperPRCount(String developerId);
}
