package edu.ucsb.cs156.courses.collections;

import java.util.Optional;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import edu.ucsb.cs156.courses.documents.Update;

@Repository
public interface UpdateCollection extends MongoRepository<Update, ObjectId> { // edit objectid if needed
    @Query("{'courseInfo.quarter': ?0, 'section.enrollCode': ?1}") // edit
    Optional<Update> findOneByQuarterAndEnrollCode(String quarter, String enrollCode); //edit

    @Query("{'courseInfo.quarter': {$gte: ?0, $lte: ?1}, 'courseInfo.courseId': { $regex: ?2 }}") // edit
    List<Update> findByQuarterRangeAndCourseId( // edit function name and values
        String startQuarter, 
        String endQuarter,
        String courseId );
    
}
