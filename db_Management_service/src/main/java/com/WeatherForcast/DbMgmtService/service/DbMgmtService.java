package com.WeatherForcast.DbMgmtService.service;

import com.WeatherForcast.DbMgmtService.model.NexradModel;
import com.WeatherForcast.DbMgmtService.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbMgmtService {

    @Autowired
    private UrlRepository repository;

    //service to save the given url with an auto-generated id
    public NexradModel saveUrl(NexradModel urlModel){
        //check if the data is already available in the database
        List<NexradModel> modelList = this.repository.findByUrl(urlModel.getUrl());

        return modelList.isEmpty() ? this.repository.save(urlModel) : modelList.get(0);
    }

    //service that returns the url for the given id
    public NexradModel getUrl(Integer id){
        return this.repository.getById(id);
    }
}
