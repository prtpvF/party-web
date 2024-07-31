package by.intexsoft.diplom.common.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "conversation")
@Data
public class ConversationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "conversations")
    private Set<PersonModel> participants = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "conversation")
    private List<MessageModel> messagesList = new ArrayList<>();
}
