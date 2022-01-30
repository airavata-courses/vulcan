package com.WeatherForcast.DbMgmtService.repository;

import com.WeatherForcast.DbMgmtService.model.UrlModel;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlRepository extends JpaRepository<UrlModel, Integer> {
}
