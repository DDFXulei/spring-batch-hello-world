package com.josephwork.springbatchhelloworld.entity;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component("dbJdbcWriter")
public class DbJdbcWriter implements ItemWriter<Todo> {

	@Override
	public void write(List<? extends Todo> items) throws Exception {

		for(Todo todo: items) {
			System.out.println(todo);
		}
		
	}

	
	
}
