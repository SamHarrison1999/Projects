package com.sg.classroster.dao;

import com.sg.classroster.dto.Student;
import java.io.FileWriter;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClassRosterDaoFileImplTest {

    ClassRosterDao testDao;

    public ClassRosterDaoFileImplTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() throws Exception{
        String testFile = "testroster.txt";
        new FileWriter(testFile);
        testDao = new ClassRosterDaoFileImpl(testFile);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAddGetStudent() throws Exception{
        //ARRANGE
        //Create test inputs
        String studentId = "0001";
        Student student = new Student(studentId);
        student.setFirstName("Ada");
        student.setLastName("Lovelace");
        student.setCohort("Java-May-1845");

        //ACT
        //Add to DAO
        testDao.addStudent(studentId, student);

        //Get student
        Student retrievedStudent = testDao.getStudent(studentId);

        //ASSERT
        //Check data
        assertEquals(student.getStudentId(),retrievedStudent.getStudentId(), "Checking student id.");
        assertEquals(student.getFirstName(),retrievedStudent.getFirstName(), "Checking student first name.");
        assertEquals(student.getLastName(),retrievedStudent.getLastName(), "Checking student last name.");
        assertEquals(student.getCohort(),retrievedStudent.getCohort(), "Checking student cohort.");
    }

    @Test
    public void testAddGetAllStudents() throws Exception{
        //ARRANGE
        //Create test inputs
        String studentId = "0001";
        Student firstStudent = new Student(studentId);
        firstStudent.setFirstName("Ada");
        firstStudent.setLastName("Lovelace");
        firstStudent.setCohort("Java-May-1845");

        Student secondStudent = new Student("0002");
        secondStudent.setFirstName("Charles");
        secondStudent.setLastName("Babbage");
        secondStudent.setCohort(".NET-May-1845");

        testDao.addStudent(firstStudent.getStudentId(),firstStudent);
        testDao.addStudent(secondStudent.getStudentId(),secondStudent);

        //ACT
        //Retrieve list of all students
        List<Student> allStudents = testDao.getAllStudents();


        //ASSERT
        //Check general content
        assertNotNull(allStudents,"The lost of students must not null");
        assertEquals(2, allStudents.size(),"List of students should have 2 students");

        //Check specifics
        assertTrue(testDao.getAllStudents().contains(firstStudent),"The list of students should include Ada.");
        assertTrue(testDao.getAllStudents().contains(secondStudent),"The list of students should include Charles.");
    }

    @Test
    public void testRemoveStudent() throws Exception{
        //ARRANGE
        //Create test inputs
        String studentId = "0001";
        Student firstStudent = new Student(studentId);
        firstStudent.setFirstName("Ada");
        firstStudent.setLastName("Lovelace");
        firstStudent.setCohort("Java-May-1845");

        Student secondStudent = new Student("0002");
        secondStudent.setFirstName("Charles");
        secondStudent.setLastName("Babbage");
        secondStudent.setCohort(".NET-May-1845");

        testDao.addStudent(firstStudent.getStudentId(),firstStudent);
        testDao.addStudent(secondStudent.getStudentId(),secondStudent);

        //ACT
        //Remove Ada from list
        Student removedStudent = testDao.removeStudent(firstStudent.getStudentId());

        //ASSERT
        assertEquals(removedStudent,firstStudent,"The removed student should be Ada");

        List<Student> allStudents = testDao.getAllStudents();

        //General contents
        assertNotNull(allStudents,"All students list should be not null.");
        assertEquals(1,allStudents.size(),"All students should only have 1 student.");

        //Specifics
        assertFalse(allStudents.contains(firstStudent), "All students should NOT include Ada.");
        assertTrue(allStudents.contains(secondStudent),"All students should NOT include Charles");

        //Remove second student
        removedStudent = testDao.removeStudent(secondStudent.getStudentId());

        assertEquals(removedStudent, secondStudent, "The removed student should be Charles");

        allStudents = testDao.getAllStudents();

        assertTrue(allStudents.isEmpty(), "The retrieved list of students should be empty.");

        Student retrievedStudent = testDao.getStudent(firstStudent.getStudentId());

        assertNull(retrievedStudent, "Ada was removed, should be null");

        retrievedStudent = testDao.getStudent(secondStudent.getStudentId());

        assertNull(retrievedStudent, "Charles was removed, should be null.");




    }



}