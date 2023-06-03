package edu.ucsb.cs156.courses.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GradeDataTests {
    @Test
    public void test_getSubjectArea_and_getCourseNum_CMPSC_156() throws Exception {
        GradeData gh = GradeData.builder()
            .course("CMPSC   156")
            .build();
        assertEquals("CMPSC", gh.getSubjectArea());
        assertEquals("156", gh.getCourseNum());
    }

    @Test
    public void test_getSubjectArea_and_getCourseNum_null() throws Exception {
        GradeData gh = GradeData.builder()
            .course(null)
            .build();
        assertNull(gh.getSubjectArea());
        assertNull(gh.getCourseNum());
    } 
}