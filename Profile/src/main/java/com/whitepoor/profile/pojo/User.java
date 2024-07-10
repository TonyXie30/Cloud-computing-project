package com.whitepoor.profile.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "user")
@Table(schema = "public")
@Getter
@Setter
public class User {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(unique = true)
    String username;
    @Column
    String gender;
    @Column
    String subject;
    @Column
    int age;
    @Column
    String picture;
}
