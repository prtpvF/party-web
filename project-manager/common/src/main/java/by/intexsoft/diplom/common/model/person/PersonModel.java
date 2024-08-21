package by.intexsoft.diplom.common.model.person;

import by.intexsoft.diplom.common.model.conversation.ConversationModel;
import by.intexsoft.diplom.common.model.draft.PartyCreateDraftModel;
import by.intexsoft.diplom.common.model.draft.PartyUpdateDraftModel;
import by.intexsoft.diplom.common.model.party.PartyEntity;
import by.intexsoft.diplom.common.model.payment.PartyPaymentModel;
import by.intexsoft.diplom.common.model.request.FriendshipRequestModel;
import by.intexsoft.diplom.common.model.request.ParticipationRequestModel;
import by.intexsoft.diplom.common.model.role.PersonRoleModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;

        @NotBlank(message = "field can't be blank")
        @Column(name = "username",unique = true)
        private String username;

        @NotBlank(message = "field can't be blank")
        @Email(message = " field must be email type")
        @Column(name = "email", unique = true)
        private String email;

        private boolean isActive;

        @Min(value = 14, message = "you must be older than 14")
        @Max(value = 100, message = "entered age isn't correct")
        private int age;

        private double rating;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "role")
        @JsonBackReference
        private PersonRoleModel role;

        @NotBlank
        @Length(min = 3, max = 15)
        private String city;

        private String status;

        @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
        @JoinTable(
                name = "person_parties",
                joinColumns = {@JoinColumn(name = "party_id"),
                }, inverseJoinColumns = {@JoinColumn(name = "id")}
        )
        @JsonIdentityReference(alwaysAsId = true)
        private List<PartyEntity> parties = new ArrayList<>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
        @JsonManagedReference
        private List<ParticipationRequestModel> participationRequests = new ArrayList<>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
        @JsonIdentityReference(alwaysAsId = true)
        private List<PartyPaymentModel> paymentOperations = new ArrayList<>(); // change name

        @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
        @JoinTable(
                name = "person_conversations",
                joinColumns = {@JoinColumn(name = "conversation_id")},
                inverseJoinColumns = {@JoinColumn(name = "id")}
        )
        @JsonIdentityReference(alwaysAsId = true)
        private List<ConversationModel> conversations = new ArrayList<>();

        @OneToMany( fetch = FetchType.LAZY,mappedBy = "sender")
        @JsonIdentityReference(alwaysAsId = true)
        private List<FriendshipRequestModel> sentFriendshipRequests = new ArrayList<>();

        @OneToMany( fetch = FetchType.LAZY,mappedBy = "receiver")
        @JsonIdentityReference(alwaysAsId = true)
        private List<FriendshipRequestModel> receivedFriendshipRequests = new ArrayList<>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.REMOVE)
        private List<PartyUpdateDraftModel> partyUpdatingDrafts = new ArrayList<>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
        private List<PartyCreateDraftModel> partyCreatingDrafts = new ArrayList<>();

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "person_friends",
                joinColumns = @JoinColumn(name = "person_id"),
                inverseJoinColumns = @JoinColumn(name = "friend_id")
        )
        private List<PersonModel> friends = new ArrayList<>();

        @Override
        public String toString() {
            return "Person{" +
                    "id='" + id + '\''+
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    ", age=" + age +
                    ", createdAt=" + createdAt +
                    '}';
        }
}