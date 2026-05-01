package tn.esprit.microservice3.Services.Interfaces;

import tn.esprit.microservice3.DTO.FactureRequestDTO;
import tn.esprit.microservice3.Entities.Facture;

import java.util.List;

public interface FactureService {
    Facture createFacture(FactureRequestDTO request);
    List<Facture> getAllFactures();
    Facture getFactureById(Long id);
    void deleteFacture(Long id);
}
