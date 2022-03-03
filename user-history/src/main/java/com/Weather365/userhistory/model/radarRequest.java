package com.Weather365.userhistory.model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tbl_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class radarRequest extends tokenRequest{

    @Id
    @GeneratedValue
    private int Id;

    private int userId;

    private String date;

    private String month;

    private String year;

    private String station;

    private String time;
}
