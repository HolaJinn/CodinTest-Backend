package com.proxym.yacine.codintest.service;

import com.proxym.yacine.codintest.dto.request.NewTagRequest;
import com.proxym.yacine.codintest.dto.response.TagResponse;

import java.util.List;

public interface TagService {

    void createTag(NewTagRequest newTagRequest);
    List<TagResponse> getAllTags();
    TagResponse findById(Long id);
    TagResponse findByName(String name);
}
