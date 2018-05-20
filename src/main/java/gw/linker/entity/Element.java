package gw.linker.entity;



import javax.persistence.*;

@Entity
@Table(name = "element_in_project")
public class Element {

    @Id
    @Column
    private int numberInProject;
    @ManyToOne
    private Project project;
    @Column
    private int pcbInProjectNr;
    @Column
    private String label;
    @Column
    private double length;
    @Column
    private double width;
    @Column
    private String type;

    public int getNumberInProject() {
        return numberInProject;
    }

    public void setNumberInProject(int numberInProject) {
        this.numberInProject = numberInProject;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getPcbInProjectNr() {
        return pcbInProjectNr;
    }

    public void setPcbInProjectNr(int pcbInProjectNr) {
        this.pcbInProjectNr = pcbInProjectNr;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
