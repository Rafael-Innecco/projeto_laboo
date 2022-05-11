package br.usp.poli.comp.labpoo2022;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ProjetoLabPoo2022Application {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoLabPoo2022Application.class, args);
	}

}
