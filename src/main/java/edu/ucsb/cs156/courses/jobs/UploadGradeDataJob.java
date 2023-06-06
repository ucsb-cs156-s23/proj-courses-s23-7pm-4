package edu.ucsb.cs156.courses.jobs;

import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs156.courses.entities.GradeData;
import edu.ucsb.cs156.courses.repositories.GradeDataRepository;
import edu.ucsb.cs156.courses.services.UCSBGradeDataService;
import edu.ucsb.cs156.courses.services.jobs.JobContext;
import edu.ucsb.cs156.courses.services.jobs.JobContextConsumer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UploadGradeDataJob implements JobContextConsumer {
    @Getter
    private UCSBGradeDataService ucsbGradeDataService;
    @Getter
    private GradeDataRepository gradeDataRepository;

    @Override
    public void accept(JobContext ctx) throws Exception {
        ctx.log("Updating UCSB Grade Data");
        List<String> urls = ucsbGradeDataService.getUrls();

        GradeData previous = new GradeData();
        List<GradeData> results = null;
        for (String url : urls) {
            results = ucsbGradeDataService.getGradeData(url);
            GradeData topRow = results.get(0);
            upsertAll(gradeDataRepository, results);
            logProgress(ctx, topRow, previous);
        }

        ctx.log("Finished updating UCSB Grade Data");
    }

    private void logProgress(JobContext ctx, GradeData topRow, GradeData previous) {
        if (!topRow.getYyyyq().equals(previous.getYyyyq())) {
            ctx.log("Processing data for year: " + topRow.getYyyyq());
            previous.setYyyyq(topRow.getYyyyq());
        }
        ctx.log("Processing data for subjectArea: " + topRow.getSubjectArea());
    }

    public static List<GradeData> upsertAll(
            GradeDataRepository gradeDataRepository,
            List<GradeData> gradeDatas) {
        List<GradeData> result = new ArrayList<GradeData>();
        for (GradeData gradeData : gradeDatas) {
            List<GradeData> query = gradeDataRepository.findByYyyyqAndCourseAndProfessorAndGrade(
                    gradeData.getYyyyq(), gradeData.getCourse(), gradeData.getProfessor(),
                    gradeData.getGrade());
            if (query.size() == 0) {
                gradeData = gradeDataRepository.save(gradeData);
                result.add(gradeData);
            } else {
                GradeData existing = query.get(0);
                existing.setCount(gradeData.getCount());
                existing = gradeDataRepository.save(existing);
                result.add(existing);
            }
        }
        return result;
    }
}