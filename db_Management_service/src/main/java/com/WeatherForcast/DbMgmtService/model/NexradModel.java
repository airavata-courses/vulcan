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
@Table(name ="TBL_NEXRAD")
public class NexradModel extends BaseModel{
    public String date;
    public String month;
    public String year;
    public String station;
    public String time;
    private String url;
}
