package fileio;

import entity.Student;
import java.io.*;

public class StudentFileHandler {

    private static final String FILE_PATH = "fileio/students.txt";
    private static final int MAX = 100;

    // CREATE
    public static void addStudent(Student student) throws IOException {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(file, true);
        fw.write(student.toFileString() + "\n");
        fw.close();
    }

    // READ ALL
    public static Student[] getAllStudents() throws IOException {
        Student[] students = new Student[MAX];
        int index = 0;

        File file = new File(FILE_PATH);
        if (!file.exists()) return students;

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null && index < MAX) {
            students[index] = Student.fromFileString(line);
            index++;
        }
        br.close();
        return students;
    }

    // SEARCH by ID only
    public static Student searchStudent(String id) throws IOException {
        Student[] students = getAllStudents();
        for (Student s : students) {
            if (s != null && s.getId().equals(id)) return s;
        }
        return null;
    }

     // UPDATE with old ID support
    public static void updateStudent(String oldId, Student updatedStudent) throws IOException {
        Student[] students = getAllStudents();
        FileWriter fw = new FileWriter(FILE_PATH);

        for (Student s : students) {
            if (s != null) {
                if (s.getId().equals(oldId)) {
                    fw.write(updatedStudent.toFileString() + "\n");
                } else {
                    fw.write(s.toFileString() + "\n");
                }
            }
        }
        fw.close();
    }

// DELETE
    
    public static void deleteStudent(String id) throws IOException {
        Student[] students = getAllStudents();
        FileWriter fw = new FileWriter(FILE_PATH);

        for (Student s : students) {
            if (s != null && !s.getId().equals(id)) {
                fw.write(s.toFileString() + "\n");
            }
        }
        fw.close();
    }
}
