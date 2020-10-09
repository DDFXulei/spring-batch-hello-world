package com.josephwork.springbatchhelloworld.config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.SqlServerPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.josephwork.springbatchhelloworld.entity.Todo;

@Configuration
public class ItemReaderDbDemo {
	
	//generate task Object
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//Step exec tasks
	//generate step Object
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	@Qualifier("mssqlDataSource")
	private DataSource dataSource;

	@Autowired
	@Qualifier("dbJdbcWriter")
	private ItemWriter<? super Todo> dbJdbcWriter;
	
	@Bean
	public Job itemReaderDbDemoJob() {
		return jobBuilderFactory.get("itemReaderDbDemoJob").start(itemReaderDbStep()).build();
	}

	@Bean
	public Step itemReaderDbStep() {
		return stepBuilderFactory.get("itemReaderDbStep")
				.<Todo,Todo>chunk(2)
				.reader(dbJdbcReader())
				.writer(dbJdbcWriter)
				.build();
	}

	@Bean
	@StepScope
	public JdbcPagingItemReader<Todo> dbJdbcReader() {
		JdbcPagingItemReader<Todo> reader = new JdbcPagingItemReader<Todo>();
		reader.setDataSource(dataSource);
		reader.setFetchSize(2);
		reader.setRowMapper(new  RowMapper<Todo>() {
			@Override
			public Todo mapRow(ResultSet rs, int rowNum) throws SQLException {
				Todo todo = new Todo();
				todo.setId(rs.getLong(1));
				todo.setDescription(rs.getString(2));
				todo.setDetails(rs.getString(3));
				return todo;
			}	
			
		});
		SqlServerPagingQueryProvider provider = new  SqlServerPagingQueryProvider();
		provider.setSelectClause("id,description,details");
		provider.setFromClause("from dbo.todo");
		
		//sort
		Map<String,Order> sort = new HashMap<>(1);
		sort.put("id", Order.DESCENDING);
		provider.setSortKeys(sort);
		
		reader.setQueryProvider(provider);
		return reader;
	}
}






