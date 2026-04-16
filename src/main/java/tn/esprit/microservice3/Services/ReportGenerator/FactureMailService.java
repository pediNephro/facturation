package tn.esprit.microservice3.Services.ReportGenerator;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.esprit.microservice3.Entities.Facture;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class FactureMailService {

    private final JavaMailSender mailSender;

    public FactureMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public byte[] generatePdfBytes(Facture facture) {
        try {
            String html = buildFactureHtml(facture);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la génération du PDF de la facture", e);
        }
    }

    public void generatePdfAndSendByEmail(Facture facture) {
        try {
            byte[] pdfBytes = generatePdfBytes(facture);
            String fileName = "facture_" + facture.getId() + ".pdf";
            sendEmailWithAttachment(facture.getPatient().getEmail(), pdfBytes, fileName, facture);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'envoi email de la facture", e);
        }
    }

    private void sendEmailWithAttachment(String to, byte[] pdfBytes, String fileName, Facture facture) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Votre facture - Nephro System");
        helper.setText(
                "Bonjour " + facture.getPatient().getFirstName() + ",\n\n" +
                        "Veuillez trouver ci-joint votre facture.\n\n" +
                        "Cordialement,\nNephro System"
        );
        helper.addAttachment(fileName, new ByteArrayResource(pdfBytes));
        mailSender.send(message);
    }

    private String buildFactureHtml(Facture facture) {
        String patientName = facture.getPatient().getFirstName() + " " + facture.getPatient().getLastName();
        String email = facture.getPatient().getEmail();
        String date = facture.getDateCreation() != null
                ? facture.getDateCreation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : "";

        return """
                <html>
                <head>
                    <meta charset="UTF-8"/>
                    <style>
                        body { font-family: Arial, sans-serif; padding: 30px; color: #222; }
                        .container { border: 1px solid #ddd; border-radius: 12px; padding: 24px; }
                        h1 { color: #2563eb; margin-bottom: 20px; }
                        .info { margin-bottom: 20px; line-height: 1.8; }
                        table { width: 100%%; border-collapse: collapse; margin-top: 20px; }
                        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
                        th { background-color: #f3f4f6; }
                        .total { margin-top: 20px; font-size: 18px; font-weight: bold; text-align: right; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>Facture</h1>
                        <div class="info">
                            <p><strong>ID Facture :</strong> %d</p>
                            <p><strong>Patient :</strong> %s</p>
                            <p><strong>Email :</strong> %s</p>
                            <p><strong>Date :</strong> %s</p>
                        </div>
                        <table>
                            <thead><tr><th>Acte</th><th>Prix Total</th></tr></thead>
                            <tbody>
                                <tr><td>%s</td><td>%.2f TND</td></tr>
                            </tbody>
                        </table>
                        <div class="total">Total : %.2f TND</div>
                    </div>
                </body>
                </html>
                """.formatted(
                facture.getId(), patientName, email, date,
                facture.getNomAct(), facture.getPrixTotal(), facture.getPrixTotal()
        );
    }
}
