package edu.ucsb.cs156.courses.documents;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "updates")
public class Update {
    private ObjectId subject_area; // need to define/change class type
    private CourseInfo quarter_yyyyq; // need to define/change class type
    private Section last_update; // need to define/change class type

    @Override
    public Object clone() throws CloneNotSupportedException {

        Update newUpdate = new Update();
        
        newUpdate.set_id(this._id);

        CourseInfo newCourseInfo = (CourseInfo) this.getCourseInfo().clone();
        newUpdate.setCourseInfo(newCourseInfo);

        Section newSection = (Section) this.getSection().clone();
        newUpdate.setSection(newSection);

        return newUpdate;
    }
}
