package by.intexsoft.diplom.common_module.models;

import by.intexsoft.diplom.common_module.models.enums.FriendshipRequestStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendship_request")
@Data
public class FriendshipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Person sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Person receiver;

    @NotNull
    private FriendshipRequestStatusEnum status;

    private LocalDateTime createdAt; //LOCALDATETIME
}
