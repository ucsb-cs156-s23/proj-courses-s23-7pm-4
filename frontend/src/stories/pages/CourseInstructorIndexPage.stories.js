import React from 'react';

import CourseInstructorIndexPage from "main/pages/CourseInstructor/CourseInstructorIndexPage";
import { threeSections } from "fixtures/sectionFixtures"
import { systemInfoFixtures } from 'fixtures/systemInfoFixtures';
import { ucsbSubjectsFixtures } from 'fixtures/ucsbSubjectsFixtures';
import { apiCurrentUserFixtures } from 'fixtures/currentUserFixtures';

export default {
    title: 'pages/CourseInstructorIndexPage',
    component: CourseInstructorIndexPage,
    parameters: {
        mockData: [
            {
                url: '/api/UCSBSubjects/all',
                method: 'GET',
                status: 200,
                response: ucsbSubjectsFixtures.threeSubjects
            },
            {
                url: '/api/public/courseinstructor/search',
                method: 'GET',
                status: 200,
                response: threeSections
            },
            {
                url: '/api/systemInfo',
                method: 'GET',
                status: 200,
                response: systemInfoFixtures.showingBoth
            },
            {
                url: '/api/currentUser',
                method: 'GET',
                status: 200,
                response: apiCurrentUserFixtures.adminUser
            },
        ]
    }
};

const Template = () => <CourseInstructorIndexPage />;

export const Default = Template.bind({});