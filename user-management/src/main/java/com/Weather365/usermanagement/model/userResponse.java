package com.Weather365.usermanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class userResponse {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String emailId;
}
