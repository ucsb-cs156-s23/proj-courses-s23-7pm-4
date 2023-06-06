import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { toast } from "react-toastify";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import { allTheSubjects } from "fixtures/subjectFixtures";

import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";

import CourseInstructorSearchForm from "main/components/BasicCourseSearch/CourseInstructorSearchForm";

jest.mock("react-toastify", () => ({
  toast: jest.fn(),
}));

describe("CourseInstructorSearchForm tests", () => {

  const axiosMock = new AxiosMockAdapter(axios);

  const queryClient = new QueryClient();
  const addToast = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
    jest.spyOn(console, 'error')
    console.error.mockImplementation(() => null);

    axiosMock
      .onGet("/api/currentUser")
      .reply(200, apiCurrentUserFixtures.userOnly);
    axiosMock
      .onGet("/api/systemInfo")
      .reply(200, {
        ...systemInfoFixtures.showingNeither,
        "startQtrYYYYQ": "20201",
        "endQtrYYYYQ": "20214"
      });

    toast.mockReturnValue({
      addToast: addToast,
    });
  });


  test("renders without crashing", () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <CourseInstructorSearchForm />
        </MemoryRouter>
      </QueryClientProvider>
    );
  });

  test("when I select a start quarter, the state for start quarter changes", () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <CourseInstructorSearchForm />
        </MemoryRouter>
      </QueryClientProvider>
    );
    const selectStartQuarter = screen.getByLabelText("Start Quarter");
    userEvent.selectOptions(selectStartQuarter, "20204");
    expect(selectStartQuarter.value).toBe("20204");
  });

  test("when I select the end quarter, the state for the end quarter changes", () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <CourseInstructorSearchForm />
        </MemoryRouter>
      </QueryClientProvider>
    );
    const selectEndQuarter = screen.getByLabelText("End Quarter");
    userEvent.selectOptions(selectEndQuarter, "20204");
    expect(selectEndQuarter.value).toBe("20204");
  });

  test("Correct style for submit button when rendered", () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <CourseInstructorSearchForm />
        </MemoryRouter>
      </QueryClientProvider>
    );
    const submitButton = screen.getByTestId("submit-button");
    expect(submitButton).toBeInTheDocument();
    expect(submitButton).toHaveStyle({ paddingTop: "10px", paddingBottom: "10px" });
  });

  test("when I select the instructor, the state for the instructor changes", () => {
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <CourseInstructorSearchForm />
        </MemoryRouter>
      </QueryClientProvider>
    );
    const selectInstructor = screen.getByLabelText("Instructor Name (Try searching 'Conrad' or 'Mirza')");
    userEvent.type(selectInstructor, "Conrad");
    expect(selectInstructor.value).toBe("Conrad");
  });


  test("when I click submit, the right stuff happens", async () => {
    axiosMock.onGet("/api/UCSBSubjects/all").reply(200, allTheSubjects);
    const sampleReturnValue = {
      sampleKey: "sampleValue",
    };

    const fetchJSONSpy = jest.fn();

    fetchJSONSpy.mockResolvedValue(sampleReturnValue);

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <CourseInstructorSearchForm fetchJSON={fetchJSONSpy} />
        </MemoryRouter>
      </QueryClientProvider>
    );

    const expectedFields = {
      startQuarter: "20211",
      endQuarter: "20214",
      instructor: "",
    };


    const selectStartQuarter = screen.getByLabelText("Start Quarter");
    userEvent.selectOptions(selectStartQuarter, "20211");
    const selectEndQuarter = screen.getByLabelText("End Quarter");
    userEvent.selectOptions(selectEndQuarter, "20214");
    const selectInstructor = screen.getByLabelText("Instructor Name (Try searching 'Conrad' or 'Mirza')");
    userEvent.type(selectInstructor, "");
    const submitButton = screen.getByText("Submit");
    userEvent.click(submitButton);

    await waitFor(() => expect(fetchJSONSpy).toHaveBeenCalledTimes(1));

    expect(fetchJSONSpy).toHaveBeenCalledWith(
      expect.any(Object),
      expectedFields
    );
  });

  test("when I click submit when JSON is EMPTY, setCourse is not called!", async () => {
    axiosMock.onGet("/api/UCSBSubjects/all").reply(200, allTheSubjects);

    const sampleReturnValue = {
      sampleKey: "sampleValue",
      total: 0,
    };

    const fetchJSONSpy = jest.fn();

    fetchJSONSpy.mockResolvedValue(sampleReturnValue);

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <CourseInstructorSearchForm fetchJSON={fetchJSONSpy} />
        </MemoryRouter>
      </QueryClientProvider>
    );



    const selectStartQuarter = screen.getByLabelText("Start Quarter");
    userEvent.selectOptions(selectStartQuarter, "20204");
    const selectEndQuarter = screen.getByLabelText("End Quarter");
    userEvent.selectOptions(selectEndQuarter, "20204");
    const selectInstructor = screen.getByLabelText("Instructor Name (Try searching 'Conrad' or 'Mirza')");
    userEvent.type(selectInstructor, "Conrad");
    const submitButton = screen.getByText("Submit");
    userEvent.click(submitButton);
  });


  test("renders without crashing when fallback values are used", async () => {

    axiosMock
      .onGet("/api/systemInfo")
      .reply(200, {
        "springH2ConsoleEnabled": false,
        "showSwaggerUILink": false,
        "startQtrYYYYQ": null, // use fallback value
        "endQtrYYYYQ": null  // use fallback value
      });

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <CourseInstructorSearchForm />
        </MemoryRouter>
      </QueryClientProvider>
    );

    // Make sure the first and last options 
    expect(await screen.findByTestId(/CourseInstructorSearch.StartQuarter-option-0/)).toHaveValue("20211")
    expect(await screen.findByTestId(/CourseInstructorSearch.StartQuarter-option-3/)).toHaveValue("20214")

  });

});