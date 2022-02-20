package com.Weather365.userhistory.service;

import com.Weather365.userhistory.model.radarRequest;
import com.Weather365.userhistory.repository.userHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class userHistoryService {

    @Autowired
    private userHistoryRepository repository;

    public void saveHistory(radarRequest data){
        this.repository.save(data);
    }

    public List<radarRequest> getHistory(int userId){
        return this.repository.findAll().stream()
                .filter(x->x.getUserId() == userId)
                .collect(Collectors.toList());
    }
}