package by.intexsoft.diplom.common_module.model;

import by.intexsoft.diplom.common_module.model.role.PartyType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "party")
@Data
public class Party {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @NotBlank(message = "field can't be empty")
        private String name;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "type_id")
        private PartyType type;

        @NotNull
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "person_id")
        private Person organizer;

        private int ageRestriction;

        private int countOfPlaces;

        @Length(max = 255, message = "length must be shorter than 256")
        private String description;

        @NotBlank(message = "field can't be empty")
        @Length(min = 2, max = 20, message = "city name must be longer than 1 and shorter than 20 characters")
        private String city;

        @Nullable
        @Length(min = 5, max = 20, message = "field must be longer than 4 and shorter than 21")
        @NotBlank(message = "field can't be empty")
        private String address;

        @Nullable
        @Max(value = 5, message = "value must be maximum 5")
        private double minimalRating;

        @Nullable
        private double ticketCost;

        private LocalDateTime dateOfEvent;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;

        @OneToOne(mappedBy = "party", fetch = FetchType.LAZY)
        private DeletingPartyRequest request;

        @ManyToMany(fetch = FetchType.LAZY, mappedBy = "parties")
        private List<Person> guests = new ArrayList<>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "party")
        private List<ParticipationRequest> participationRequests = new ArrayList<>();

        @OneToMany(mappedBy = "party")
        private Set<Image> images = new HashSet<>();

        @Override
        public String toString() {
            return "Party{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", organizer=" + organizer +
                    ", ageRestriction=" + ageRestriction +
                    ", countOfPlaces=" + countOfPlaces +
                    ", description='" + description + '\'' +
                    ", city='" + city + '\'' +
                    ", address='" + address + '\'' +
                    ", minimalRating=" + minimalRating +
                    ", ticketCost=" + ticketCost +
                    ", dateOfEvent=" + dateOfEvent +
                    ", createdAt=" + createdAt +
                    ", updatedAt=" + updatedAt +
                    ", guests=" + guests +
                    ", participationRequests=" + participationRequests +
                    ", images=" + images +
                    '}';
        }
}
