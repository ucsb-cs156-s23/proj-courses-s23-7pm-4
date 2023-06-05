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
    private String subject_area;
    private String quarter_yyyyq;
    private String last_update;

    @Override
    public Object clone() throws CloneNotSupportedException {

        Update newUpdate = new Update();

        String newSubjectArea = (String) this.getSubject_area().clone();
        newUpdate.setSubject_area(newSubjectArea);

        String newQuarter = (String) this.getQuarter_yyyyq().clone();
        newUpdate.setQuarter_yyyyq(newQuarter);

        String newLastUpdate = (String) this.getLast_update().clone();
        newUpdate.setLast_update(newLastUpdate);

        return newUpdate;
    }
}
