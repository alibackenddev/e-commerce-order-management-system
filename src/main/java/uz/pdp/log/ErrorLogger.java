package uz.pdp.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

public class ErrorLogger extends AppenderBase<LoggingEvent> {

    private String token;
    private String chatId;

    public void setBotToken(String token) {
        this.token = token;
    }

    public void setChatID(String chatId) {
        this.chatId = chatId;
    }

    private TelegramBot telegramBot;

    public ErrorLogger() {
        addFilter(new Filter<>() {
            @Override
            public FilterReply decide(LoggingEvent loggingEvent) {
                if (loggingEvent.getLevel().equals(Level.ERROR)) {
                    return FilterReply.ACCEPT;
                }
                return FilterReply.DENY;
            }
        });
    }

    @Override
    public void start() {
        telegramBot = new TelegramBot(token);
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        String loggerName =
                loggingEvent.getLoggerName();
        System.out.println("loggerName = " + loggerName);

        String logMessage =
                loggingEvent.getFormattedMessage();
        System.out.println("logMessage = " + logMessage);

        IThrowableProxy throwable =
                loggingEvent.getThrowableProxy();
        String className = throwable.getClassName();

        String message = """
                🚨 APPLICATION ERROR
                Logger: %s
                Message:%s
                Exception:%s
                """
                .formatted(
                        loggerName,
                        logMessage,
                        className
                );
        System.out.println("message = " + message);

        SendMessage sendMessage = new SendMessage(chatId, message);
        telegramBot.execute(sendMessage);
    }
}
