import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Transaction {
    String referenceNumber;
    String senderPhone;
    String transactionDate;
    double amount;
    String receiverPhone;
    String receiverName;

    public Transaction(String referenceNumber, String senderPhone, String transactionDate, double amount,
            String receiverPhone, String receiverName) {
        this.referenceNumber = referenceNumber;
        this.senderPhone = senderPhone;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.receiverPhone = receiverPhone;
        this.receiverName = receiverName;
    }

    public Object[] toTableRow() {
        return new Object[] { referenceNumber, senderPhone, transactionDate, amount, receiverPhone, receiverName };
    }
}

class TransactionArray {
    private Transaction[] transactions;
    private int transactionCount;

    public TransactionArray(int capacity) {
        transactions = new Transaction[capacity];
        transactionCount = 0;
    }

    public void loadFromFile(String filename) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
            String recordLine;
            while ((recordLine = fileReader.readLine()) != null && transactionCount < transactions.length) {
                String[] fields = recordLine.split(",");
                if (fields.length == 6) {
                    transactions[transactionCount++] = new Transaction(
                            fields[0].trim(), fields[1].trim(), fields[2].trim(),
                            Double.parseDouble(fields[3].trim()), fields[4].trim(), fields[5].trim());
                }
            }
        } catch (IOException | NumberFormatException exception) {
            System.out.println("Error reading file: " + exception.getMessage());
        }
    }

    public Transaction[] getAllTransactions() {
        Transaction[] activeTransactions = new Transaction[transactionCount];
        System.arraycopy(transactions, 0, activeTransactions, 0, transactionCount);
        return activeTransactions;
    }

    public Transaction linearSearch(String targetReference) {
        for (int index = 0; index < transactionCount; index++) {
            if (transactions[index].referenceNumber.equalsIgnoreCase(targetReference)) {
                return transactions[index];
            }
        }
        return null;
    }

    public Transaction binarySearch(String targetReference) {
        sortByReferenceNumber();

        int lowerBound = 0;
        int upperBound = transactionCount - 1;

        while (lowerBound <= upperBound) {
            int midpointIndex = lowerBound + (upperBound - lowerBound) / 2;
            int comparisonValue = transactions[midpointIndex].referenceNumber.compareToIgnoreCase(targetReference);

            if (comparisonValue == 0) {
                return transactions[midpointIndex];
            } else if (comparisonValue < 0) {
                lowerBound = midpointIndex + 1;
            } else {
                upperBound = midpointIndex - 1;
            }
        }
        return null;
    }

    private void sortByReferenceNumber() {
        for (int index = 1; index < transactionCount; index++) {
            Transaction currentTransaction = transactions[index];
            int scanIndex = index - 1;
            while (scanIndex >= 0 && transactions[scanIndex].referenceNumber
                    .compareToIgnoreCase(currentTransaction.referenceNumber) > 0) {
                transactions[scanIndex + 1] = transactions[scanIndex];
                scanIndex--;
            }
            transactions[scanIndex + 1] = currentTransaction;
        }
    }

    public void bubbleSortByAmount() {
        boolean hasSwappedElements;
        for (int iterationIndex = 0; iterationIndex < transactionCount - 1; iterationIndex++) {
            hasSwappedElements = false;
            for (int elementIndex = 0; elementIndex < transactionCount - iterationIndex - 1; elementIndex++) {
                if (transactions[elementIndex].amount > transactions[elementIndex + 1].amount) {
                    Transaction largerTransaction = transactions[elementIndex];
                    transactions[elementIndex] = transactions[elementIndex + 1];
                    transactions[elementIndex + 1] = largerTransaction;
                    hasSwappedElements = true;
                }
            }
            if (!hasSwappedElements)
                break;
        }
    }

    public double calculateTotalAmount() {
        double accumulatedTotal = 0;
        for (int index = 0; index < transactionCount; index++) {
            accumulatedTotal += transactions[index].amount;
        }
        return accumulatedTotal;
    }
}

final class Theme {
    static final Color BG_DARK = new Color(0x18, 0x1A, 0x2F);
    static final Color PANEL = new Color(0x24, 0x2E, 0x49);
    static final Color PANEL_ALT = new Color(0x37, 0x41, 0x5C);
    static final Color MAROON = new Color(0x54, 0x16, 0x2B);
    static final Color SLATE = new Color(0x37, 0x41, 0x5C);
    static final Color PEACH = new Color(0xFD, 0xA4, 0x81);
    static final Color RED = new Color(0xB4, 0x18, 0x2D);
    static final Color TEXT_PRIMARY = new Color(0xF5, 0xF5, 0xF5);
    static final Color TEXT_SECONDARY = new Color(0x87, 0x8D, 0x9D);

    static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 20);
    static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 14);
    static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 13);
    static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 13);
}

class RoundedBorder implements Border {
    private final int radius;
    private final Color color;

    RoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public Insets getBorderInsets(Component targetComponent) {
        return new Insets(radius / 2 + 4, radius / 2 + 8, radius / 2 + 4, radius / 2 + 8);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component targetComponent, Graphics graphics, int x, int y, int width, int height) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(color);
        graphics2D.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        graphics2D.dispose();
    }
}

class RoundedPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final int radius;
    private final Color fill;

    RoundedPanel(int radius, Color fill) {
        this.radius = radius;
        this.fill = fill;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(fill);
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        graphics2D.dispose();
        super.paintComponent(graphics);
    }
}

class RoundedButton extends JButton {
    private static final long serialVersionUID = 1L;
    private final Color baseColor;
    private final Color hoverColor;
    private final Color pressedColor;
    private boolean isHovering = false;
    private boolean isPressedDown = false;

    RoundedButton(String text, Color baseColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = baseColor.brighter();
        this.pressedColor = baseColor.darker();

        setForeground(Color.WHITE);
        setFont(Theme.BUTTON_FONT);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                isHovering = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent event) {
                isHovering = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent event) {
                isPressedDown = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                isPressedDown = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color activeFill = isEnabled() ? (isPressedDown ? pressedColor : (isHovering ? hoverColor : baseColor))
                : Theme.TEXT_SECONDARY;
        graphics2D.setColor(activeFill);
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        graphics2D.dispose();
        super.paintComponent(graphics);
    }
}

public class MobileMoneyGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final String STUDENT_NAME = "";
    private static final String STUDENT_NO = "";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    private TransactionArray transactionDatabase;
    private DefaultTableModel transactionTableModel;
    private JTable transactionDataTable;
    private JLabel statusBarLabel;

    private RoundedButton loadActionBtn;
    private RoundedButton linearSearchActionBtn;
    private RoundedButton binarySearchActionBtn;
    private RoundedButton bubbleSortActionBtn;
    private RoundedButton showTotalActionBtn;

    public MobileMoneyGUI() {
        transactionDatabase = new TransactionArray(100);

        setTitle("MOBILE MONEY - " + STUDENT_NAME + "_" + STUDENT_NO);
        setSize(900, 560);
        setMinimumSize(new Dimension(760, 460));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG_DARK);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTableCard(), BorderLayout.CENTER);
        add(buildControlPanel(), BorderLayout.SOUTH);

        wireActions();
    }

    private JComponent buildHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.BG_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 24, 8, 24));

        JLabel titleLabel = new JLabel("MOBILE MONEY - " + STUDENT_NAME + "_" + STUDENT_NO);
        titleLabel.setFont(Theme.TITLE_FONT);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        return headerPanel;
    }

    private JComponent buildTableCard() {
        RoundedPanel cardContainer = new RoundedPanel(14, Theme.PANEL);
        cardContainer.setLayout(new BorderLayout());
        cardContainer.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        String[] columnHeaders = { "Ref Number", "Sender's Phonenumber", "Date", "Amount", "Receiver's Phonenumber",
                "Receiver Name" };
        transactionTableModel = new DefaultTableModel(columnHeaders, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionDataTable = new JTable(transactionTableModel);
        styleTable(transactionDataTable);

        JScrollPane scrollPane = new JScrollPane(transactionDataTable);
        scrollPane.getViewport().setBackground(Theme.PANEL_ALT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        cardContainer.add(scrollPane, BorderLayout.CENTER);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Theme.BG_DARK);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(4, 20, 4, 24));
        wrapperPanel.add(cardContainer, BorderLayout.CENTER);
        return wrapperPanel;
    }

    private void styleTable(JTable targetTable) {
        targetTable.setBackground(Theme.PANEL_ALT);
        targetTable.setForeground(Theme.TEXT_PRIMARY);
        targetTable.setGridColor(Theme.PANEL);
        targetTable.setRowHeight(26);
        targetTable.setFont(Theme.BODY_FONT);
        targetTable.setSelectionBackground(Theme.MAROON);
        targetTable.setSelectionForeground(Color.WHITE);
        targetTable.setFillsViewportHeight(true);
        targetTable.setShowVerticalLines(false);

        targetTable.getTableHeader().setBackground(Theme.SLATE);
        targetTable.getTableHeader().setForeground(Color.WHITE);
        targetTable.getTableHeader().setFont(Theme.HEADER_FONT);
        targetTable.getTableHeader().setPreferredSize(new Dimension(0, 32));

        targetTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable tableInstance, Object tableValue, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component cellComponent = super.getTableCellRendererComponent(tableInstance, tableValue, isSelected,
                        hasFocus, row, column);
                if (!isSelected) {
                    cellComponent.setBackground(row % 2 == 0 ? Theme.PANEL_ALT : Theme.PANEL);
                    cellComponent.setForeground(Theme.TEXT_PRIMARY);
                }
                return cellComponent;
            }
        });
    }

    private JComponent buildControlPanel() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Theme.BG_DARK);
        containerPanel.setBorder(BorderFactory.createEmptyBorder(4, 20, 16, 24));

        JPanel buttonGrid = new JPanel(new GridLayout(1, 5, 10, 0));
        buttonGrid.setOpaque(false);

        loadActionBtn = new RoundedButton("Load File", Theme.SLATE);
        linearSearchActionBtn = new RoundedButton("Linear Search", Theme.MAROON);
        binarySearchActionBtn = new RoundedButton("Binary Search", Theme.MAROON);
        bubbleSortActionBtn = new RoundedButton("Bubble Sort", Theme.PEACH);
        showTotalActionBtn = new RoundedButton("Show Total", Theme.SLATE);

        buttonGrid.add(loadActionBtn);
        buttonGrid.add(linearSearchActionBtn);
        buttonGrid.add(binarySearchActionBtn);
        buttonGrid.add(bubbleSortActionBtn);
        buttonGrid.add(showTotalActionBtn);

        containerPanel.add(buttonGrid, BorderLayout.CENTER);

        statusBarLabel = new JLabel(" Ready.");
        statusBarLabel.setFont(Theme.BODY_FONT);
        statusBarLabel.setForeground(Theme.TEXT_SECONDARY);
        statusBarLabel.setBorder(BorderFactory.createEmptyBorder(8, 2, 0, 0));
        containerPanel.add(statusBarLabel, BorderLayout.SOUTH);

        return containerPanel;
    }

    private void wireActions() {
        loadActionBtn.addActionListener(event -> {
            transactionDatabase.loadFromFile(TRANSACTIONS_FILE);
            refreshTable();
            statusBarLabel.setText(" Data loaded successfully.");
            JOptionPane.showMessageDialog(this, "Data loaded successfully.");
        });

        linearSearchActionBtn.addActionListener(event -> {
            String searchReference = JOptionPane.showInputDialog(this, "Enter Reference Number:");
            if (searchReference != null && !searchReference.trim().isEmpty()) {
                showSearchResult(transactionDatabase.linearSearch(searchReference.trim()));
            }
        });

        binarySearchActionBtn.addActionListener(event -> {
            String searchReference = JOptionPane.showInputDialog(this, "Enter Reference Number:");
            if (searchReference != null && !searchReference.trim().isEmpty()) {
                Transaction foundTransaction = transactionDatabase.binarySearch(searchReference.trim());
                refreshTable();
                showSearchResult(foundTransaction);
            }
        });

        bubbleSortActionBtn.addActionListener(event -> {
            transactionDatabase.bubbleSortByAmount();
            refreshTable();
            statusBarLabel.setText(" Sorted by amount.");
        });

        showTotalActionBtn.addActionListener(event -> {
            double accumulatedTotal = transactionDatabase.calculateTotalAmount();
            JOptionPane.showMessageDialog(this, "Total Transacted Amount: K" + String.format("%.2f", accumulatedTotal));
        });
    }

    private void refreshTable() {
        transactionTableModel.setRowCount(0);
        for (Transaction currentTransaction : transactionDatabase.getAllTransactions()) {
            transactionTableModel.addRow(currentTransaction.toTableRow());
        }
        statusBarLabel.setText(" " + transactionDatabase.getAllTransactions().length + " transaction(s) loaded.");
    }

    private void showSearchResult(Transaction matchedTransaction) {
        if (matchedTransaction != null) {
            JOptionPane.showMessageDialog(this,
                    "Transaction Found:\nSender: " + matchedTransaction.senderPhone + "\nReceiver: "
                            + matchedTransaction.receiverName + "\nAmount: K" + matchedTransaction.amount);
        } else {
            JOptionPane.showMessageDialog(this, "Transaction not found.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MobileMoneyGUI().setVisible(true));
    }
}