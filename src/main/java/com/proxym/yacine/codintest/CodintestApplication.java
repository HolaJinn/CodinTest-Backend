package com.proxym.yacine.codintest;

import com.proxym.yacine.codintest.model.Role;
import com.proxym.yacine.codintest.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class CodintestApplication implements CommandLineRunner {



	@Autowired
	private RoleRepository roleRepository;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(CodintestApplication.class, args);
	}

//	@Bean
//	public BCryptPasswordEncoder passwordEncoder(){
//		return new BCryptPasswordEncoder() ;
//	}

	@Override
	public void run(String... args) throws Exception {

		Role superAdmin = new Role(null, "SUPERADMIN");
		Role owner = new Role(null, "OWNER");
		Role recruiter = new Role(null, "RECRUITER");
		Role candidate = new Role(null , "CANDIDATE");

		if (!roleRepository.existsById(1)){
			roleRepository.save(superAdmin);
		}
		if (!roleRepository.existsById(2)){
			roleRepository.save(owner);
		}
		if (!roleRepository.existsById(3)) {
			roleRepository.save(recruiter);
		}
		if (!roleRepository.existsById(4)) {
			roleRepository.save(candidate);
		}
	}
}
