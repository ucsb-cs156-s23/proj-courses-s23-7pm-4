package edu.ucsb.cs156.courses.entities;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.GeneratedValue;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "gradedata")
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueGradeData", columnNames = { "yyyyq", "course", "instructor", "grade"})})
public class GradeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String yyyyq;
    private String course;
    private String professor;
    private String grade;
    private String count;

    public String getSubjectArea() {
        if (course==null)
            return null;
        return course.substring(0, 8).trim();
    }
    public String getCourseNum() {
        if (course==null)
            return null;
        return course.substring(8).trim();
    }
}