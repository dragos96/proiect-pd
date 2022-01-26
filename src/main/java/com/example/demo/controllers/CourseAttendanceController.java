package com.example.demo.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CourseAttendanceDto;
import com.example.demo.entities.Course;
import com.example.demo.entities.CourseAttendance;
import com.example.demo.entities.ERole;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.entities.UserHelper;
import com.example.demo.repositories.CourseAttendanceRepository;
import com.example.demo.repositories.CourseRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;

@RestController
@RequestMapping("/api/courseAttendance")
public class CourseAttendanceController {
	
	@Autowired
	private UserHelper userHelper;
	
	@Autowired
	CourseAttendanceRepository courseAttendanceRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@GetMapping("/all-by-teacher/{courseId}")
	public ResponseEntity<List<User>> getAllForTeacher(Principal principal, @PathVariable("courseId") long courseId)  {
		
		
		Optional<Course> courseOp = this.courseRepository.findById(courseId);
		if(!courseOp.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Course course = courseOp.get();
		
		Optional<User>userOp = this.userHelper.getUserFromPrincipal(principal);
		if(!userOp.isPresent()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		
		
		List<CourseAttendance> courseAttendances = courseAttendanceRepository.findByAssignerIdAndCourseId(userOp.get().getId(), course.getId());
		List<User> students = new ArrayList<>();
		for(CourseAttendance c : courseAttendances) {
			students.add(this.userRepository.findById(c.getUser().getId()).get());
		}
		return new ResponseEntity<>(students, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<CourseAttendance>> getAll()  {
		
		List<CourseAttendance> courseAttendances = (List<CourseAttendance>) courseAttendanceRepository.findAll();
		return new ResponseEntity<>(courseAttendances, HttpStatus.OK);
	}
	
	@GetMapping("/course/{courseId}")
	public ResponseEntity<List<CourseAttendance>> getAllAttendanceByCourseId(@PathVariable Long courseId)  {
		
		List<CourseAttendance> courseAttendances = (List<CourseAttendance>) courseAttendanceRepository.findByCourseId(courseId);
		return new ResponseEntity<>(courseAttendances, HttpStatus.OK);
	}
	
	
	@PostMapping("/save/{courseId}/{studentId}")
	public ResponseEntity<CourseAttendance> saveCourseAttendance(@RequestBody CourseAttendance courseAttendance, Principal principal, @PathVariable("courseId") long courseId, @PathVariable("studentId") long studentId){
		
		Optional<User>userOp = this.userHelper.getUserFromPrincipal(principal);
		if(!userOp.isPresent()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		Optional<User>userOpStudent = this.userRepository.findById(studentId);
		if(!userOpStudent.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Role roleStudent = this.roleRepository.findByName(ERole.ROLE_USER).get();
		if(!userOpStudent.get().getRoles().contains(roleStudent)) {
			System.out.println("BAD REQUEST USER ROLE");
			return ResponseEntity.badRequest().build();
		}
		
		
		Optional<Course> courseOp = this.courseRepository.findById(courseId);
		if(!courseOp.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Course course = courseOp.get();
		User user = userOp.get();
		List<Course> userCourses = this.courseRepository.findByOwnerId(user.getId());
		if(!userCourses.contains(course)) {
			System.out.println("BAD REQUEST COURSE DOESN'T BELONG TO OWNER - Principal");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		courseAttendance.setCourse(course);
		courseAttendance.setUser(userOpStudent.get());
		courseAttendance.setAssigner(user);
		
		CourseAttendance courseAttendanceSaved = courseAttendanceRepository.save(courseAttendance);
		return ResponseEntity.ok(courseAttendanceSaved);
		
	}
	
	@PostMapping("/save")
	public ResponseEntity<CourseAttendance> saveCourseAttendance(@RequestBody CourseAttendanceDto courseAttendanceDto, Principal principal){
		
		Optional<User>userOp = this.userHelper.getUserFromPrincipal(principal);
		if(!userOp.isPresent()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		Optional<User>userOpStudent = this.userRepository.findByEmail(courseAttendanceDto.getEmail());
		if(!userOpStudent.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Role roleStudent = this.roleRepository.findByName(ERole.ROLE_USER).get();
		if(!userOpStudent.get().getRoles().contains(roleStudent)) {
			System.out.println("BAD REQUEST USER ROLE");
			return ResponseEntity.badRequest().build();
		}
		
		
		Optional<Course> courseOp = this.courseRepository.findById(courseAttendanceDto.getCourseId());
		if(!courseOp.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Course course = courseOp.get();
		User user = userOp.get();
		List<Course> userCourses = this.courseRepository.findByOwnerId(user.getId());
		if(!userCourses.contains(course)) {
			System.out.println("BAD REQUEST COURSE DOESN'T BELONG TO OWNER - Principal");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		CourseAttendance courseAttendance = new CourseAttendance();
		courseAttendance.setCourse(course);
		courseAttendance.setUser(userOpStudent.get());
		courseAttendance.setAssigner(user);
		
		CourseAttendance courseAttendanceSaved = courseAttendanceRepository.save(courseAttendance);
		return ResponseEntity.ok(courseAttendanceSaved);
		
	}
	
	@DeleteMapping("/delete/{courseId}")
	public ResponseEntity<CourseAttendance> deleteCourseAttendance(@PathVariable("courseId") Long courseId, Principal principal){
		
		Optional<CourseAttendance> opCA = courseAttendanceRepository.findById(courseId);
		if(!opCA.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		CourseAttendance ca = opCA.get();
		courseAttendanceRepository.delete(ca);
		return ResponseEntity.ok(ca);
		
	}

}
