package com.proxym.yacine.codintest;

import com.proxym.yacine.codintest.model.ProgrammingLanguage;
import com.proxym.yacine.codintest.model.Role;
import com.proxym.yacine.codintest.model.Tag;
import com.proxym.yacine.codintest.repository.ProgrammingLanguageRepository;
import com.proxym.yacine.codintest.repository.RoleRepository;
import com.proxym.yacine.codintest.repository.TagRepository;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
		ModelMapper modelMapper = new ModelMapper();
		Provider<LocalDate> localDateProvider = new AbstractProvider<LocalDate>() {
			@Override
			protected LocalDate get() {
				return LocalDate.now();
			}
		};

		Converter<String, LocalDate> toStringDate = new AbstractConverter<String, LocalDate>() {
			@Override
			protected LocalDate convert(String source) {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate localDate = LocalDate.parse(source, format);
				return localDate;
			}
		};

		modelMapper.createTypeMap(String.class, LocalDate.class);
		modelMapper.addConverter(toStringDate);
		modelMapper.getTypeMap(String.class, LocalDate.class).setProvider(localDateProvider);
		return modelMapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(CodintestApplication.class, args);
	}

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

		ProgrammingLanguage java = new ProgrammingLanguage(null, "Java");
		ProgrammingLanguage cpp = new ProgrammingLanguage(null, "C++");
		ProgrammingLanguage javascript = new ProgrammingLanguage(null, "JavaScript");
		ProgrammingLanguage python = new ProgrammingLanguage(null, "Python");
		ProgrammingLanguage ruby = new ProgrammingLanguage(null, "Ruby");

		if (!programmingLanguageRepository.existsById(1)) {
			programmingLanguageRepository.save(java);
		}
		if (!programmingLanguageRepository.existsById(2)) {
			programmingLanguageRepository.save(cpp);
		}if (!programmingLanguageRepository.existsById(3)) {
			programmingLanguageRepository.save(javascript);
		}if (!programmingLanguageRepository.existsById(4)) {
			programmingLanguageRepository.save(python);
		}if (!programmingLanguageRepository.existsById(5)) {
			programmingLanguageRepository.save(ruby);
		}

		Tag string = new Tag(null, "String", "This tag is for string type of exercises");
		Tag array = new Tag(null, "Array", "This tag is for array type of exercises");
		Tag tree = new Tag(null, "Tree", "This tag is for tree type of exercises");
		Tag number = new Tag(null, "Number", "This tag is for number type of exercises");
		Tag algorithm = new Tag(null, "Algorithm", "This tag is for algorithm type of exercises");
		Tag dataStructure = new Tag(null, "Data Structure", "This tag is for data structure type of exercises");
		Tag hashList = new Tag(null, "Hash List", "This tag is for hash list type of exercises");
		Tag map = new Tag(null, "Map", "This tag is for map type of exercises");

		if (!tagRepository.existsById(1)) {
			tagRepository.save(string);
		}
		if (!tagRepository.existsById(2)) {
			tagRepository.save(array);
		}
		if (!tagRepository.existsById(3)) {
			tagRepository.save(tree);
		}
		if (!tagRepository.existsById(4)) {
			tagRepository.save(number);
		}
		if (!tagRepository.existsById(5)) {
			tagRepository.save(algorithm);
		}
		if (!tagRepository.existsById(6)) {
			tagRepository.save(dataStructure);
		}
		if (!tagRepository.existsById(7)) {
			tagRepository.save(hashList);
		}
		if (!tagRepository.existsById(8)) {
			tagRepository.save(map);
		}

	}
}
