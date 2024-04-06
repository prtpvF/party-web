package com.by.chaplygin.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.annotation.Target;

@Entity
@Table(name="Report")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    private int id;
    @ManyToOne
    @JoinColumn(name = "person")
    private Person person;
    @JoinColumn(name = "party")
    private Party party;
    private String date;
    private String text;
}
