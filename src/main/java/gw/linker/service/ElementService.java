package gw.linker.service;

import gw.linker.entity.Element;

import java.util.List;

public interface ElementService {

    Element save(Element contact);

    List<Element> findAll();
}
