package com.acmecorp.developeriq.service.impl;

import com.acmecorp.developeriq.dto.CommitDTO;
import com.acmecorp.developeriq.dto.IssueDTO;
import com.acmecorp.developeriq.dto.PRDTO;
import com.acmecorp.developeriq.model.Commit;
import com.acmecorp.developeriq.model.Issue;
import com.acmecorp.developeriq.model.PR;
import com.acmecorp.developeriq.model.Repository;
import com.acmecorp.developeriq.repository.CommitRepository;
import com.acmecorp.developeriq.repository.IssueRepository;
import com.acmecorp.developeriq.repository.PRRepository;
import com.acmecorp.developeriq.repository.RepositoryRepository;
import com.acmecorp.developeriq.service.DeveloperIqService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.acmecorp.developeriq.constant.DeveloperIqConstants.*;

@Service
@RequiredArgsConstructor
public class DeveloperIqServiceImpl implements DeveloperIqService {
    private final RestTemplate restTemplate;
    private final RepositoryRepository repositoryRepository;
    private final CommitRepository commitRepository;
    private final IssueRepository issueRepository;
    private final PRRepository prRepository;

    @Value("${github.token}")
    private String githubToken;

    @Override
    public List<CommitDTO> getDeveloperCommitsCount(String developerId) {
        // Get user repositories
        List<Repository> repositoryList = getUserRepositories(developerId);
        repositoryRepository.saveAll(repositoryList);
        //Get commits per repository
        List<CommitDTO> commitsCount = new ArrayList<>();
        int iterator = 0;
        for (Repository repository : repositoryList) {
            List<Commit> commits = getCommitsForRepository(developerId, repository.getName());
            commits.forEach(commit -> commit.setRepositoryName(repository.getName()));
            commitRepository.saveAll(commits);
            //Change to DTO
            commitsCount.add(CommitDTO.builder()
                    .id(++iterator)
                    .author(developerId)
                    .repositoryName(repository.getName())
                    .numberOfCommits(commits.size())
                    .build());
        }
        return commitsCount;
    }

    @Override
    public IssueDTO getDeveloperIssueCount(String developerId) {
        String url = GITHUB_API + PATH_SEPARATOR + SEARCH_ENDPOINT + PATH_SEPARATOR + ISSUE_ENDPOINT +
                String.format(ISSUE_QUERY, developerId);
        HttpHeaders httpHeaders = getHttpHeaders();
        RequestEntity<Void> requestEntity = new RequestEntity<>(httpHeaders, HttpMethod.GET, null);
        Issue issue = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Issue.class).getBody();
        issue.setUser(developerId);
        issueRepository.save(issue);
        return IssueDTO.builder().totalIssueCount(issue.getTotalIssues()).developerName(developerId).build();
    }

    @Override
    public PRDTO getDeveloperPRCount(String developerId) {
        String url = GITHUB_API + PATH_SEPARATOR + SEARCH_ENDPOINT + PATH_SEPARATOR + ISSUE_ENDPOINT +
                String.format(PR_QUERY, developerId);
        HttpHeaders httpHeaders = getHttpHeaders();
        RequestEntity<Void> requestEntity = new RequestEntity<>(httpHeaders, HttpMethod.GET, null);
        PR pr = restTemplate.exchange(url, HttpMethod.GET, requestEntity, PR.class).getBody();
        pr.setUser(developerId);
        prRepository.save(pr);
        return PRDTO.builder().totalPRCount(pr.getTotalIssues()).developerName(developerId).build();
    }

    private List<Repository> getUserRepositories(String developerId) {
        String url = GITHUB_API + PATH_SEPARATOR + USERS_ENDPOINT + PATH_SEPARATOR + developerId + PATH_SEPARATOR + REPOSITORIES_ENDPOINT;
        HttpHeaders httpHeaders = getHttpHeaders();
        RequestEntity<Void> requestEntity = new RequestEntity<>(httpHeaders, HttpMethod.GET, null);
        return Arrays.asList(restTemplate.exchange(url, HttpMethod.GET, requestEntity, Repository[].class).getBody());
    }

    private List<Commit> getCommitsForRepository(String developerId, String repositoryName) {
        String url = GITHUB_API + PATH_SEPARATOR + REPOSITORIES_ENDPOINT + PATH_SEPARATOR + developerId + PATH_SEPARATOR +
                repositoryName + PATH_SEPARATOR + COMMITS_ENDPOINT + String.format(AUTHOR_QUERY, developerId);
        HttpHeaders httpHeaders = getHttpHeaders();
        RequestEntity<Void> requestEntity = new RequestEntity<>(httpHeaders, HttpMethod.GET, null);
        return Arrays.asList(restTemplate.exchange(url, HttpMethod.GET, requestEntity, Commit[].class).getBody());
    }


    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        return headers;
    }
}
