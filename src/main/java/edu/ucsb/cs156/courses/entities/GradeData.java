package edu.ucsb.cs156.courses.entities;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Entity(name = "grade_data")
public class GradeData {
    @Id
    private String quarter;
    private String level;
    private String department;
    private String courseNumber;
    private String professor;
    private String grade;
    private String numStudents;
}