package service;

import domain.Grade;
import domain.Homework;
import domain.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;
import validation.*;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    private Service service;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "students.xml");
        HomeworkXMLRepository fileRepository2 = new HomeworkXMLRepository(homeworkValidator, "homework.xml");
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, "grades.xml");

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @org.junit.jupiter.api.Test
    void saveStudentWithExistingID() {
        service.deleteStudent("3");
        assertEquals(0,service.saveStudent("3","Biborka",313));
        assertEquals(1,service.saveStudent("3","Biborka",313));
        service.deleteStudent("3");
    }

    @org.junit.jupiter.api.Test
    void saveHomeworkDeadlineAndStartlineTest() {
        service.deleteHomework("4");
        service.deleteHomework("5");
        assertEquals(0,service.saveHomework("4","The hardest homework ever.",14,5));
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            service.saveHomework("5","The second hardest homework ever.",14,166);
        }, "ValidationException was expected");

        Assertions.assertEquals("Deadline invalid! \n", thrown.getMessage());
        service.deleteHomework("4");
        service.deleteHomework("5");
    }

    @org.junit.jupiter.api.Test
    void saveGradeTest() {
        // grade already exist
        assertEquals(1,service.saveGrade("1","2",5.66,8,"Not bad"));
        // invalid StudentId
        assertEquals(-1,service.saveGrade("1999","2",5.66,8,"Not bad"));
        // invalid HomeworkId
        assertEquals(-1,service.saveGrade("1","21444",5.66,8,"Not bad"));
  }

    @org.junit.jupiter.api.Test
    void extendDeadLineAndUpdateHomeworkTest() {
        assertAll(
                () -> assertEquals(0, service.extendDeadline("2",10)),
                () -> assertEquals(0, service.updateHomework("1","New description", 7,3)),
                () -> assertEquals(1, service.updateHomework("1123","New description", 7,3))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"1","2"})
    void extendUpdateStudentTest(String id) {
        assertEquals(0, service.updateStudent(id,"Jonny",333));
    }
}