package com.youpassed.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Major {
	private Integer id;
	private String title;
	private List<Exam> exams;
	private int capacity;
	private int applicants;
}
