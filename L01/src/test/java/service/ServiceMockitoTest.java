package service;

import domain.Homework;
import domain.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ServiceMockitoTest {
    @Mock
    public StudentXMLRepository studentXMLRepository;
    @Mock
    public HomeworkXMLRepository homeworkXMLRepository;
    @Mock
    public GradeXMLRepository gradeXMLRepository;

    public Service service;


    @org.junit.jupiter.api.BeforeEach
    public void init() {
//        MockitoAnnotations.initMocks(ServiceMockitoTest.class);
        studentXMLRepository = mock(StudentXMLRepository.class);
        homeworkXMLRepository = mock(HomeworkXMLRepository.class);
        gradeXMLRepository = mock(GradeXMLRepository.class);
        service = new Service(studentXMLRepository, homeworkXMLRepository, gradeXMLRepository);
    }
    @Test
    public void findAllHomeworkTest() {
        List<Homework> homeworks = new java.util.ArrayList<>(Collections.emptyList());
        homeworks.add(new Homework("1","First description 1",9,5));
        homeworks.add(new Homework("2","First description 2",7,4));
        homeworks.add(new Homework("3","First description 3",6,5));
        when(homeworkXMLRepository.findAll()).thenReturn(homeworks);
        Assertions.assertEquals(homeworks, homeworkXMLRepository.findAll());
    }

    @Test
    public void testSaveHomework() {
        Homework dummyHomework = new Homework("1","description 1",9,5);
        service.saveHomework(dummyHomework.getID(), dummyHomework.getDescription(), dummyHomework.getDeadline(), dummyHomework.getStartline());
        Mockito.verify(homeworkXMLRepository).save(dummyHomework);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1"})
    void extendUpdateStudentTest(String id) {
        Student newStudent = new Student("1","Jonny",333);
        assertEquals(1, service.updateStudent(id,"Jonny",333));
        assertEquals(newStudent, service.findByID(id));
    }

}