package com.whitepoor.eventviewer.service;


import com.whitepoor.eventviewer.api.EventRepository;
import com.whitepoor.eventviewer.pojo.Event;
import com.whitepoor.eventviewer.pojo.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.whitepoor.eventviewer.utils.PageUtils.getPageRequest;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public Page<Event> findAllEvents(PageInfo info, String[] attr){
        PageRequest pageRequest = getPageRequest(info,attr);
        return eventRepository.findAll(pageRequest);
    }

    public List<Event> findEventByTimeInterval(Timestamp startTime, Timestamp endTime) {
        if (startTime == null) {
            return eventRepository.findEventByEndTime(endTime);
        }
        if (endTime == null) {
            return eventRepository.findEventByStartTime(startTime);
        }
        return eventRepository.findEventByTimeInterval(startTime, endTime);
    }

    public List<Event> findEventByUser(Long userId) {
        List<Long> eventIds =  eventRepository.findEventByUser(userId);
        List<Event> eventList = new ArrayList<>();
        for (Long id:eventIds){
            Event event = eventRepository.findEventById(id);
            eventList.add(event);
        }
        return eventList;
    }

    public List<Event> getRandomEvents(Integer num) {
        return eventRepository.getRandomEvent(num);
    }
}
