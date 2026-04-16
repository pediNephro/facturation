package tn.esprit.microservice3.Services.Implementations;

import org.springframework.stereotype.Service;
import tn.esprit.microservice3.Entities.Action;
import tn.esprit.microservice3.Repositories.ActionRepository;
import tn.esprit.microservice3.Services.Interfaces.ActionService;

import java.util.List;
import java.util.Optional;

@Service
public class ActionServiceImpl implements ActionService {

    private final ActionRepository actionRepository;

    public ActionServiceImpl(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    @Override
    public Action addAction(Action action) {
        return actionRepository.save(action);
    }

    @Override
    public List<Action> getAllActions() {
        return actionRepository.findAll();
    }

    @Override
    public Optional<Action> getActionById(Long id) {
        return actionRepository.findById(id);
    }

    @Override
    public Action updateAction(Long id, Action action) {
        Action existing = actionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Action not found with id: " + id));
        existing.setNomAct(action.getNomAct());
        existing.setCategoryAct(action.getCategoryAct());
        existing.setPrixAct(action.getPrixAct());
        return actionRepository.save(existing);
    }

    @Override
    public void deleteAction(Long id) {
        Action existing = actionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Action not found with id: " + id));
        actionRepository.delete(existing);
    }
}
