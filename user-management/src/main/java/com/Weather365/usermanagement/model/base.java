package com.Weather365.usermanagement.model;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name ="tbl_user")
public class base {
    @Id
    @GeneratedValue
    private Integer userId;
}
