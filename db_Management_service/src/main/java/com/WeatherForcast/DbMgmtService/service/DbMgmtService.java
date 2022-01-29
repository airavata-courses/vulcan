package com.WeatherForcast.DbMgmtService.service;

import com.WeatherForcast.DbMgmtService.model.UrlModel;
import com.WeatherForcast.DbMgmtService.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbMgmtService {

    @Autowired
    private UrlRepository repository;

    public UrlModel saveUrl(UrlModel urlModel){
        return this.repository.save(urlModel);
    }
}
