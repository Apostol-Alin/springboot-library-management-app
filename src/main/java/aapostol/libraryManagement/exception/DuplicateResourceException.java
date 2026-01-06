package aapostol.libraryManagement.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super("A resource with the same name already exists: " + message);
    }
}