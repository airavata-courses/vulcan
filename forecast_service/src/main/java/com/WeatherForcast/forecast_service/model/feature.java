package com.WeatherForcast.forecast_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class feature {
    private String type;
    private properties properties;
    private geometry geometry;
}

