package by.intexsoft.diplom.common_module.model.Party;

import by.intexsoft.diplom.common_module.model.Image;
import by.intexsoft.diplom.common_module.model.ParticipationRequest;
import by.intexsoft.diplom.common_module.model.Person;
import by.intexsoft.diplom.common_module.model.role.PartyType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "party")
@Data
@RedisHash("Party")
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
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person organizer;
    @Min(value = 14, message = "your guests must be older than 14")
    private int ageRestriction;

    @Min(value = 0, message = "count of places must be minimum 0")
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
    @Min(value = 0, message = "value must be minimum 0")
    @Max(value = 5, message = "value must be maximum 5")
    private double minimalRating;
    @Nullable
    @Min(value = 0, message = "the minimal ticket cost is 0")
    @Max(value = 3000, message = "the maximal ticket cost is 3000")
    private double ticketCost;
    @NotNull(message = "filed can't be null")
    private LocalDateTime dateOfEvent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "parties")
    private List<Person> guests = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "party")
    private List<ParticipationRequest> participationRequests = new ArrayList<>();

    @OneToMany(mappedBy = "party")
    private Set<Image> images = new HashSet<>();
}
