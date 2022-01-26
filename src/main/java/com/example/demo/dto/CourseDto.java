package com.example.demo.dto;

import com.example.demo.entities.Course;

public class CourseDto {

	private Long id;
	private String title;
	private String owner;
	
	public CourseDto() {
		
	}
	
	public CourseDto(Course course) {
		this.id = course.getId();
		this.title = course.getName();
		this.owner = course.getOwner() != null ? course.getOwner().getUsername() : null;
	}
	
	
	
	public CourseDto(Long id, String title, String owner) {
		this.id = id;
		this.title = title;
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "CourseDto [id=" + id + ", title=" + title + ", owner=" + owner + "]";
	}
	
	
	
}
