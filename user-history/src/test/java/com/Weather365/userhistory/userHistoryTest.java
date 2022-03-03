package com.Weather365.userhistory;

import com.Weather365.userhistory.model.radarRequest;
import com.Weather365.userhistory.repository.userHistoryRepository;
import com.Weather365.userhistory.service.userHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class userHistoryTest {

    private userHistoryService service;
    private userHistoryRepository repository;

    @BeforeEach
    public void setup(){
        repository = mock(userHistoryRepository.class);
        service = new userHistoryService(repository);
    }

    @Test
    public void saveValidUserHistoryTest(){

        radarRequest request = new radarRequest(
                0,
                38,
                "06",
                "May",
                "2006",
                "KCCX",
                "12PM"
        );

        when(this.repository.save(request)).thenReturn(request);

        var result = (int)this.service.saveHistory(request);

        assertEquals(result , request.getId());

    }

    @Test
    public void getValidUserHistoryTest(){

        radarRequest request = new radarRequest(
                0,
                38,
                "06",
                "May",
                "2006",
                "KCCX",
                "12PM"
        );

        Integer userId = request.getUserId();

        when(this.repository.findAll().stream()
                        .filter(x->x.getUserId() == userId)
                        .collect(Collectors.toList()))
                .thenReturn(new ArrayList<radarRequest>() {{
                    add(request);
                }});

        var result = this.service.getHistory(userId);

        assertEquals(result.get(0) , request);

    }

    @Test
    public void getInValidUserHistoryTest(){

        Integer userId = 39;

        when(this.repository.findAll().stream()
                .filter(x->x.getUserId() == userId)
                .collect(Collectors.toList()))
                .thenReturn(new ArrayList<radarRequest>() {});

        var result = this.service.getHistory(userId);

        assertEquals(result.size() , 0);

    }
}
