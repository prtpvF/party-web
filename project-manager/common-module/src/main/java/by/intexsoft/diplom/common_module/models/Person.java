package by.intexsoft.diplom.common_module.models;

import by.intexsoft.diplom.common_module.models.Party.Party;
import by.intexsoft.diplom.common_module.models.roles.PersonRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "field can't be blank")
    private String username;
    @NotBlank(message = "field can't be blank")
    private String password;
    @NotBlank(message = "field can't be blank")
    @Email(message = " field must be email type") //todo add to DTO
    private String email;
    private boolean isActive;
    @Min(value = 14, message = "you must be older than 14")
    @Max(value = 100, message = "entered age isn't correct")
    private int age;
    private double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private PersonRole role;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "person_parties",
            joinColumns = {@JoinColumn(name = "party_id"),     //укероротить
            }, inverseJoinColumns = {@JoinColumn(name = "id")}
    )
    private List<Party> parties = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    private List<ParticipationRequest> participationRequests = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    private List<Operation> paymentOperations = new ArrayList<>(); // change name

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "person_conversations",
            joinColumns = {@JoinColumn(name = "conversation_id")},
            inverseJoinColumns = {@JoinColumn(name = "id")}
    )
    private List<Conversation> conversations = new ArrayList<>();

    @OneToMany( fetch = FetchType.LAZY,mappedBy = "sender")
    private List<FriendshipRequest> sentFriendshipRequest = new ArrayList<>();

    @OneToMany( fetch = FetchType.LAZY,mappedBy = "receiver")
    private List<FriendshipRequest> receivedFriendshipRequest = new ArrayList<>();

    @Override
    public String toString() {
        return "Person{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createdAt=" + createdAt +
                ", role=" + role +
                '}';
    }
}
