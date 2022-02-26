package com.WeatherForcast.forecast_service.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class forecastModel {
    private float latitude;
    private float longitude;
    private float intensity;
}

