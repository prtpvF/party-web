package by.intexsoft.diplom.common.model;

import by.intexsoft.diplom.common.model.role.PartyTypeModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @NotBlank(message = "field can't be empty")
        private String name;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "type_id")
        @JsonBackReference
        private PartyTypeModel type;

        @NotNull
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "person_id")
        private PersonModel organizer;

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

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "status_id", nullable = false)
        private PartyStatusModel status;

        @Nullable
        @Max(value = 5, message = "value must be maximum 5")
        private Double minimalRating;

        private Double averageRate;

        @Nullable
        private Double ticketCost;

        private LocalDateTime dateOfEvent;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;

        @OneToOne(mappedBy = "party", fetch = FetchType.LAZY,
                  cascade = CascadeType.REMOVE)
        private DeletingPartyRequestModel request;

        @ManyToMany(fetch = FetchType.LAZY, mappedBy = "parties")
        @JsonIdentityReference(alwaysAsId = true)
        private List<PersonModel> guests = new ArrayList<>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "party")
        private List<ParticipationRequestModel> participationRequests = new ArrayList<>();

        @OneToMany(mappedBy = "party")
        @JsonIdentityReference(alwaysAsId = true)
        private Set<ImageModel> images = new HashSet<>();

        @ElementCollection
        private List<Integer> rates = new ArrayList<>();

        public void addGuest(PersonModel guest){
                this.guests.add(guest);
        }

        public void addRate(int rate) {
                this.rates.add(rate);
        }



        @Override
        public String toString() {
            return "Party{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
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
                    '}';
        }
}
