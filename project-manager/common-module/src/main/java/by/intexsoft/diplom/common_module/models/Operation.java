package by.intexsoft.diplom.common_module.models;

import by.intexsoft.diplom.common_module.models.enums.OperationStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
@Data
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
    private double amount;
    private LocalDateTime created_at;
    @NotNull(message = "status can't be empty")
    private OperationStatusEnum status;
}
