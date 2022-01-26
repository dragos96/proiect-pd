package com.example.demo.dto;

public class CommentDto {

	
	private String text;
	private Long courseId;
	
	public CommentDto() {
	}

	public CommentDto(String text, Long courseId) {
		this.text = text;
		this.courseId = courseId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	@Override
	public String toString() {
		return "CommentDto [text=" + text + ", courseId=" + courseId + "]";
	}
	
	
	
	
	
}
