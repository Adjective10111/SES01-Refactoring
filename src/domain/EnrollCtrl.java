package domain;

import domain.exceptions.EnrollmentRulesViolationException;

import java.util.List;
import java.util.Map;

public class EnrollCtrl {
    // check if it is passed or has unsatisfied prerequisites
    private void checkCourse(Student student, Course course) throws EnrollmentRulesViolationException {
        if (student.passed(course))
            throw new EnrollmentRulesViolationException
                    (String.format("The student has already passed %s", course.getName()));
        for (var prereq : course.prerequisites) {
            if (!student.passed(prereq))
                throw new EnrollmentRulesViolationException
                        (String.format("The student has not passed %s as a prerequisite of %s",
                                prereq.getName(), course.getName()));
        }
    }

    // check if exam time has collision with another course or if there are duplicates
    private void checkCollision(List<CSE> courses) throws EnrollmentRulesViolationException {
        for (CSE cse : courses) {
            for (var cse2 : courses) {
                if (cse == cse2)
                    continue;
                if (cse.getExamTime().equals(cse2.getExamTime()))
                    throw new EnrollmentRulesViolationException
                            (String.format("Two offerings %s and %s have the same exam time", cse, cse2));
                if (cse.getCourse().equals(cse2.getCourse()))
                    throw new EnrollmentRulesViolationException
                            (String.format("%s is requested to be taken twice", cse.getCourse().getName()));
            }
        }
    }

    private int getRequestedUnits(List<CSE> courses) {
        int unitsRequested = 0;
        for (CSE o : courses)
            unitsRequested += o.getCourse().getUnits();
        return unitsRequested;
    }

    public void enroll(Student s, List<CSE> courses) throws EnrollmentRulesViolationException {
        for (CSE cse : courses) {
            checkCourse(s, cse.getCourse());
        }
        checkCollision(courses);

        // check unit taking limit based on gpa
        int unitsRequested = getRequestedUnits(courses);
        int allowedUnits = s.getAllowedUnits();
        if (allowedUnits < unitsRequested)
            throw new EnrollmentRulesViolationException
                    (String.format("Number of units (%d) requested does not match GPA of %f", unitsRequested, s.gpa()));

        // take the courses if no exception was thrown
        for (CSE o : courses)
            s.takeCourse(o.getCourse(), o.getSection());
    }
}
