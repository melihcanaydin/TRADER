package main.java.tradingbot.dto;

public class TradeResponseDto {
    private String message;

    public TradeResponseDto() {
    }

    public TradeResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TradeResponseDto{" +
               "message='" + message + '\'' +
               '}';
    }
}