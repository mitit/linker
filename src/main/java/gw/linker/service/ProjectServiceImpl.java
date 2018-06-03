package gw.linker.service;

import gw.linker.entity.Project;
import gw.linker.repository.ProjectRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Getter
    @Setter
    private Project currentProject;

    @Override
    public Project save(Project contact) {
        return projectRepository.save(contact);
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Optional<Project> find(String name) {
        return projectRepository.findById(name);
    }
}
