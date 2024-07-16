package by.intexsoft.diplom.common_module.model;

import by.intexsoft.diplom.common_module.model.Party.Party;
import by.intexsoft.diplom.common_module.model.role.PersonRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "person")
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotBlank(message = "field can't be blank")
    @Column(name = "username",unique = true)
    private String username;
    @NotBlank(message = "field can't be blank")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role")
    private PersonRole role;
    @NotBlank
    @Length(min = 3, max = 15)
    private String city;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "person_friends",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Person> friends = new ArrayList<>();

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
