package com.WeatherForcast.DbMgmtService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="TBL_URL")
public class UrlModel {
    public UrlModel(String url) {
        this.url = url;
    }

    @Id
    @GeneratedValue
    private int id;
    private String url;
}
