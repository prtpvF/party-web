package by.intexsoft.diplom.common_module.model;

import by.intexsoft.diplom.common_module.model.Party.Party;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name="images")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;
}
