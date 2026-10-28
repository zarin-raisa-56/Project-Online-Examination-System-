package entity;

public class Student {
    private String id;
    private String name;
    private String subject;
    private String phone;

    public Student(String id, String name, String subject, String phone) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.phone = phone;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSubject() { return subject; }
    public String getPhone() { return phone; }

    
    public String toFileString() {
        return id + "," + name + "," + subject + "," + phone;
    }

    public static Student fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 4) {
            return new Student(parts[0], parts[1], parts[2], parts[3]);
        }
        return null;
    }
}
