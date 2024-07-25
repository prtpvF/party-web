package by.intexsoft.diplom.common.model;

import by.intexsoft.diplom.common.model.enums.FriendshipRequestStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendship_request")
@Data
public class FriendshipRequestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private PersonModel sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private PersonModel receiver;

    @NotNull
    private FriendshipRequestStatusEnum status;

    private LocalDateTime createdAt; //LOCALDATETIME
}
