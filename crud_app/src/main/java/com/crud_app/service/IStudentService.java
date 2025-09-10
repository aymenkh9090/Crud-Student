package com.crud_app.service;

import com.crud_app.model.Student;

import java.util.List;

public interface IStudentService {
    List<Student> getAllStudent();
    Student saveStudent(Student student);
    Student updateStudent( Student studentDto);
    void deleteStudent(Long id);
    Student getById(Long id);

}
