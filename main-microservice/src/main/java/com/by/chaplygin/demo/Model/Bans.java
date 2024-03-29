package com.by.chaplygin.demo.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Table(name = "bans")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bans {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "person_id")
   private Person person;
    @ManyToOne
    @JoinColumn(name = "organizer_id")
   private Organizer organizer;
    @Column(name = "startOfBan")
    private String startOfBan;
    @Column(name = "endOfBan")
    private String  endOfBan;

    public Bans(Person person, Organizer organizer, String  startOfBan, String  endOfBan) {
        this.person = person;
        this.organizer = organizer;
        this.startOfBan = startOfBan;
        this.endOfBan = endOfBan;
    }
}
