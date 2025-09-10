package com.crud_app.service.impl;

import com.crud_app.model.Student;
import com.crud_app.repository.StudentRepository;
import com.crud_app.service.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements IStudentService{

    private final StudentRepository studentRepository;

    @Override
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    @Override
    public Student saveStudent(Student student) {
       if(studentRepository.existsByEmail(student.getEmail())){
           throw new RuntimeException("Email already exists");
       }
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Student studentDto) {
        return studentRepository.save(studentDto);
    }

   /* @Override
    public Student updateStudent(Long id, Student studentDto) {
        Student student =
                studentRepository.findById(id)
                        .orElseThrow(()->new RuntimeException("Student not found"));
        Student updatedStudent = Student.builder()
                .id(studentDto.getId())
                .name(studentDto.getName())
                .email(studentDto.getEmail())
                .adress(studentDto.getAdress())
                .phone(studentDto.getPhone())
                .build();
        return updatedStudent;
    }*/

    @Override
    public void deleteStudent(Long id) {

        Student student =
                studentRepository.findById(id)
                        .orElseThrow(()->new RuntimeException("Student not found"));
        studentRepository
                .delete(student);
    }

    @Override
    public Student getById(Long id) {
        Student student =
                studentRepository.findById(id)
                        .orElseThrow(()->new RuntimeException("Student not found"));
        return student;
    }




}
