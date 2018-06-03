package gw.linker.exception;

public class ProjectNotFoundException extends IllegalArgumentException {
    @Override
    public String getMessage() {
        return "Project not found";
    }
}
