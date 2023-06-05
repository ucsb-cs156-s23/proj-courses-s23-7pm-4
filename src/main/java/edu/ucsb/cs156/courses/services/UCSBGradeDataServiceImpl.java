package edu.ucsb.cs156.courses.services;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;

import edu.ucsb.cs156.courses.entities.GradeData;
import edu.ucsb.cs156.courses.models.Quarter;
import edu.ucsb.cs156.courses.models.github.ApiResult;
import edu.ucsb.cs156.courses.models.github.TreeElement;

import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("UCSBGradeDataService")
@Slf4j
public class UCSBGradeDataServiceImpl implements UCSBGradeDataService {

    private final RestTemplate restTemplate;

    public UCSBGradeDataServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    @Autowired
    ObjectMapper mapper;

    public static final String REPO_OWNER_AND_NAME = "ucsb-cs156/UCSB_Grades";
    public static final String API_ENDPOINT = "https://api.github.com/repos/"
            + REPO_OWNER_AND_NAME +
            "/git/trees/main?recursive=1";

    @Override
    public List<String> getUrls() throws Exception {

        log.info("getting data from {}", API_ENDPOINT);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        Map<String, String> uriVariables = Map.of("recursive", "1");

        ResponseEntity<String> re = restTemplate.exchange(API_ENDPOINT, HttpMethod.GET, entity, String.class,
                uriVariables);

        ApiResult apiResult = mapper.readValue(re.getBody(), ApiResult.class);

        List<TreeElement> treeElements = apiResult.getTree();
        List<String> urls = treeElements.stream().map(treeElement -> treeElement.getPath())
                .filter(path -> (path.startsWith("quarters/") && path.endsWith(".csv"))).collect(Collectors.toList());
        List<String> rawUrls = urls.stream()
                .map(url -> "https://raw.githubusercontent.com/" + REPO_OWNER_AND_NAME + "/main/" + url)
                .collect(Collectors.toList());
        return rawUrls;
    }

    @Override
    public List<GradeData> getGradeData(String url) throws Exception {
        log.info("getting data from {}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String csvData = re.getBody();
        return parse(new StringReader(csvData));
    }

    @Override
    public List<GradeData> parse(Reader reader) throws Exception {
        List<GradeData> gradeDataList = new ArrayList<GradeData>();
        log.info("Parsing CSV file with grade data... ");
        CSVReader csvReader = new CSVReader(reader);
        csvReader.skip(1);
        List<String[]> myEntries = csvReader.readAll();
        for (String[] row : myEntries) {
            String yyyyq = Integer.toString(Quarter.qyyToyyyyQ(row[0]));
            GradeData gradeData = GradeData.builder()
                    .yyyyq(yyyyq)
                    .course(row[2])
                    .professor(row[3])
                    .grade(row[4])
                    .count(Integer.parseInt(row[5]))
                    .build();
            log.info("Parsed: " + gradeData.toString());
            gradeDataList.add(gradeData);
        }
        csvReader.close();
        return gradeDataList;
    }
}