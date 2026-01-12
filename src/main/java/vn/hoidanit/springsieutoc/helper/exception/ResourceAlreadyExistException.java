package vn.hoidanit.springsieutoc.helper.exception;

public class ResourceAlreadyExistException extends RuntimeException{
    private static final Long serialVersionUID = 1L;

    public ResourceAlreadyExistException(String message){
        super(message);
    }
}
