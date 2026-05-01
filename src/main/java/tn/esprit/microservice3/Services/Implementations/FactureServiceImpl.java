package tn.esprit.microservice3.Services.Implementations;

import org.springframework.stereotype.Service;
import tn.esprit.microservice3.DTO.FactureRequestDTO;
import tn.esprit.microservice3.Entities.Action;
import tn.esprit.microservice3.Entities.Facture;
import tn.esprit.microservice3.Entities.User;
import tn.esprit.microservice3.Repositories.ActionRepository;
import tn.esprit.microservice3.Repositories.FactureRepository;
import tn.esprit.microservice3.Repositories.UserRepository;
import tn.esprit.microservice3.Services.Interfaces.FactureService;
import tn.esprit.microservice3.Services.ReportGenerator.FactureMailService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FactureServiceImpl implements FactureService {

    private final FactureRepository factureRepository;
    private final UserRepository userRepository;
    private final ActionRepository actionRepository;
    private final FactureMailService factureMailService;

    public FactureServiceImpl(FactureRepository factureRepository,
                              UserRepository userRepository,
                              ActionRepository actionRepository,
                              FactureMailService factureMailService) {
        this.factureRepository = factureRepository;
        this.userRepository = userRepository;
        this.actionRepository = actionRepository;
        this.factureMailService = factureMailService;
    }

    @Override
    public Facture createFacture(FactureRequestDTO request) {
        User patient = userRepository.findById(request.getIdPatient())
                .orElseThrow(() -> new RuntimeException("Patient introuvable avec id : " + request.getIdPatient()));

        if (request.getActionIds() == null || request.getActionIds().isEmpty()) {
            throw new RuntimeException("Vous devez sélectionner au moins un acte");
        }

        List<Action> actions = actionRepository.findAllById(request.getActionIds());

        if (actions.isEmpty()) {
            throw new RuntimeException("Aucun acte valide trouvé");
        }

        if (actions.size() != request.getActionIds().size()) {
            throw new RuntimeException("Un ou plusieurs actes sélectionnés sont introuvables");
        }

        String nomActConcatene = actions.stream()
                .map(Action::getNomAct)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        BigDecimal prixInitial = actions.stream()
                .map(Action::getPrixAct)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal prixFinal = prixInitial;

        if (patient.isAssurance()) {
            BigDecimal reduction = prixInitial.multiply(BigDecimal.valueOf(0.20));
            prixFinal = prixInitial.subtract(reduction);
        }

        prixFinal = prixFinal.setScale(2, RoundingMode.HALF_UP);

        Facture facture = Facture.builder()
                .patient(patient)
                .nomAct(nomActConcatene)
                .prixTotal(prixFinal)
                .dateCreation(LocalDateTime.now())
                .build();

        Facture savedFacture = factureRepository.save(facture);

        try {
            factureMailService.generatePdfAndSendByEmail(savedFacture);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedFacture;
    }

    @Override
    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    @Override
    public Facture getFactureById(Long id) {
        return factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable avec id : " + id));
    }

    @Override
    public void deleteFacture(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable avec id : " + id));
        factureRepository.delete(facture);
    }
}
