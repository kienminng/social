package com.social.api.dto.request.post;


import java.util.List;

import com.social.api.entity.Img;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostRequest {
    private String content;
    private List<Img> imgs;
}
