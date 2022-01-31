package com.WeatherForcast.DbMgmtService.repository;

import com.WeatherForcast.DbMgmtService.model.NexradModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlRepository extends JpaRepository<NexradModel, Integer> {
    List<NexradModel> findByUrl(String url);
}
