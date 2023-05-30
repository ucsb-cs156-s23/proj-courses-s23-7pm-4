import React from "react";
import OurTable from "main/components/OurTable";

import { yyyyqToQyy } from "main/utils/quarterUtilities.js";

function getFirstVal(values) {
    return values[0];
};

function getCourseId(courseIds) {
    return courseIds[0].substring(0, courseIds[0].length - 2);
}

export default function BasicCourseTable({ courses }) {

    const columns = [
        {
            Header: 'Quarter',
            accessor: (row, _rowIndex) => yyyyqToQyy(row.quarter),
            id: 'quarter',
        },
        {
            Header: 'Course Id',
            accessor: 'courseInfo.courseId',

            aggregate: getCourseId,
            Aggregated: ({ cell: { value } }) => `${value}`,

            Cell: ({ cell: { value } }) => value.substring(0, value.length - 2)
        },
        {
            Header: 'Title',
            accessor: 'title',

            aggregate: getFirstVal,
            Aggregated: ({ cell: { value } }) => `${value}`
        },
        {
            Header: 'Description',
            accessor: 'description',
        },
        {
            Header: 'Level Code',
            accessor: 'objLevelCode',
        },
        {
            Header: 'Subject Area',
            accessor: 'subjectArea',
        },
        {
            Header: 'Units',
            accessor: 'unitsFixed',
        },
    ];

    return <OurTable
        data={courses}
        columns={columns}
        testid={"BasicCourseTable"}
    />;
};