package com.example.demo;

import java.util.Calendar;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.entities.Course;
import com.example.demo.entities.CourseAttendance;
import com.example.demo.entities.CourseItem;
import com.example.demo.entities.CourseType;
import com.example.demo.entities.ERole;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.CourseAttendanceRepository;
import com.example.demo.repositories.CourseItemRepository;
import com.example.demo.repositories.CourseRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(DemoApplication.class,
				args);
	}

	public static void main2(String[] args) {

		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(DemoApplication.class,
				args);
		configurableApplicationContext.getBean(UserRepository.class);

		UserRepository userRepository = configurableApplicationContext.getBean(UserRepository.class);
		RoleRepository roleRepository = configurableApplicationContext.getBean(RoleRepository.class);
		CourseRepository courseRepository = configurableApplicationContext.getBean(CourseRepository.class);
		CourseItemRepository courseItemRepository = configurableApplicationContext.getBean(CourseItemRepository.class);
		CourseAttendanceRepository courseAttendanceRepository = configurableApplicationContext
				.getBean(CourseAttendanceRepository.class);

		Role adminRole = new Role(ERole.ROLE_ADMIN);
		Role regularRole = new Role(ERole.ROLE_USER);
		Role moderatorRole = new Role(ERole.ROLE_MODERATOR);

		roleRepository.save(adminRole);
		roleRepository.save(regularRole);
		roleRepository.save(moderatorRole);

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = "password";
		String encodedPassword = passwordEncoder.encode(password);
		User user1 = new User("username", "email@email.com", encodedPassword);
		user1.setRoles(Set.of(adminRole));

		User user2 = new User("username2", "email2@email.com", encodedPassword);
		user2.setRoles(Set.of(adminRole, moderatorRole));

		User user3 = new User("username3", "email3@email.com", encodedPassword);
		user3.setRoles(Set.of(regularRole));

		User user4 = new User("username4", "email4@email.com", encodedPassword);
		user4.setRoles(Set.of(regularRole));

		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(user3);
		userRepository.save(user4);

		Course course1 = new Course("First course", user1);
		Course course2 = new Course("Second course", user1);
		Course course3 = new Course("Third course", user2);
		Course course4 = new Course("Fourth course", user1);

		courseRepository.save(course1);
		courseRepository.save(course2);
		courseRepository.save(course3);
		courseRepository.save(course4);

		CourseItem courseItem1 = new CourseItem("Title1", "text1", CourseType.HTML, course1);
		CourseItem courseItem2 = new CourseItem("Title2", "text2", CourseType.FILE, course2);
		CourseItem courseItem3 = new CourseItem("Title3", "text3", CourseType.HTML, course1);
		CourseItem courseItem4 = new CourseItem("Title4", "text4", CourseType.FILE, course1);

		courseItemRepository.save(courseItem1);
		courseItemRepository.save(courseItem2);
		courseItemRepository.save(courseItem3);
		courseItemRepository.save(courseItem4);

		Calendar calendar = Calendar.getInstance();

		CourseAttendance courseAttendance1 = new CourseAttendance(user3, course1, user1, calendar.getTime());
		CourseAttendance courseAttendance2 = new CourseAttendance(user4, course1, user1, calendar.getTime());
		CourseAttendance courseAttendance3 = new CourseAttendance(user3, course2, user1, calendar.getTime());
		CourseAttendance courseAttendance4 = new CourseAttendance(user3, course3, user2, calendar.getTime());

		courseAttendanceRepository.save(courseAttendance1);
		courseAttendanceRepository.save(courseAttendance2);
		courseAttendanceRepository.save(courseAttendance3);
		courseAttendanceRepository.save(courseAttendance4);
	}

}
