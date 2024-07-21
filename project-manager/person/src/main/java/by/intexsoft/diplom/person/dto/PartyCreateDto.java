package by.intexsoft.diplom.person.dto;

import by.intexsoft.diplom.common_module.model.Image;
import by.intexsoft.diplom.common_module.model.Person;
import by.intexsoft.diplom.common_module.model.role.PartyType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartyCreateDto {
    private String name;
    private PartyType type;
    private int ageRestriction;
    private int countOfPlaces;
    private String description;
    private String city;
    private String address;
    private double minimalRating;
    private double ticketCost;
    private LocalDateTime dateOfEvent;
    private Set<Image> images = new HashSet<>();
}