package by.intexsoft.diplom.common.model;

import by.intexsoft.diplom.common.model.enums.OperationStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
@Data
public class OperationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private PersonModel personModel;
    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private CardModel cardModel;
    private double amount;
    private LocalDateTime created_at;
    @NotNull(message = "status can't be empty")
    private OperationStatusEnum status;
}
