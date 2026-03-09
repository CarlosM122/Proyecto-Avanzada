package uq.sistemagestionsolicitudes.exception;

public class InvalidStateException extends RuntimeException{
    public InvalidStateException(String mensaje){
        super(mensaje);
    }
}
