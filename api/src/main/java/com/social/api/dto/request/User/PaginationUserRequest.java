package com.social.api.dto.request.User;

import com.social.api.dto.request.BasePaginationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaginationUserRequest extends BasePaginationRequest{
    private String email;
    private String username;
    private String address;
    private String phoneNumber;
}
