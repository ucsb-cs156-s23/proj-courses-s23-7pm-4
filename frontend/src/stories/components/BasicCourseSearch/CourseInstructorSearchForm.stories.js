import React from "react";

import CourseInstructorSearchForm from "main/components/BasicCourseSearch/CourseInstructorSearchForm";
import { allTheSubjects } from "fixtures/subjectFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";

export default {
  title: "components/BasicCourseSearch/CourseInstructorSearchForm",
  component: CourseInstructorSearchForm,
  parameters: {
    mockData: [
      {
        url: '/api/UCSBSubjects/all',
        method: 'GET',
        status: 200,
        response: allTheSubjects
      },
      {
        url: '/api/systemInfo',
        method: 'GET',
        status: 200,
        response: systemInfoFixtures.showingBothStartAndEndQtr
      },
    ],
  },
};

const Template = (args) => {
  return <CourseInstructorSearchForm {...args} />;
};

export const Default = Template.bind({});

Default.args = {
  submitText: "Create",
  fetchJSON: (_event, data) => {
    console.log("Submit was clicked, data=", data);
  }
};