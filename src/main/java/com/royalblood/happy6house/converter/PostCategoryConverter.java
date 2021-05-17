package com.royalblood.happy6house.converter;

import com.royalblood.happy6house.domain.PostCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostCategoryConverter implements Converter<String, PostCategory> {
    @Override
    public PostCategory convert(String value) {
        return PostCategory.from(value.toUpperCase());
    }
}
