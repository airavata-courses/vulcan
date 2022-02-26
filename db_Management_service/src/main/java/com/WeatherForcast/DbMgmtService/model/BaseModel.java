package com.WeatherForcast.DbMgmtService.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name ="TBL_NEXRAD")
public abstract class BaseModel {
    @Id
    @GeneratedValue
    private int id;
}
