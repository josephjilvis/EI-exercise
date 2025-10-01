// FactoryDemo.java
// Demonstration of the Factory Design Pattern in Java
// Single-file example with multiple product implementations and a factory.
// Author: ChatGPT (Jose-friendly version)

import java.util.*;

// Product: common interface for all products created by the factory
interface Notification {
    void send(String recipient, String message);
    String getDescription();
}

// ConcreteProduct 1
class EmailNotification implements Notification {
    private String smtpServer;

    public EmailNotification(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    @Override
    public void send(String recipient, String message) {
        // In a real app, you'd use an SMTP library. Here we print to console.
        System.out.println("[Email -> " + recipient + "] via " + smtpServer + ": " + message);
    }

    @Override
    public String getDescription() {
        return "EmailNotification (SMTP: " + smtpServer + ")";
    }
}

// ConcreteProduct 2
class SMSNotification implements Notification {
    private String provider;

    public SMSNotification(String provider) {
        this.provider = provider;
    }

    @Override
    public void send(String recipient, String message) {
        // Replace with real SMS gateway integration in production.
        System.out.println("[SMS -> " + recipient + "] using " + provider + ": " + message);
    }

    @Override
    public String getDescription() {
        return "SMSNotification (Provider: " + provider + ")";
    }
}

// ConcreteProduct 3
class PushNotification implements Notification {
    private String appId;

    public PushNotification(String appId) {
        this.appId = appId;
    }

    @Override
    public void send(String recipient, String message) {
        System.out.println("[Push -> " + recipient + "] (AppId:" + appId + "): " + message);
    }

    @Override
    public String getDescription() {
        return "PushNotification (AppId: " + appId + ")";
    }
}

// FactoryProductType: Use an enum to select types in a type-safe way
enum NotificationType {
    EMAIL,
    SMS,
    PUSH
}

// Simple Factory class (static factory method)
class NotificationFactory {
    // This factory method centralizes the logic of creating notification objects.
    // The client provides a type and configuration map; the factory returns a Notification.
    public static Notification create(NotificationType type, Map<String, String> config) {
        switch (type) {
            case EMAIL:
                String smtp = config.getOrDefault("smtp", "smtp.example.com");
                return new EmailNotification(smtp);
            case SMS:
                String provider = config.getOrDefault("provider", "Twilio");
                return new SMSNotification(provider);
            case PUSH:
                String appId = config.getOrDefault("appId", "com.example.app");
                return new PushNotification(appId);
            default:
                throw new IllegalArgumentException("Unsupported NotificationType: " + type);
        }
    }
}

// Alternative: Factory with registration (extensible at runtime)
// This shows a slightly more advanced factory that allows third-party registration of creators.
interface NotificationCreator {
    Notification create(Map<String, String> config);
}

class ExtensibleNotificationFactory {
    private Map<String, NotificationCreator> registry = new HashMap<>();

    // Register default creators in the constructor
    public ExtensibleNotificationFactory() {
        register("email", cfg -> new EmailNotification(cfg.getOrDefault("smtp", "smtp.example.com")));
        register("sms", cfg -> new SMSNotification(cfg.getOrDefault("provider", "Twilio")));
        register("push", cfg -> new PushNotification(cfg.getOrDefault("appId", "com.example.app")));
    }

    public void register(String key, NotificationCreator creator) {
        registry.put(key.toLowerCase(), creator);
    }

    public Notification create(String key, Map<String, String> config) {
        NotificationCreator creator = registry.get(key.toLowerCase());
        if (creator == null) {
            throw new IllegalArgumentException("No creator registered for key: " + key);
        }
        return creator.create(config);
    }
}

// Client code demonstrating use of factories
public class FactoryDemo {
    public static void main(String[] args) {
        System.out.println("=== Simple Factory Demo ===");
        Map<String, String> cfg1 = new HashMap<>();
        cfg1.put("smtp", "smtp.mycompany.com");

        Notification email = NotificationFactory.create(NotificationType.EMAIL, cfg1);
        System.out.println("Created: " + email.getDescription());
        email.send("jose@example.com", "Hello Jose! This is an email notification.");

        Map<String, String> cfg2 = new HashMap<>();
        cfg2.put("provider", "Nexmo");
        Notification sms = NotificationFactory.create(NotificationType.SMS, cfg2);
        System.out.println("Created: " + sms.getDescription());
        sms.send("+919876543210", "This is an SMS notification.");

        Notification push = NotificationFactory.create(NotificationType.PUSH, Collections.emptyMap());
        System.out.println("Created: " + push.getDescription());
        push.send("user-123", "You have a new alert!");

        System.out.println("\n=== Extensible Factory Demo ===");
        ExtensibleNotificationFactory extFactory = new ExtensibleNotificationFactory();

        // Register a custom notification type at runtime (example: Slack message)
        extFactory.register("slack", cfg -> new Notification() {
            private String webhook = cfg.getOrDefault("webhook", "https://hooks.slack.com/default");

            @Override
            public void send(String recipient, String message) {
                // A placeholder for sending to Slack webhook
                System.out.println("[Slack -> " + recipient + "] webhook:" + webhook + " message:" + message);
            }

            @Override
            public String getDescription() {
                return "SlackNotification (webhook: " + webhook + ")";
            }
        });

        Map<String, String> slackCfg = new HashMap<>();
        slackCfg.put("webhook", "https://hooks.slack.com/services/ABC/DEF/XYZ");
        Notification slack = extFactory.create("slack", slackCfg);
        System.out.println("Created (runtime): " + slack.getDescription());
        slack.send("#general", "Hello team! This is a Slack notification created at runtime.");

        // Show how easy it is to add new types without changing client code:
        System.out.println("\n=== Adding a new type (Demo) without modifying factory code ===");
        extFactory.register("console-logger", cfg -> new Notification() {
            @Override
            public void send(String recipient, String message) {
                System.out.println("[ConsoleLogger] To: " + recipient + " | " + message);
            }

            @Override
            public String getDescription() {
                return "ConsoleLoggerNotification";
            }
        });

        Notification logger = extFactory.create("console-logger", Collections.emptyMap());
        System.out.println("Created (runtime): " + logger.getDescription());
        logger.send("DevOps", "Deployment completed successfully.");

        System.out.println("\nDemo complete.");
    }
}
