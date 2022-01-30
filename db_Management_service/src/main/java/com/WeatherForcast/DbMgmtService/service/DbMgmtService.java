package com.WeatherForcast.DbMgmtService.service;

import com.WeatherForcast.DbMgmtService.model.UrlModel;
import com.WeatherForcast.DbMgmtService.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbMgmtService {

    @Autowired
    private UrlRepository repository;

    //service to save the given url with an auto-generated id
    public UrlModel saveUrl(UrlModel urlModel){
        //check if the data is already available in the database
        List<UrlModel> modelList = this.repository.findByUrl(urlModel.getUrl());

        return modelList.isEmpty() ? this.repository.save(urlModel) : modelList.get(0);
    }

    //service that returns the url for the given id
    public String getUrl(Integer id){
        return this.repository.getById(id).getUrl();
    }
}
