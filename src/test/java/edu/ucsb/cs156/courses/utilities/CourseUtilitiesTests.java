package edu.ucsb.cs156.courses.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class CourseUtilitiesTests {

  @Test
  void test_makeFormattedCourseId_CMPSC_130A() {
    String actual = CourseUtilities.makeFormattedCourseId("CMPSC", "130A");
    String expected="CMPSC   130A";
    assertEquals(expected,actual);
  }

  @Test
  void test_makeFormattedCourseId_CMPSC_24() {
    String actual = CourseUtilities.makeFormattedCourseId("CMPSC", "24");
    String expected="CMPSC    24";
    assertEquals(expected,actual);
  }

  @Test
  void test_makeFormattedCourseId_CMPSC_5JA() {
    String actual = CourseUtilities.makeFormattedCourseId("CMPSC", "5JA");
    String expected="CMPSC     5JA";
    assertEquals(expected,actual);
  }

  @Test
  void test_makeFormattedCourseId_CMPSC_5A() {
    String actual = CourseUtilities.makeFormattedCourseId("CMPSC", "5A");
    String expected="CMPSC     5A";
    assertEquals(expected,actual);
  }

}