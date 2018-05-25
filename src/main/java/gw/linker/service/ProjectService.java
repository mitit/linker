package gw.linker.service;

import gw.linker.entity.Project;

import java.util.List;

public interface ProjectService {

    Project save(Project contact);

    List<Project> findAll();
}
