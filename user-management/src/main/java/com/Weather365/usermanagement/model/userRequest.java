package com.Weather365.usermanagement.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class userRequest extends base{

    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
}
