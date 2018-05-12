package gw.linker.service;

import gw.linker.entity.ElementInProject;
import gw.linker.repository.ElementInProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElementInProjectServiceImpl implements ElementInProjectService {

    @Autowired
    private ElementInProjectRepository elementInProjectRepository;

    @Override
    public ElementInProject save(ElementInProject contact) {
        return elementInProjectRepository.save(contact);
    }

    @Override
    public List<ElementInProject> findAll() {
        return elementInProjectRepository.findAll();
    }
}
