package by.intexsoft.diplom.common.model.party;

import by.intexsoft.diplom.common.model.role.PartyTypeModel;
import by.intexsoft.diplom.common.model.status.PartyDraftStatusModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "party_draft")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyDraftModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String name;

        private int ageRestriction;

        private int countOfPlaces;

        private String description;

        private String city;

        private String address;

        private Double minimalRating;

        @OneToOne
        @JoinColumn(name = "status_id")
        private PartyDraftStatusModel status;

        private Double ticketCost;

        private LocalDateTime dateOfEvent;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;

        private boolean draftFlag = true;

        @OneToOne(mappedBy = "draft", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private PartyEntity party;
}
