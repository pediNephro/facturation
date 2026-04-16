package tn.esprit.microservice3.Controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.microservice3.DTO.FactureRequestDTO;
import tn.esprit.microservice3.Entities.Facture;
import tn.esprit.microservice3.Services.Interfaces.FactureService;
import tn.esprit.microservice3.Services.ReportGenerator.FactureMailService;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/factures")
public class FactureController {

    private final FactureService factureService;
    private final FactureMailService factureMailService;

    public FactureController(FactureService factureService, FactureMailService factureMailService) {
        this.factureService = factureService;
        this.factureMailService = factureMailService;
    }

    @PostMapping
    public ResponseEntity<Facture> createFacture(@RequestBody FactureRequestDTO request) {
        return ResponseEntity.ok(factureService.createFacture(request));
    }

    @GetMapping
    public ResponseEntity<List<Facture>> getAllFactures() {
        return ResponseEntity.ok(factureService.getAllFactures());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facture> getFactureById(@PathVariable Long id) {
        return ResponseEntity.ok(factureService.getFactureById(id));
    }

    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getFacturePdf(@PathVariable Long id,
                                                @RequestParam(defaultValue = "false") boolean download) {
        Facture facture = factureService.getFactureById(id);
        byte[] pdfBytes = factureMailService.generatePdfBytes(facture);

        String disposition = download
                ? "attachment; filename=facture_" + id + ".pdf"
                : "inline; filename=facture_" + id + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFacture(@PathVariable Long id) {
        factureService.deleteFacture(id);
        return ResponseEntity.ok("Facture supprimée avec succès");
    }
}
