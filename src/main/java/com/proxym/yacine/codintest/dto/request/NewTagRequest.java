package com.proxym.yacine.codintest.dto.request;

import com.proxym.yacine.codintest.model.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class NewTagRequest extends Auditable {
    private String name;
    private String description;
}
