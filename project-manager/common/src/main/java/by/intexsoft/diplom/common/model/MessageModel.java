package by.intexsoft.diplom.common.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Table(name = "Messages")
@Data
public class MessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private ConversationModel conversationModel;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private PersonModel sender;

    @NotBlank
    @Length(min = 1, max = 400, message = "length of the message must be minimum 1 and maximum 400")
    private String body;
    private LocalDateTime sendAt;
}
