package tn.esprit.microservice3.Services.Interfaces;

import tn.esprit.microservice3.Entities.Action;

import java.util.List;
import java.util.Optional;

public interface ActionService {
    Action addAction(Action action);
    List<Action> getAllActions();
    Optional<Action> getActionById(Long id);
    Action updateAction(Long id, Action action);
    void deleteAction(Long id);
}
