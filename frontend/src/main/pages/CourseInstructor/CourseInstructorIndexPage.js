import { useState } from "react";
import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import CourseInstructorSearchForm from "main/components/BasicCourseSearch/CourseInstructorSearchForm";
import { useBackendMutation } from "main/utils/useBackend";
import BasicCourseTable from "main/components/Courses/BasicCourseTable";

export default function CourseInstructorIndexPage() {
  // Stryker disable next-line all : Can't test state because hook is internal
  const [courseJSON, setCourseJSON] = useState([]);

  const objectToAxiosParams = (query) => ({
    // Stryker disable next-line all : Can't test state because hook is internal
    url: "/api/public/courseinstructor/search",
    params: {
      startQtr: query.startQuarter,
      endQtr: query.endQuarter,
      instructor: query.instructor,
    },
  });

  const onSuccess = (setCourseJSON);



  const mutation = useBackendMutation(
    objectToAxiosParams,
    // Stryker disable next-line all : Can't test state because hook is internal
    { onSuccess },
    // Stryker disable next-line all : hard to set up test for caching
    []
  );

  async function fetchCourseInstructorJSON(_event, query) {
    mutation.mutate(query);
  }

  return (
    <BasicLayout>
      <div className="pt-2">
        <h5>Welcome to the UCSB Course Instructor Search!</h5>
        <CourseInstructorSearchForm fetchJSON={fetchCourseInstructorJSON} />
        {/* Need to still implement search feature */}
        <BasicCourseTable courses={courseJSON} />
      </div>
    </BasicLayout>
  );
}