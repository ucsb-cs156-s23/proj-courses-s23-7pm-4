package edu.ucsb.cs156.courses.services;

import edu.ucsb.cs156.courses.entities.GradeData;

import java.io.Reader;
import java.util.List;


public interface UCSBGradeDataService {
    public List<String> getUrls() throws Exception;
    public List<GradeData> getGradeData(String url) throws Exception;
    public List<GradeData> parse(Reader reader) throws Exception;
}