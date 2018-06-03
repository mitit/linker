package gw.linker.service;

import gw.linker.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Project save(Project contact);

    List<Project> findAll();

    Optional<Project> find(String name);

    void setCurrentProject(Project project);

    Project getCurrentProject();
}
