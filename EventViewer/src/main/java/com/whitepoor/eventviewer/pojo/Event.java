package com.whitepoor.eventviewer.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Entity
public class Event {

    @Id
    @Column(unique = true)
    @GeneratedValue
    long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "place_id")
    Place place;

    @Setter
    @Getter
    @Column
    Integer limits;

    /**
     * TODO: Is this ok?
     */
    @Setter
    @Column
    Timestamp startTime;
    @Setter
    @Column
    Timestamp endTime;

    @Setter
    @Column
    String description;

    @Setter
    @Column
    String picture;

    @Setter
    @Column
    String name;

}
