package uz.pdp.exception_handling;

public class InSufficientStockException extends RuntimeException {
    public InSufficientStockException(String message) {
        super(message);
    }
}
