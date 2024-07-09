package com.whitepoor.eventviewer.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;

@Entity(name = "place")
@Table(schema = "public")
@Schema
@Getter
public class Place {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(unique = true)
    String name;

    @Column
    int maxLimit;


}
