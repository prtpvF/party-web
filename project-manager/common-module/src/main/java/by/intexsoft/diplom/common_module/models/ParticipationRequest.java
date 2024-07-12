package by.intexsoft.diplom.common_module.models;

import by.intexsoft.diplom.common_module.models.Party.Party;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "participation_requests")
@Data
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;



}
