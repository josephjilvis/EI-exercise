🔹 Overview

The Factory Design Pattern is a Creational Design Pattern that provides an interface for creating objects but allows subclasses/factories to decide which class to instantiate.
It abstracts the instantiation logic, making the system more flexible and easier to extend.
🔹 Example (Simplified Java Code)
Notification email = NotificationFactory.create(NotificationType.EMAIL, cfg);
Notification sms   = NotificationFactory.create(NotificationType.SMS, cfg);
Notification push  = NotificationFactory.create(NotificationType.PUSH, cfg);

🔹 Advantages

✅ Removes tight coupling between client and object creation
✅ Follows Open/Closed Principle (easy to add new product types)
✅ Centralizes object creation logic

🔹 Disadvantages

❌ Factory code can become complex when too many product variations exist
❌ Client needs to know the correct factory or key to request a product

🔹 Real-World Use Cases

Logging frameworks (choosing FileLogger, ConsoleLogger, etc.)

Notification systems (SMS, Email, Push)

Database drivers (MySQL, PostgreSQL, Oracle)

📄 README – Flyweight Design Pattern
🔹 Overview

The Flyweight Design Pattern is a Structural Design Pattern used to minimize memory usage by sharing objects.
It is especially useful when you need to create a large number of similar objects that share common state (intrinsic) while varying in external state (extrinsic).

🔹 Example (Simplified Java Code)
// TreeType acts as the Flyweight (shared)
class TreeType {
    private String name;
    private String color;
    private String texture;

    public TreeType(String name, String color, String texture) {
        this.name = name;
        this.color = color;
        this.texture = texture;
    }

    public void draw(int x, int y) {
        System.out.println("Drawing " + name + " tree at (" + x + "," + y + ")");
    }
}

// Factory to manage TreeTypes
class TreeFactory {
    private static Map<String, TreeType> types = new HashMap<>();

    public static TreeType getTreeType(String name, String color, String texture) {
        String key = name + color + texture;
        types.putIfAbsent(key, new TreeType(name, color, texture));
        return types.get(key);
    }
}

🔹 Advantages

✅ Greatly reduces memory usage with large numbers of objects
✅ Centralizes shared state for efficiency
✅ Faster performance for repetitive object creation

🔹 Disadvantages

❌ Increased complexity in separating intrinsic vs extrinsic state
❌ Harder to implement and maintain

🔹 Real-World Use Cases

Text editors (characters are flyweights, positions are extrinsic)

Game development (trees, bullets, particles)

GUI libraries (buttons/icons shared across windows)

👉 Together, Factory focuses on how objects are created, while Flyweight focuses on how objects are shared to save memory.
