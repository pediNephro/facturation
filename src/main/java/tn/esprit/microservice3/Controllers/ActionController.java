package tn.esprit.microservice3.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.microservice3.Entities.Action;
import tn.esprit.microservice3.Services.Interfaces.ActionService;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/actions")
public class ActionController {

    private final ActionService actionService;

    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    @PostMapping
    public ResponseEntity<Action> addAction(@RequestBody Action action) {
        return ResponseEntity.ok(actionService.addAction(action));
    }

    @GetMapping
    public ResponseEntity<List<Action>> getAllActions() {
        return ResponseEntity.ok(actionService.getAllActions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Action> getActionById(@PathVariable Long id) {
        return actionService.getActionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Action> updateAction(@PathVariable Long id, @RequestBody Action action) {
        return ResponseEntity.ok(actionService.updateAction(id, action));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAction(@PathVariable Long id) {
        actionService.deleteAction(id);
        return ResponseEntity.ok("Action deleted successfully");
    }
}
