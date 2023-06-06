package edu.ucsb.cs156.courses.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ucsb.cs156.courses.repositories.GradeDataRepository;
import edu.ucsb.cs156.courses.services.UCSBGradeDataService;
import lombok.extern.slf4j.Slf4j;

@Service
public class UploadGradeDataJobFactory {

    @Autowired
    UCSBGradeDataService ucsbGradeDataService;

    @Autowired
    GradeDataRepository gradeDataRepository;

    public UploadGradeDataJob create() {
        return new UploadGradeDataJob(
                ucsbGradeDataService,
                gradeDataRepository);
    }
}