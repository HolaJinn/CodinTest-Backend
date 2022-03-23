package com.proxym.yacine.codintest;

import com.proxym.yacine.codintest.model.ProgrammingLanguage;
import com.proxym.yacine.codintest.model.Role;
import com.proxym.yacine.codintest.model.Tag;
import com.proxym.yacine.codintest.repository.ProgrammingLanguageRepository;
import com.proxym.yacine.codintest.repository.RoleRepository;
import com.proxym.yacine.codintest.repository.TagRepository;
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

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private ProgrammingLanguageRepository programmingLanguageRepository;

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

		ProgrammingLanguage java = new ProgrammingLanguage(null, "JAVA");
		ProgrammingLanguage cpp = new ProgrammingLanguage(null, "C++");

		if (!programmingLanguageRepository.existsById(1)) {
			programmingLanguageRepository.save(java);
		}
		if (!programmingLanguageRepository.existsById(2)) {
			programmingLanguageRepository.save(cpp);
		}

		Tag string = new Tag(null, "String", "This tag is for string type of exercises");
		Tag array = new Tag(null, "Array", "This tag is for array type of exercises");
		if (!tagRepository.existsById(1)) {
			tagRepository.save(string);
		}
		if (!tagRepository.existsById(2)) {
			tagRepository.save(array);
		}

	}
}
