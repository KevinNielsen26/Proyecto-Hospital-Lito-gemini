package com.example.hospital_lito;

import com.example.hospital_lito.repositorio.AsignacionRepository;
import com.example.hospital_lito.repositorio.CamaRepository;
import com.example.hospital_lito.repositorio.CategoriaRepository;
import com.example.hospital_lito.repositorio.EnfermeraRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HospitalLitoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalLitoApplication.class, args);
	}
}

