import React from "react";
import OurTable, { ButtonColumn } from "main/components/OurTable";
import { useBackendMutation } from "main/utils/useBackend";
import { useNavigate } from "react-router-dom";
import { hasRole } from "main/utils/currentUser";

export default function UCSBGradeDataTable({ subjects, currentUser }) {

    const navigate = useNavigate();

    const editCallback = (cell) => {
        navigate(`/UCSBGradeData/edit/${cell.row.values.subjectCode}`)
    }

    // Stryker disable all : hard to test for query caching
    const deleteMutation = useBackendMutation(
        cellToAxiosParamsDelete,
        { onSuccess: onDeleteSuccess },
        ["/api/UCSBGradeData/all"]
    );
    // Stryker enable all 

    // Stryker disable next-line all : TODO try to make a good test for this
    const deleteCallback = async (cell) => { deleteMutation.mutate(cell); }

    const columns = [
        {
            Header: 'Yyyyq',
            accessor: 'yyyyq', 
        },
        {
            Header: 'Course',
            accessor: 'course',
        },
        {
            Header: 'Professor',
            accessor: 'professor',
        },
        {
            Header: 'Grade',
            accessor: 'grade',
        },
        {
            Header: 'Count',
            accessor: 'count',
        },
        {
            Header: 'Inactive',
            accessor: (row) => String(row.inactive),
            id: 'inactive',
        }
    ];

    // const columnsIfAdmin = [
    //     ...columns,
    //     ButtonColumn("Edit", "primary", editCallback, "UCSBSubjectsTable"),
    //     ButtonColumn("Delete", "danger", deleteCallback, "UCSBSubjectsTable")
    // ]

    const columnsToDisplay = hasRole(currentUser, "ROLE_ADMIN") ? columnsIfAdmin : columns;

    return <OurTable
        data={subjects}
        columns={columnsToDisplay}
        testid={"UCSBGradeDataTable"}
    />;
};