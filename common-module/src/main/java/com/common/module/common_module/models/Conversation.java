package com.common.module.common_module.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "conversation")
@Data
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "conversations")
    private Set<Person> participants = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "conversation")
    private List<Message> messageList = new ArrayList<>();
}
