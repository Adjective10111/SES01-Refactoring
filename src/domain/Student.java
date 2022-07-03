package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {
    private final String id;
    private final String name;
    private final Map<Term, Map<Course, Double>> transcript;
    private final List<CourseSection> currentTerm;
    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.transcript = new HashMap<>();
        this.currentTerm = new ArrayList<>();
    }

    public void takeCourse(Course c, int section) {
        currentTerm.add(new CourseSection(c, section));
    }

    public Map<Term, Map<Course, Double>> getTranscript() {
        return transcript;
    }

    public void addTranscriptRecord(Course course, Term term, double grade) {
        if (!transcript.containsKey(term))
            transcript.put(term, new HashMap<>());
        transcript.get(term).put(course, grade);
    }

    public List<CourseSection> getCurrentTerm() {
        return currentTerm;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public boolean passed(Course course) {
        for (var termEntry : transcript.entrySet()) {
            var courseScoreMap = termEntry.getValue();
            var foundCourse = courseScoreMap.get(course);
            if (foundCourse == null)
                break;
            else if (foundCourse >= 10)
                return true;
        }
        return false;
    }

    public double gpa() {
        double score = 0;
        int totalUnits = 0;

        for (var termEntry : transcript.entrySet()) {
            var courseScoreMap = termEntry.getValue();
            for (var courseEntry : courseScoreMap.entrySet()) {
                int units = courseEntry.getKey().getUnits();
                totalUnits += units;
                score += units * courseEntry.getValue();
            }
        }

        return score / totalUnits;
    }

    public int getAllowedUnits() {
        var gpaVal = gpa();
        if (gpaVal < 12)
            return 14;
        else if (gpaVal < 16)
            return 16;
        else
            return 20;
    }

    static class CourseSection {
        Course course;
        int section;
        CourseSection(Course course, int section) {
            this.course = course;
            this.section = section;
        }
    }
}
