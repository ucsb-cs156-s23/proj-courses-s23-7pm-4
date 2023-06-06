import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import { useBackendMutation, useBackend } from "main/utils/useBackend";
import { toast } from "react-toastify";
import { Button } from "react-bootstrap";
import UCSBSubjectsTable from 'main/components/UCSBGradeData/UCSBGradeDataTable';

export default function AdminLoadSubjectsPage() {
  const { data: subjects, error: _error, status: _status } =
      useBackend(
        // Stryker disable next-line all : don't test internal caching of React Query
        ["/api/UCSBGradeData/all"], { method: "GET", url: "/api/UCSBGradeData/all" }, []
      );

  const objectToAxiosParams = () => ({
    url: '/api/UCSBGradeData/load',
    method: 'POST',
  });

//  var gradeDataCount = gradeData.length;

//   const onSuccess = (gradeData) => {
//     toast(`Number of Subjects Loaded : ${subjects.length - subjectsCount}`);
//     subjectsCount = subjects.length;
//   };


  const mutation = useBackendMutation(
    objectToAxiosParams,
    { onSuccess },
    // Stryker disable next-line all : hard to set up test for caching
    ["/api/UCSBGradeData/all"]
  );


  const onSubmit = async (data) => {
      mutation.mutate(data);
  };

  return (
    <BasicLayout>
      <h2>Grade Data</h2>
      <Button
        variant="primary"
        onClick={onSubmit}
        data-testid="AdminLoadGradeData-Load-Button"
      >
        Load GradeData
      </Button>
      <UCSBGradeDataTable gradeData={gradeData} />
    </BasicLayout>
  );
};