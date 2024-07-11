package com.common.module.common_module.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "Messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Person sender;

    @NotBlank
    @Length(min = 1, max = 400, message = "length of the message must be minimum 1 and maximum 400")
    private String body;
    private LocalDateTime sendAt;
}
