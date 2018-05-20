package gw.linker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Project {

    @Id
    @Column
    private int number;
    @OneToMany
    private List<Element> elements;

    private LocalDateTime beginDate;
    private LocalDateTime endTime;
    private String executor;
    private List<List<Integer>> adjacencyMatrix;
    private List<List<Integer>> emcMatrix;
    private String result;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}
