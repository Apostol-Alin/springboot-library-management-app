package aapostol.libraryManagement.exception;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super("Business rule violation: " + message);
    }
}
