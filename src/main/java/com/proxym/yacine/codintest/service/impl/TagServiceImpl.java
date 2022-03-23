package com.proxym.yacine.codintest.service.impl;

import com.proxym.yacine.codintest.dto.request.NewTagRequest;
import com.proxym.yacine.codintest.dto.response.TagResponse;
import com.proxym.yacine.codintest.exception.CustomException;
import com.proxym.yacine.codintest.model.Tag;
import com.proxym.yacine.codintest.repository.TagRepository;
import com.proxym.yacine.codintest.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createTag(NewTagRequest newTagRequest) {
        if(repository.existsByName(newTagRequest.getName())) {
            throw new CustomException("This tag name exists already", "TAG EXISTS ALREADY", 400);
        }
        Tag tag = Tag.builder()
                .name(newTagRequest.getName())
                .description(newTagRequest.getDescription())
                .build();

        repository.save(tag);
    }

    @Override
    public List<TagResponse> getAllTags() {
        return repository.findAll().stream()
                .map(tag -> modelMapper.map(tag, TagResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public TagResponse findById(Integer id) {
        return modelMapper.map(repository.findById(id), TagResponse.class);
    }

    @Override
    public TagResponse findByName(String name) {
        return modelMapper.map(repository.findByName(name), TagResponse.class);
    }
}
