package com.whitepoor.eventviewer.controller;


import com.whitepoor.eventviewer.pojo.Event;
import com.whitepoor.eventviewer.pojo.PageInfo;
import com.whitepoor.eventviewer.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping("/api/showAllEvents")
    @ResponseBody
    @CrossOrigin
    public Page<Event> showAllEvents(@RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer limit,
                                     @RequestParam(required = false) String sort,
                                     @RequestParam(required = false) String[] attr) {
        /*
         * Show all events
         * @return all events
         */
        PageInfo info = new PageInfo(page,limit,sort);
        if (attr == null){
            attr = new String[]{"id"};
        }
        return eventService.findAllEvents(info,attr);
    }

    @PostMapping("/api/getRecommendEvents")
    @ResponseBody
    @CrossOrigin
    public List<Event> getRecommendEvents(@RequestParam(required = false, defaultValue = "1")
                                              Integer num){
        return eventService.getRandomEvents(num);
    }
}
