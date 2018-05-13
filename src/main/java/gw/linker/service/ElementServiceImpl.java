package gw.linker.service;

import gw.linker.entity.Element;
import gw.linker.repository.ElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElementServiceImpl implements ElementService {

    @Autowired
    private ElementRepository elementRepository;

    @Override
    public Element save(Element contact) {
        return elementRepository.save(contact);
    }

    @Override
    public List<Element> findAll() {
        return elementRepository.findAll();
    }
}
