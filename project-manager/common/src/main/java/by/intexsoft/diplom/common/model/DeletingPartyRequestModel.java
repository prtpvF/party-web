package by.intexsoft.diplom.common.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "PartyDeletingRequest")
@Data
public class DeletingPartyRequestModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "person_id")
        private PersonModel organizer;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @OneToOne(cascade = CascadeType.REMOVE)
        @JoinColumn(name = "party_id", referencedColumnName = "id")
        private PartyEntity party;
}