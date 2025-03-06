package com.local.MySchedule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.local.MySchedule.dao")
public class MyScheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyScheduleApplication.class, args);
	}

}
