package br.com.businesstec.download_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {
		"classpath:application.properties",
		"classpath:env.properties"
}, ignoreResourceNotFound = true)
public class DownloadServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DownloadServiceApplication.class, args);
	}

}

