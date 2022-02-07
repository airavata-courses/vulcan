package com.WeatherForcast.forecast_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class geometry {

    private String type;
    private List<ArrayList<ArrayList<Double>>> coordinates;
}
