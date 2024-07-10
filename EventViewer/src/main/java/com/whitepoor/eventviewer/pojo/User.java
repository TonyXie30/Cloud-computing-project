package com.whitepoor.eventviewer.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "user")
@Table(schema = "public")
@Getter
@Setter
@Schema
public class User {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(unique = true)
    String username;

    /**
     * 如果用具体类ArrayList接收会报错，但用List接口不会。
     * 原因似乎出自Hibernate的PersistenctBag实现了List接口，故向上转型可行，
     * 但直接强制转型为ArrayList非法。类似于LinkedList和ArrayList互转是非法的。
     */
    @ManyToMany
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinTable(
            name = "joined_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    List<Event> joinedEvents;

}
