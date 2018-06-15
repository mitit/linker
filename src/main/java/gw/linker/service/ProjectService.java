package gw.linker.service;

import gw.linker.entity.Project;
import gw.linker.entity.dto.AcoAlgorithmResultDto;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    Project save(Project contact);

    List<Project> findAll();

    Optional<Project> find(String name);

    void setCurrentProject(Project project);

    Project getCurrentProject();

    AcoAlgorithmResultDto getWorkResult();

    double getAlpha();

    double getBeta();

    double getCcc();

    double getDdd();

    double getPcbSquareKoeffParameter();

    void setAlpha(double parameter);

    void setBeta(double parameter);

    void setCcc(double parameter);

    void setDdd(double parameter);

    void setPcbSquareKoeffParameter(double parameter);
}
