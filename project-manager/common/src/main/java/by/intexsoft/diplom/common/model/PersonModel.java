package by.intexsoft.diplom.common.model;

import by.intexsoft.diplom.common.model.role.PersonRole;
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
    @Length(min = 5, message = "password must be longer than 5 characters")
    private String password;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role")
    private PersonRole role;

    @NotBlank
    @Length(min = 3, max = 15)
    private String city;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "person_parties",
            joinColumns = {@JoinColumn(name = "party_id"),
            }, inverseJoinColumns = {@JoinColumn(name = "id")}
    )
    @JsonIdentityReference(alwaysAsId = true)
    private List<PartyModel> parties = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    @JsonManagedReference
    private List<ParticipationRequestModel> participationRequestModels = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    @JsonIdentityReference(alwaysAsId = true)
    private List<OperationModel> paymentOperationModels = new ArrayList<>(); // change name

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "person_conversations",
            joinColumns = {@JoinColumn(name = "conversation_id")},
            inverseJoinColumns = {@JoinColumn(name = "id")}
    )
    @JsonIdentityReference(alwaysAsId = true)
    private List<ConversationModel> conversationModels = new ArrayList<>();

    @OneToMany( fetch = FetchType.LAZY,mappedBy = "sender")
    @JsonIdentityReference(alwaysAsId = true)
    private List<FriendshipRequestModel> sentFriendshipRequestModel = new ArrayList<>();

    @OneToMany( fetch = FetchType.LAZY,mappedBy = "receiver")
    @JsonIdentityReference(alwaysAsId = true)
    private List<FriendshipRequestModel> receivedFriendshipRequestModel = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "person_friends",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<PersonModel> friends = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organizer")
    @JsonIdentityReference(alwaysAsId = true)
    private List<DeletingPartyRequestMode> partyDeletingRequests = new ArrayList<>();

    @Override
    public String toString() {
        return "Person{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createdAt=" + createdAt +
                '}';
    }
}
