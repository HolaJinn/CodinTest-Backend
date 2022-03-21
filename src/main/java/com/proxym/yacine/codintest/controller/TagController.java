package com.proxym.yacine.codintest.controller;

import com.proxym.yacine.codintest.dto.request.NewTagRequest;
import com.proxym.yacine.codintest.dto.response.TagResponse;
import com.proxym.yacine.codintest.service.TagService;
import com.proxym.yacine.codintest.util.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(Routes.tagsRoute)
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("")
    public ResponseEntity<List<TagResponse>> getAll() {
        List<TagResponse> tags = tagService.getAllTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getById(@PathVariable Long id) {
        TagResponse tag = tagService.findById(id);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @GetMapping(Routes.tagsByNameRoute)
    public ResponseEntity<TagResponse> getByName(@RequestParam String name) {
        TagResponse tag = tagService.findByName(name);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createTag(@RequestBody NewTagRequest newTagRequest) {
        tagService.createTag(newTagRequest);
        return new ResponseEntity<>("New tag is created successfully", HttpStatus.CREATED);
    }
}
