package tn.esprit.microservice3.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FactureRequestDTO {
    private Long idPatient;
    private List<Long> actionIds;
}
