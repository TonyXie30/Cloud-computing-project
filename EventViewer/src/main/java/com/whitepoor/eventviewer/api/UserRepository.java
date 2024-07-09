package com.whitepoor.eventviewer.api;

import com.whitepoor.eventviewer.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

    @Modifying
    @Query(value = "insert into public.joined_events (user_id, event_id) values (?1, ?2)", nativeQuery = true)
    void joinEvent(long id, long eventId);

    @Modifying
    @Query(value = "delete from public.joined_events where user_id = ?1 and event_id = ?2", nativeQuery = true)
    void leaveEvent(long id, long eventId);

    @Query(value = "select event_id from public.joined_events where user_id = ?1 ", nativeQuery = true)
    List<Long> getJoinedEvents(long id);
}
