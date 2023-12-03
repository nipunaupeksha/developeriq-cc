package com.acmecorp.developeriq.controller;

import com.acmecorp.developeriq.dto.CommitDTO;
import com.acmecorp.developeriq.dto.IssueDTO;
import com.acmecorp.developeriq.dto.PRDTO;
import com.acmecorp.developeriq.model.ModelApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "developer")
@RequestMapping("/api/v1/developers")
public interface DeveloperController {
    /**
     * Number of commits by a developer.
     *
     * @param developerId The username/id of the developer.
     * @return The list of commits by the developer in each repository.
     */
    @ApiOperation(value = "Get total commits by a user", nickname = "developerCommitCount",
            notes = "Returns the status", response = ModelApiResponse.class, tags = {"developer"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = ModelApiResponse.class),
            @ApiResponse(code = 400, message = "Invalid request params", response = ModelApiResponse.class),
            @ApiResponse(code = 404, message = "Resource not found", response = ModelApiResponse.class)})
    @RequestMapping(value = "/{developerId}/commits",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<CommitDTO>> getDeveloperCommitCount(@PathVariable("developerId") String developerId);

    /**
     * Number of issues created by a developer.
     *
     * @param developerId The username/id of the developer.
     * @return The list of issues by the developer.
     */
    @ApiOperation(value = "Get total issues created by a user", nickname = "developerCreatedIssues",
            notes = "Returns the status", response = ModelApiResponse.class, tags = {"developer"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = ModelApiResponse.class),
            @ApiResponse(code = 400, message = "Invalid request params", response = ModelApiResponse.class),
            @ApiResponse(code = 404, message = "Resource not found", response = ModelApiResponse.class)})
    @RequestMapping(value = "/{developerId}/issues",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<IssueDTO> getDeveloperCreatedIssues(@PathVariable("developerId") String developerId);

    /**
     * Number of pull requests created by a developer.
     *
     * @param developerId The username/id of the developer.
     * @return The list of pull-requests by the developer..
     */
    @ApiOperation(value = "Get total pull requests created by a user", nickname = "developerPullRequests",
            notes = "Returns the status", response = ModelApiResponse.class, tags = {"developer"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = ModelApiResponse.class),
            @ApiResponse(code = 400, message = "Invalid request params", response = ModelApiResponse.class),
            @ApiResponse(code = 404, message = "Resource not found", response = ModelApiResponse.class)})
    @RequestMapping(value = "/{developerId}/pull-requests",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<PRDTO> getDeveloperPullRequests(@PathVariable("developerId") String developerId);
}
