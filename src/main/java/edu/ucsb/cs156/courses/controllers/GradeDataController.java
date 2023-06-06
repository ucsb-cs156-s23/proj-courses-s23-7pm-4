package edu.ucsb.cs156.courses.controllers;

import edu.ucsb.cs156.courses.entities.GradeData;

import edu.ucsb.cs156.courses.repositories.GradeDataRepository;
import edu.ucsb.cs156.courses.services.UCSBGradeDataService;
import edu.ucsb.cs156.courses.utilities.CourseUtilities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Slf4j
@Api(description = "API for grade data data")
@RequestMapping("/api/gradedata")
@RestController
public class GradeDataController extends ApiController {
    @Autowired
    GradeDataRepository gradeDataRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "Get grade data for a course")
    @GetMapping(value = "/search", produces = "application/json")
    public Iterable<GradeData> gradeDataBySubjectAreaAndCourseNumber(
        @RequestParam String subjectArea,
        @RequestParam String courseNumber
    )  {
      String course=CourseUtilities.makeFormattedCourseId(subjectArea, courseNumber);
      Iterable<GradeData> gradeDataRows = gradeDataRepository.findByCourse(course);
      return gradeDataRows;
    }

}