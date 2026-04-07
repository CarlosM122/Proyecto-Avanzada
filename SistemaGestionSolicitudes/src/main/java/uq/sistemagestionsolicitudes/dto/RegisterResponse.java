package uq.sistemagestionsolicitudes.dto;

public class RegisterResponse {
    private String correo;

    public String getCorreo() {
        return correo;
    }

    public RegisterResponse(String correo) {
        this.correo = correo;
    }
}
