package com.acmecorp.developeriq.controller.impl;

import com.acmecorp.developeriq.controller.DeveloperController;
import com.acmecorp.developeriq.dto.CommitDTO;
import com.acmecorp.developeriq.dto.IssueDTO;
import com.acmecorp.developeriq.dto.PRDTO;
import com.acmecorp.developeriq.service.DeveloperIqService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"developer"})
public class DeveloperControllerImpl implements DeveloperController {
    private final DeveloperIqService developerIqService;

    @Override
    public ResponseEntity<List<CommitDTO>> getDeveloperCommitCount(String developerId) {
        return new ResponseEntity<>(developerIqService.getDeveloperCommitsCount(developerId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IssueDTO> getDeveloperCreatedIssues(String developerId) {
        return new ResponseEntity<>(developerIqService.getDeveloperIssueCount(developerId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PRDTO> getDeveloperPullRequests(String developerId) {
        return new ResponseEntity<>(developerIqService.getDeveloperPRCount(developerId), HttpStatus.OK);
    }
}
