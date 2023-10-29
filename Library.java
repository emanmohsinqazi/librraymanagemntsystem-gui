
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.JTextArea;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dell
 */
public class Library {
     private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Borrower> borrowers = new ArrayList<>();
    private HashMap<Item, Borrower> borrowedItems = new HashMap<>();
    private int nextUserId = 1;
    private JTextArea outputArea; // Store the outputArea

    public Library(JTextArea outputArea) {
        this.outputArea = outputArea;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void loadItemsFromFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int type = Integer.parseInt(parts[0]);
                String title = parts[1];

                if (type == 1) {
                    String author = parts[2];
                    int year = Integer.parseInt(parts[3]);
                    Book book = new Book(title, author, year);
                    addItem(book);
                } else if (type == 2) {
                    String publisher = parts[2];
                    ArrayList<String> authors = new ArrayList<>();
                    for (int i = 3; i < parts.length; i++) {
                        if (parts[i].endsWith(".")) {
                            authors.add(parts[i].substring(0, parts[i].length() - 1));
                            break;
                        } else {
                            authors.add(parts[i]);
                        }
                    }
                    Magazine magazine = new Magazine(title, publisher, authors);
                    addItem(magazine);
                } else if (type == 3) {
                    String publisher = parts[2];
                    String publicationDate = parts[3];
                    Newspaper newspaper = new Newspaper(title, publisher, publicationDate);
                    addItem(newspaper);
                }
            }
            reader.close();
            outputArea.append("Loaded items from file.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editItem(int id, String newTitle) {
        for (Item item : items) {
            if (item.getId() == id) {
                item.title = newTitle;
                break;
            }
        }
        outputArea.append("Item with ID " + id + " edited.\n");
    }

    public void deleteItem(int id) {
        Item itemToDelete = null;
        for (Item item : items) {
            if (item.getId() == id) {
                itemToDelete = item;
                break;
            }
        }
        if (itemToDelete != null) {
            items.remove(itemToDelete);
            outputArea.append("Item with ID " + id + " deleted.\n");
        } else {
            outputArea.append("Item with ID " + id + " not found.\n");
        }
    }

    public void viewAllItems(JTextArea outputArea) {
        for (Item item : items) {
            item.display(outputArea);
        }
    }

    public void viewItemById(int id, JTextArea outputArea) {
        for (Item item : items) {
            if (item.getId() == id) {
                item.display(outputArea);
                break;
            }
        }
    }

    public void displayHotPicks(JTextArea outputArea) {
        items.sort(Comparator.comparingInt(Item::getPopularityCount).reversed());

        outputArea.append("Hot Picks:\n");
        for (Item item : items) {
            outputArea.append(item.getTitle() + " (Popularity: " + item.getPopularityCount() + ")\n");
        }
    }

    public void displayBorrowers(JTextArea outputArea) {
        outputArea.append("Borrowers and Borrowed Items:\n");

        for (Borrower borrower : borrowers) {
            outputArea.append("User ID: " + borrower.getUserId() + "\n");

            ArrayList<Item> borrowedItemsByBorrower = getBorrowedItemsByBorrower(borrower);
            for (Item item : borrowedItemsByBorrower) {
                outputArea.append("- " + item.getTitle() + "\n");
            }

            outputArea.append("\n");
        }
    }

    public void borrowItem(Borrower borrower, int itemId, JTextArea outputArea) {
        Item itemToBorrow = null;
        for (Item item : items) {
            if (item.getId() == itemId) {
                itemToBorrow = item;
                break;
            }
        }

        if (itemToBorrow == null) {
            outputArea.append("Item with ID " + itemId + " not found.\n");
            return;
        }

        if (borrowedItems.containsKey(itemToBorrow)) {
            outputArea.append("Item is already borrowed by another user.\n");
            return;
        }

        if (borrower.hasBorrowedItem(itemToBorrow)) {
            outputArea.append("You have already borrowed this item.\n");
            return;
        }

        borrowers.add(borrower);
        borrowedItems.put(itemToBorrow, borrower);
        borrower.borrowItem(itemToBorrow);
        itemToBorrow.increasePopularity();
        outputArea.append("Item borrowed successfully.\n");
    }

}
