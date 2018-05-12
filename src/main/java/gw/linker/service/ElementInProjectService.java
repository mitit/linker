package gw.linker.service;

import gw.linker.entity.ElementInProject;

import java.util.List;

public interface ElementInProjectService {

    ElementInProject save(ElementInProject contact);

    List<ElementInProject> findAll();
}
