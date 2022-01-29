package com.WeatherForcast.DbMgmtService.repository;

import com.WeatherForcast.DbMgmtService.model.UrlModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlModel, Integer> {
}
