package edu.ucsb.cs156.courses.repositories;

import edu.ucsb.cs156.courses.entities.GradeData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeDataRepository extends CrudRepository<GradeData, Long> {
    public List<GradeData> findByYyyyqAndCourseAndProfessorAndGrade(String yyyyq, String course, String professor, String grade);
    public List<GradeData> findByCourse(String course);
}