package com.crud_app;

import com.crud_app.model.Student;
import com.crud_app.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CrudAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudAppApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner (StudentRepository studentRepository) {
		return args -> {
		studentRepository.save(Student.builder()
						.name("aymen")
						.email("aymen@gmail.com")
						.adress("mhamdia")
						.phone("12345")
				.build());
		studentRepository.save(Student.builder()
						.name("wajdi")
						.email("wajdi@gmail.com")
						.adress("mourouj")
						.phone("12345")
				.build());
		studentRepository.save(Student.builder()
						.name("marwen")
						.email("marwen@gmail.com")
						.adress("fouchena")
						.phone("12345")
				.build());

		};
	}

}
