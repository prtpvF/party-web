package by.intexsoft.diplom.common_module.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "PartyDeletingRequest")
@Data
public class PartyDeletingRequest {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "person_id")
        private Person organizer;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @OneToOne(cascade = CascadeType.REMOVE)
        @JoinColumn(name = "party_id", referencedColumnName = "id")
        private Party party;
}