package gw.linker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Project {

    @Id
    @Column
    private int number;
    @OneToMany
    private List<ElementInProject> elementInProjectList;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<ElementInProject> getElementInProjectList() {
        return elementInProjectList;
    }

    public void setElementInProjectList(List<ElementInProject> elementInProjectList) {
        this.elementInProjectList = elementInProjectList;
    }
}
