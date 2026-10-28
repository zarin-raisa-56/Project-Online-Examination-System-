package gui;

import entity.Student;
import fileio.StudentFileHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OnlineExaminationSystem extends JFrame {

    DefaultTableModel model;

    public OnlineExaminationSystem() {
        setTitle("Online Examination System");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel title = new JLabel("ONLINE EXAMINATION SYSTEM");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(350, 10, 400, 30);
        add(title);

        // LEFT PANEL
        JPanel left = new JPanel(null);
        left.setBounds(20, 50, 350, 360);
        left.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(left);

        JTextField nameF = new JTextField();
        JTextField idF = new JTextField();
        JTextField phoneF = new JTextField();

        left.add(new JLabel("Student Name:")).setBounds(10, 10, 120, 25);
        nameF.setBounds(140, 10, 190, 25);
        left.add(nameF);

        left.add(new JLabel("ID:")).setBounds(10, 45, 120, 25);
        idF.setBounds(140, 45, 190, 25);
        left.add(idF);

        left.add(new JLabel("Subject:")).setBounds(10, 80, 120, 25);
        JComboBox<String> subF = new JComboBox<>(
                new String[]{"Math", "Physics", "Java", "English"}
        );
        subF.setBounds(140, 80, 190, 25);
        left.add(subF);

        left.add(new JLabel("Phone No:")).setBounds(10, 115, 120, 25);
        phoneF.setBounds(140, 115, 190, 25);
        left.add(phoneF);
//confirm
        JButton addBtn = new JButton("CONFIRM");
        addBtn.setBounds(30, 160, 120, 30);
        left.add(addBtn);
//exit
        JButton exitBtn = new JButton("EXIT");
        exitBtn.setBounds(180, 160, 120, 30);
        left.add(exitBtn);

        // RIGHT PANEL
        JPanel right = new JPanel(new BorderLayout());
        right.setBounds(390, 50, 570, 360);
        right.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(right);

        JPanel topBar = new JPanel();
        JTextField searchF = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        JButton removeBtn = new JButton("Remove");
        JButton updateBtn = new JButton("Update");
        JButton saveBtn = new JButton("Save Changes");

        topBar.add(searchF);
        topBar.add(searchBtn);
        topBar.add(removeBtn);
        topBar.add(updateBtn);
        topBar.add(saveBtn);
        right.add(topBar, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Subject", "Phone"};
        model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        right.add(new JScrollPane(table), BorderLayout.CENTER);

        // Load existing students from file
        try {
            Student[] students = StudentFileHandler.getAllStudents();
            for (Student s : students) {
                if (s != null) {
                    model.addRow(new Object[]{s.getId(), s.getName(), s.getSubject(), s.getPhone()});
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + ex.getMessage());
        }

         // ADD STUDENT
        addBtn.addActionListener(e -> {
            String id = idF.getText().trim();
            String name = nameF.getText().trim();
            String phone = phoneF.getText().trim();

            if (id.isEmpty() || name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
                return;
            }

            try {
                if (StudentFileHandler.searchStudent(id) != null) {
                    JOptionPane.showMessageDialog(this, "Student with this ID already exists");
                    return;
                }

                Student s = new Student(id, name, subF.getSelectedItem().toString(), phone);
                StudentFileHandler.addStudent(s);
                model.addRow(new Object[]{s.getId(), s.getName(), s.getSubject(), s.getPhone()});

                idF.setText("");
                nameF.setText("");
                phoneF.setText("");
                subF.setSelectedIndex(0);

                JOptionPane.showMessageDialog(this, "Student added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving student: " + ex.getMessage());
            }
        });

        // SEARCH BY ID ONLY
        searchBtn.addActionListener(e -> {
            try {
                Student s = StudentFileHandler.searchStudent(searchF.getText().trim());
                if (s == null) {
                    JOptionPane.showMessageDialog(this, "ID not found");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "ID: " + s.getId() +
                            "\nName: " + s.getName() +
                            "\nSubject: " + s.getSubject() +
                            "\nPhone: " + s.getPhone());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error searching student: " + ex.getMessage());
            }
        });

        // REMOVE STUDENT
        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String id = (String) model.getValueAt(row, 0);
                try {
                    StudentFileHandler.deleteStudent(id);
                    model.removeRow(row);
                    JOptionPane.showMessageDialog(this, "Student removed successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error removing student: " + ex.getMessage());
                }
            }
        });

        // UPDATE STUDENT
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a student row to update");
                return;
            }

            String id = (String) model.getValueAt(row, 0);
            String name = (String) model.getValueAt(row, 1);
            String subject = (String) model.getValueAt(row, 2);
            String phone = (String) model.getValueAt(row, 3);

        // Create an edit dialog
            JDialog editDialog = new JDialog(this, "Edit Student", true);
            editDialog.setSize(400, 250);
            editDialog.setLocationRelativeTo(this);
            editDialog.setLayout(new GridLayout(5, 2, 10, 10));

            JTextField editNameF = new JTextField(name);
            JTextField editIdF = new JTextField(id);
            JComboBox<String> editSubF = new JComboBox<>(
                    new String[]{"Math", "Physics", "Java", "English"}
            );
            editSubF.setSelectedItem(subject);
            JTextField editPhoneF = new JTextField(phone);

            editDialog.add(new JLabel("ID:"));
            editDialog.add(editIdF);
            editDialog.add(new JLabel("Name:"));
            editDialog.add(editNameF);
            editDialog.add(new JLabel("Subject:"));
            editDialog.add(editSubF);
            editDialog.add(new JLabel("Phone No:"));
            editDialog.add(editPhoneF);

            JButton saveEditBtn = new JButton("Save");
            JButton cancelBtn = new JButton("Cancel");
            editDialog.add(saveEditBtn);
            editDialog.add(cancelBtn);

            saveEditBtn.addActionListener(saveAction -> {
                String newId = editIdF.getText().trim();
                String newName = editNameF.getText().trim();
                String newPhone = editPhoneF.getText().trim();
                String newSubject = editSubF.getSelectedItem().toString();

                if (newId.isEmpty() || newName.isEmpty() || newPhone.isEmpty()) {
                    JOptionPane.showMessageDialog(editDialog, "Please fill in all fields");
                    return;
                }

                Student updated = new Student(newId, newName, newSubject, newPhone);
                try {
                    StudentFileHandler.updateStudent(id, updated);
                    model.setValueAt(newId, row, 0);
                    model.setValueAt(newName, row, 1);
                    model.setValueAt(newSubject, row, 2);
                    model.setValueAt(newPhone, row, 3);
                    editDialog.dispose();
                    JOptionPane.showMessageDialog(this, "Student updated successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(editDialog, "Error updating student: " + ex.getMessage());
                }
            });

            cancelBtn.addActionListener(cancelAction -> editDialog.dispose());

            editDialog.setVisible(true);
        });

    // SAVE BUTTON 
        saveBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Changes saved successfully!");
        });

        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }
}
