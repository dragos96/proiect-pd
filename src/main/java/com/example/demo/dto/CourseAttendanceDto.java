package com.example.demo.dto;

public class CourseAttendanceDto {

	private String email;
	private Long courseId;
	
	public CourseAttendanceDto() {
	}
	
	
	
	public CourseAttendanceDto(String email, Long courseId) {
		this.email = email;
		this.courseId = courseId;
	}



	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	
	
	
}
