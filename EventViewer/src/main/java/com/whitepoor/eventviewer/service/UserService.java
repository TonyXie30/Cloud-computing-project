package com.whitepoor.eventviewer.service;

import com.whitepoor.eventviewer.api.EventRepository;
import com.whitepoor.eventviewer.api.UserRepository;
import com.whitepoor.eventviewer.config.Code;
import com.whitepoor.eventviewer.config.MyException;
import com.whitepoor.eventviewer.pojo.Event;
import com.whitepoor.eventviewer.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static com.whitepoor.eventviewer.utils.PageUtils.getPageRequest;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    public User getUser(String name){
        return userRepository.findByUsername(name);
    }

    @Transactional
    public void joinEvent(User user, long eventId) {
        Event event = eventRepository.findEventById(eventId);
        if (event == null) {
            throw new MyException(Code.Event_not_found);
        }
        if (event.getStartTime().before(new Timestamp(System.currentTimeMillis()))){
            throw new MyException(Code.METHOD_FAILED);
        }

        Integer limit = event.getLimits();

        int joined = eventRepository.getAmountOfJoinedUsers(eventId);
        if (limit!=null&&joined >= limit) {
            throw new MyException(Code.Not_available);
        }
        if (userRepository.findByUsername(user.getUsername()) == null) {
            throw new MyException(Code.USER_NOT_EXIST);
        }
        if (userRepository.getJoinedEvents(user.getId()).contains(eventId)) {
            throw new MyException(Code.Already_joined_event);
        }

        else {
            userRepository.joinEvent(user.getId(), eventId);
        }



    }
    @Transactional
    public void leaveEvent(User user, long eventId) {
        Event event = eventRepository.findEventById(eventId);
        if (event == null) {
            throw new MyException(Code.Event_not_found);
        }
        if (userRepository.findByUsername(user.getUsername()) == null) {
            throw new MyException(Code.USER_NOT_EXIST);
        }
        userRepository.leaveEvent(user.getId(), eventId);
    }
    
}