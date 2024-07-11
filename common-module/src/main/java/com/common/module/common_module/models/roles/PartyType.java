package com.common.module.common_module.models.roles;

import com.common.module.common_module.models.Party.Party;
import com.common.module.common_module.models.enums.PartyTypeEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partyType")
@Data
public class PartyType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private PartyTypeEnum type;

    @OneToMany(mappedBy = "type")
    private List<Party> parties = new ArrayList<>();
}
