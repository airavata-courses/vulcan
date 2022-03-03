package com.Weather365.userhistory.repository;

import com.Weather365.userhistory.model.radarRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface userHistoryRepository extends JpaRepository<radarRequest, Integer> {

    @Override
    List<radarRequest> findAllById(Iterable<Integer> integers);
}
