
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dell
 */

class LibraryGUI extends JFrame {
    private Library library;
    private JTextArea outputArea;
    private JTextField inputField;

    public LibraryGUI() {
        library = new Library(outputArea); // Pass the outputArea to the Library
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Library Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem loadItemsItem = new JMenuItem("Load Items from File");
        loadItemsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                library.loadItemsFromFile("data.txt");
                appendToOutput("Loaded items from file.");
            }
        });
        menu.add(loadItemsItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processInput(inputField.getText());
                inputField.setText("");
            }
        });
        panel.add(inputField, BorderLayout.SOUTH);

        add(panel);

        setVisible(true);
    }

    private void processInput(String input) {
        String[] parts = input.split(" ");
        int choice = Integer.parseInt(parts[0]);

        switch (choice) {
            case 1:
                if (parts.length < 4) {
                    appendToOutput("Invalid input. Format: 1 [type] [title] [author/year/publisher]");
                    break;
                }
                int itemType = Integer.parseInt(parts[1]);
                String title = parts[2];
                String info = input.substring(input.indexOf(title) + title.length()).trim();
                createItem(itemType, title, info);
                break;
            case 2:
                if (parts.length < 2) {
                    appendToOutput("Invalid input. Format: 2 [item ID]");
                    break;
                }
                int itemId = Integer.parseInt(parts[1]);
                deleteItem(itemId);
                break;
            case 3:
                if (parts.length < 3) {
                    appendToOutput("Invalid input. Format: 3 [item ID] [new title]");
                    break;
                }
                int editId = Integer.parseInt(parts[1]);
                String newTitle = input.substring(input.indexOf(parts[2]));
                editItem(editId, newTitle);
                break;
            case 4:
                viewAllItems();
                break;
            case 5:
                if (parts.length < 2) {
                    appendToOutput("Invalid input. Format: 5 [item ID]");
                    break;
                }
                int viewId = Integer.parseInt(parts[1]);
                viewItemById(viewId);
                break;
            case 6:
                displayHotPicks();
                break;
            case 7:
                if (parts.length < 2) {
                    appendToOutput("Invalid input. Format: 7 [item ID]");
                    break;
                }
                int borrowItemId = Integer.parseInt(parts[1]);
                Borrower borrower = new Borrower(library.getNextUserId());
                borrowItem(borrower, borrowItemId);
                break;
            case 8:
                displayBorrowers();
                break;
            case 9:
                System.exit(0);
                break;
            default:
                appendToOutput("Invalid input. Please enter a valid option.");
                break;
        }
    }

    private void createItem(int itemType, String title, String info) {
        switch (itemType) {
            case 1:
                String[] bookInfo = info.split(" ");
                if (bookInfo.length < 2) {
                    appendToOutput("Invalid input for book creation.");
                    break;
                }
                String author = bookInfo[0];
                int year = Integer.parseInt(bookInfo[1]);
                Book book = new Book(title, author, year);
                library.addItem(book);
                appendToOutput("Book added successfully.");
                break;
            case 2:
                String[] magazineInfo = info.split(";");
                if (magazineInfo.length < 2) {
                    appendToOutput("Invalid input for magazine creation.");
                    break;
                }
                String publisher = magazineInfo[0];
                String authorsInput = magazineInfo[1];
                ArrayList<String> authors = new ArrayList<>(Arrays.asList(authorsInput.split(",")));
                Magazine magazine = new Magazine(title, publisher, authors);
                library.addItem(magazine);
                appendToOutput("Magazine added successfully.");
                break;
            case 3:
                String[] newspaperInfo = info.split(";");
                if (newspaperInfo.length < 2) {
                    appendToOutput("Invalid input for newspaper creation.");
                    break;
                }
                String publisherNewspaper = newspaperInfo[0];
                String publicationDate = newspaperInfo[1];
                Newspaper newspaper = new Newspaper(title, publisherNewspaper, publicationDate);
                library.addItem(newspaper);
                appendToOutput("Newspaper added successfully.");
                break;
            default:
                appendToOutput("Invalid item type. Please enter a valid option.");
                break;
        }
    }

    private void deleteItem(int itemId) {
        library.deleteItem(itemId);
        appendToOutput("Item with ID " + itemId + " deleted.");
    }

    private void editItem(int editId, String newTitle) {
        library.editItem(editId, newTitle);
        appendToOutput("Item with ID " + editId + " edited.");
    }

    private void viewAllItems() {
        outputArea.setText("");
        library.viewAllItems(outputArea);
    }

    private void viewItemById(int viewId) {
        outputArea.setText("");
        library.viewItemById(viewId, outputArea);
    }

    private void displayHotPicks() {
        outputArea.setText("");
        library.displayHotPicks(outputArea);
    }

    private void borrowItem(Borrower borrower, int itemId) {
        library.borrowItem(borrower, itemId, outputArea);
    }

    private void displayBorrowers() {
        outputArea.setText("");
        library.displayBorrowers(outputArea);
    }

    private void appendToOutput(String text) {
        outputArea.append(text + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibraryGUI();
        });
    }
}

