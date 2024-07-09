package com.whitepoor.eventviewer.api;

import com.whitepoor.eventviewer.pojo.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long>{
    Event findEventById(Long id);

    @Query("select e from Event e where e.startTime >= ?1 and e.endTime <= ?2")
    List<Event> findEventByTimeInterval(Timestamp startTime, Timestamp endTime);

    @Query("select e from Event e where e.startTime >= ?1")
    List<Event> findEventByStartTime(Timestamp startTime);

    @Query("select e from Event e where e.endTime <= ?1")
    List<Event> findEventByEndTime(Timestamp endTime);

    @Query(value = "SELECT COUNT(*) FROM joined_events WHERE event_id = ?1", nativeQuery = true)
    int getAmountOfJoinedUsers(Long eventId);

    @Query(value ="""
            select je.event_id from Event e join joined_events je on e.id = je.event_id where je.user_id = ?1""", nativeQuery = true)
    List<Long> findEventByUser(Long userId);

    @Query(value = """
            WITH max_id AS (
                SELECT MAX(id) AS max_id FROM event
            )
            SELECT * FROM event AS t1
            WHERE t1.id >= (
                SELECT FLOOR(RANDOM() * max_id) FROM max_id
            ) and t1.start_time >= current_timestamp
            ORDER BY t1.id asc
            LIMIT ?1 ;
            """,
            nativeQuery = true)
    List<Event> getRandomEvent(Integer limit);
}
