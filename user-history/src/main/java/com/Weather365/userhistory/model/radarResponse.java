package com.Weather365.userhistory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class radarResponse {
    private String status;
    private String message;
    private List<radarRequest> data;
}
