package com.whitepoor.eventviewer.controller;

import com.whitepoor.eventviewer.config.MyException;
import com.whitepoor.eventviewer.pojo.Event;
import com.whitepoor.eventviewer.pojo.User;
import com.whitepoor.eventviewer.service.EventService;
import com.whitepoor.eventviewer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;


    @PostMapping("/api/main/joinEvent")
    @ResponseBody
    public void joinEvent(String username, long eventId) {
        /*
         * @param username
         * @param eventId
         */
        User user = userService.getUser(username);
        if (user == null){
            user = new User();
            user.setUsername(username);
            userService.register(user);
        }
        userService.joinEvent(user, eventId);
    }

    @PostMapping("/api/main/leaveEvent")
    @ResponseBody
    public void leaveEvent(String username, long eventId) {
        /*
         * @param username
         * @param eventId
         */
        User user = userService.getUser(username);
        if (user == null){
            user = new User();
            user.setUsername(username);
            userService.register(user);
        }
        userService.leaveEvent(user, eventId);
    }

    @PostMapping("/api/main/getSelfEvents")
    @ResponseBody
    public List<Event> getSelfEvents(
            @RequestParam String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String start,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") String end) {
        /*
            * Select events that the user has joined
            * @param username
            * @param start
            * @param end
            * @return List<Event>
         */
        User user = userService.getUser(username);
        if (user == null){
            user = new User();
            user.setUsername(username);
            userService.register(user);
        }

        if (start == null && end == null) {
            return eventService.findEventByUser(user.getId());
        }
        Timestamp startTime = null, endTime = null;
        try {
            if (Objects.nonNull(start))
                startTime = Timestamp.valueOf(start);
            if (Objects.nonNull(end))
                endTime = Timestamp.valueOf(end);
        } catch (IllegalArgumentException e) {
            throw new MyException(5001, "Invalid time format");
        }
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime) && startTime.after(endTime)) {
            throw new MyException(5001, "Start time should be before end time");
        }
        List<Event> events = eventService.findEventByTimeInterval(startTime, endTime);
        List<Event> userEvents = eventService.findEventByUser(user.getId());
        // 取二者交集
        events.retainAll(userEvents);
        return events;
    }

}
