package edu.ucsb.cs156.courses.jobs;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import edu.ucsb.cs156.courses.repositories.GradeDataRepository;
import edu.ucsb.cs156.courses.entities.GradeData;
import edu.ucsb.cs156.courses.entities.Job;
import edu.ucsb.cs156.courses.services.UCSBGradeDataService;
import edu.ucsb.cs156.courses.services.jobs.JobContext;

@RestClientTest(UploadGradeDataJob.class)
@AutoConfigureDataJpa
public class UploadGradeDataJobTests {

    @MockBean
    GradeDataRepository gradeDataRepository;

    @MockBean
    UCSBGradeDataService ucsbGradeDataService;

    @Test
    public void test_upsertAll() {

        // arrange

        List<GradeData> gradeHistoriesToUpsert = new ArrayList<GradeData>();
        GradeData existingOne = GradeData.builder()
                .yyyyq("20204")
                .course("CMPSC   156")
                .professor("CONRAD P")
                .grade("A")
                .count(50)
                .build();
        GradeData existingOneUpdated = GradeData.builder()
                .yyyyq("20204")
                .course("CMPSC   156")
                .professor("CONRAD P")
                .grade("A")
                .count(51)
                .build();
        GradeData newOne = GradeData.builder()
                .yyyyq("20204")
                .course("CMPSC   148")
                .professor("HOLLERER T")
                .grade("A")
                .count(50)
                .build();

        gradeHistoriesToUpsert.add(existingOneUpdated);
        gradeHistoriesToUpsert.add(newOne);

        when(gradeDataRepository.findByYyyyqAndCourseAndProfessorAndGrade(eq("20204"), eq("CMPSC   156"), eq("CONRAD P"), eq("A")))
            .thenReturn(Arrays.asList(existingOne));

        when(gradeDataRepository.findByYyyyqAndCourseAndProfessorAndGrade(eq("20204"), eq("CMPSC   148"), eq("HOLLERER T"), eq("A")))
            .thenReturn(Arrays.asList());

        when(gradeDataRepository.save(eq(existingOneUpdated)))
            .thenReturn(existingOneUpdated);

        when(gradeDataRepository.save(eq(newOne)))
            .thenReturn(newOne);


        // act

        List<GradeData> result = UploadGradeDataJob.upsertAll(gradeDataRepository, gradeHistoriesToUpsert);

        // assert

        assertTrue(result.contains(existingOne));
        assertTrue(result.contains(newOne));

        verify(gradeDataRepository).findByYyyyqAndCourseAndProfessorAndGrade(eq("20204"), eq("CMPSC   156"), eq("CONRAD P"), eq("A"));
        verify(gradeDataRepository).findByYyyyqAndCourseAndProfessorAndGrade(eq("20204"), eq("CMPSC   148"), eq("HOLLERER T"), eq("A"));
        verify(gradeDataRepository).save(existingOneUpdated);
        verify(gradeDataRepository).save(newOne);

    }

    @Test
    void test_log_output_success() throws Exception {

        // Arrange

        Job jobStarted = Job.builder().build();
        JobContext ctx = new JobContext(null, jobStarted);

        UploadGradeDataJob uploadGradeDataJob = 
            new UploadGradeDataJob(ucsbGradeDataService,
                gradeDataRepository);

        List<String> mockedListOfUrls = new ArrayList<String>();
        mockedListOfUrls.add("https://raw.githubusercontent.com/ucsb-cs156/UCSB_Grades/main/quarters/F20/CMPSC.csv");
        mockedListOfUrls.add("https://raw.githubusercontent.com/ucsb-cs156/UCSB_Grades/main/quarters/F20/CMPTGCS.csv");
        mockedListOfUrls.add("https://raw.githubusercontent.com/ucsb-cs156/UCSB_Grades/main/quarters/W21/CMPSC.csv");
        mockedListOfUrls.add("https://raw.githubusercontent.com/ucsb-cs156/UCSB_Grades/main/quarters/W21/CMPTGCS.csv");

        List<GradeData> gradeData_F20_CMPSC = new ArrayList<GradeData>();
        gradeData_F20_CMPSC.add(
            GradeData.builder()
                .yyyyq("20204")
                .course("CMPSC   156")
                .professor("CONRAD P")
                .grade("A")
                .count(50)
                .build()
        );

        List<GradeData> gradeData_F20_CMPTGCS = new ArrayList<GradeData>();
        gradeData_F20_CMPTGCS.add(
            GradeData.builder()
                .yyyyq("20204")
                .course("CMPTGCS   1A")
                .professor("WANG R K")
                .grade("P")
                .count(8)
                .build()
        );

        List<GradeData> gradeData_W21_CMPSC = new ArrayList<GradeData>();
        gradeData_W21_CMPSC.add(
            GradeData.builder()
                .yyyyq("20211")
                .course("CMPSC   156")
                .professor("CONRAD P")
                .grade("A")
                .count(50)
                .build()
        );

        List<GradeData> gradeData_W21_CMPTGCS = new ArrayList<GradeData>();
        gradeData_W21_CMPTGCS.add(
            GradeData.builder()
                .yyyyq("20211")
                .course("CMPTGCS  20")
                .professor("WANG R K")
                .grade("P")
                .count(8)
                .build()
        );

        when(ucsbGradeDataService.getUrls()).thenReturn(mockedListOfUrls);
        when(ucsbGradeDataService.getGradeData(eq(mockedListOfUrls.get(0)))).thenReturn(gradeData_F20_CMPSC);
        when(ucsbGradeDataService.getGradeData(eq(mockedListOfUrls.get(1)))).thenReturn(gradeData_F20_CMPTGCS);
        when(ucsbGradeDataService.getGradeData(eq(mockedListOfUrls.get(2)))).thenReturn(gradeData_W21_CMPSC);
        when(ucsbGradeDataService.getGradeData(eq(mockedListOfUrls.get(3)))).thenReturn(gradeData_W21_CMPTGCS);

        // Act

        uploadGradeDataJob.accept(ctx);

        // Assert

        String expected = """
            Updating UCSB Grade Data
            Processing data for year: 20204
            Processing data for subjectArea: CMPSC
            Processing data for subjectArea: CMPTGCS
            Processing data for year: 20211
            Processing data for subjectArea: CMPSC
            Processing data for subjectArea: CMPTGCS
            Finished updating UCSB Grade Data""";

        assertEquals(expected, jobStarted.getLog());
    }
}