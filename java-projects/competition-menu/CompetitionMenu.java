import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.HashSet;

class TeamNode {
    String teamNumber;
    String sponsoringUniversity;
    String[] studentNames;
    int[] roundScores;
    boolean isEliminated;
    TeamNode nextTeam;

    public TeamNode(String teamNumber, String sponsoringUniversity, String[] studentNames) {
        this.teamNumber = teamNumber;
        this.sponsoringUniversity = sponsoringUniversity;
        this.studentNames = studentNames;
        this.roundScores = new int[4];
        this.isEliminated = false;
        this.nextTeam = null;
    }
}

class TeamLinkedList {
    private TeamNode headNode;

    public void registerTeam(String teamNumber, String university, String[] students) {
        if (searchTeam(teamNumber) != null) {
            System.out.println("Error: Team number already exists.");
            return;
        }

        TeamNode newTeam = new TeamNode(teamNumber, university, students);
        if (headNode == null) {
            headNode = newTeam;
        } else {
            TeamNode currentTeam = headNode;
            while (currentTeam.nextTeam != null) {
                currentTeam = currentTeam.nextTeam;
            }
            currentTeam.nextTeam = newTeam;
        }
        System.out.println("Team registered successfully.");
    }

    public void displayAllTeams() {
        if (headNode == null) {
            System.out.println("No teams registered yet.");
            return;
        }

        TeamNode currentTeam = headNode;
        while (currentTeam != null) {
            System.out.println("Team: " + currentTeam.teamNumber + " | Uni: " + currentTeam.sponsoringUniversity
                    + " | Status: " + (currentTeam.isEliminated ? "Eliminated" : "Active"));
            System.out.println("  Students: " + String.join(", ", currentTeam.studentNames));
            System.out.print("  Scores: ");
            for (int score : currentTeam.roundScores) {
                System.out.print(score + " ");
            }
            System.out.println("\n");
            currentTeam = currentTeam.nextTeam;
        }
    }

    public void deleteTeam(String teamNumber) {
        if (headNode == null) {
            System.out.println("No teams to delete.");
            return;
        }

        if (headNode.teamNumber.equals(teamNumber)) {
            headNode = headNode.nextTeam;
            System.out.println("Team deleted.");
            return;
        }

        TeamNode currentTeam = headNode;
        TeamNode previousTeam = null;
        while (currentTeam != null && !currentTeam.teamNumber.equals(teamNumber)) {
            previousTeam = currentTeam;
            currentTeam = currentTeam.nextTeam;
        }

        if (currentTeam == null) {
            System.out.println("Team not found.");
            return;
        }

        previousTeam.nextTeam = currentTeam.nextTeam;
        System.out.println("Team deleted.");
    }

    public TeamNode searchTeam(String teamNumber) {
        TeamNode currentTeam = headNode;
        while (currentTeam != null) {
            if (currentTeam.teamNumber.equals(teamNumber)) {
                return currentTeam;
            }
            currentTeam = currentTeam.nextTeam;
        }
        return null;
    }

    public void captureScores(int roundIndex, Scanner inputScanner) {
        if (headNode == null) {
            System.out.println("No teams available.");
            return;
        }

        TeamNode currentTeam = headNode;
        TeamNode lowestScoringTeam = null;
        int lowestScore = Integer.MAX_VALUE;

        while (currentTeam != null) {
            if (!currentTeam.isEliminated) {
                int score = readNonNegativeInt(inputScanner,
                        "Enter Round " + (roundIndex + 1) + " score for Team " + currentTeam.teamNumber + ": ");
                currentTeam.roundScores[roundIndex] = score;

                // Elimination is evaluated exclusively on the active round's score.
                if (score < lowestScore) {
                    lowestScore = score;
                    lowestScoringTeam = currentTeam;
                }
            }
            currentTeam = currentTeam.nextTeam;
        }

        if (lowestScoringTeam != null) {
            lowestScoringTeam.isEliminated = true;
            System.out.println("\nTeam " + lowestScoringTeam.teamNumber + " from "
                    + lowestScoringTeam.sponsoringUniversity + " has been eliminated.");
        }
    }

    private int readNonNegativeInt(Scanner inputScanner, String prompt) {
        int parsedScore = -1;
        while (parsedScore < 0) {
            System.out.print(prompt);
            try {
                parsedScore = Integer.parseInt(inputScanner.nextLine().trim());
                if (parsedScore < 0) {
                    System.out.println("Score can't be negative.");
                }
            } catch (NumberFormatException event) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
        return parsedScore;
    }

    public void determineWinner() {
        TeamNode currentTeam = headNode;
        TeamNode winner = null;
        int highestTotal = -1;

        while (currentTeam != null) {
            if (!currentTeam.isEliminated) {
                int teamTotal = 0;
                for (int score : currentTeam.roundScores) {
                    teamTotal += score;
                }
                if (teamTotal > highestTotal) {
                    highestTotal = teamTotal;
                    winner = currentTeam;
                }
            }
            currentTeam = currentTeam.nextTeam;
        }

        if (winner != null) {
            System.out.println("The winning university is " + winner.sponsoringUniversity + " (Team "
                    + winner.teamNumber + ") with " + highestTotal + " points!");
        } else {
            System.out.println("No active teams remaining to determine a winner.");
        }
    }
}

final class Theme {
    static final Color BG_DARK = new Color(0x16, 0x21, 0x3A);
    static final Color PANEL = new Color(0x22, 0x30, 0x4A);
    static final Color PANEL_ALT = new Color(0x27, 0x36, 0x54);
    static final Color PURPLE = new Color(0xA1, 0x63, 0xF7);
    static final Color PERIWINKLE = new Color(0x6F, 0x88, 0xFC);
    static final Color CYAN = new Color(0x45, 0xE3, 0xFF);
    static final Color CORAL = new Color(0xFF, 0x75, 0x82);
    static final Color TEXT_PRIMARY = new Color(0xF5, 0xF5, 0xF5);
    static final Color TEXT_SECONDARY = new Color(0x9A, 0xA5, 0xB8);

    static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 20);
    static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 15);
    static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 13);
    static final Font NAV_FONT = new Font("SansSerif", Font.BOLD, 13);
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
    private boolean hovering = false;
    private boolean pressedDown = false;

    RoundedButton(String text, Color baseColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = baseColor.brighter();
        this.pressedColor = baseColor.darker();

        setForeground(Color.WHITE);
        setFont(Theme.NAV_FONT);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                hovering = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent event) {
                hovering = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent event) {
                pressedDown = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                pressedDown = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color activeFill = isEnabled() ? (pressedDown ? pressedColor : (hovering ? hoverColor : baseColor))
                : Theme.TEXT_SECONDARY;
        graphics2D.setColor(activeFill);
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        graphics2D.dispose();
        super.paintComponent(graphics);
    }
}

class NavButton extends JButton {
    private static final long serialVersionUID = 1L;
    private boolean isSelected = false;
    private boolean isHovering = false;
    private final Color accentColor;

    NavButton(String text, Color accentColor) {
        super(text);
        this.accentColor = accentColor;
        setForeground(Theme.TEXT_PRIMARY);
        setFont(Theme.NAV_FONT);
        setHorizontalAlignment(SwingConstants.LEFT);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

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
        });
    }

    void setNavSelected(boolean selectedStatus) {
        this.isSelected = selectedStatus;
        setForeground(selectedStatus ? Color.WHITE : Theme.TEXT_PRIMARY);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (isSelected) {
            graphics2D.setColor(accentColor);
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        } else if (isHovering) {
            graphics2D.setColor(Theme.PANEL_ALT);
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        }
        graphics2D.dispose();
        super.paintComponent(graphics);
    }
}

public class CompetitionMenu {
    private static final String STUDENT_NAME = "";
    private static final String STUDENT_NO = "";
    private static final int TOTAL_ROUNDS = 4;

    private final TeamLinkedList competitionList = new TeamLinkedList();

    private final List<String> registeredTeamNumbers = new ArrayList<>();

    private int currentRound = 0;

    private JFrame applicationFrame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JLabel statusBar;
    private final Map<String, NavButton> navButtons = new LinkedHashMap<>();

    private DefaultTableModel teamsTableModel;
    private JPanel captureFormBody;
    private JLabel captureHeaderLabel;
    private final Map<String, JTextField> captureFields = new LinkedHashMap<>();
    private JLabel winnerResultLabel;
    private JTextArea searchResultArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CompetitionMenu().buildAndShow());
    }

    private void buildAndShow() {
        applicationFrame = new JFrame("JAVA PROGRAMMING COMPETITION - " + STUDENT_NAME + "_" + STUDENT_NO);
        applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        applicationFrame.setSize(980, 620);
        applicationFrame.setMinimumSize(new Dimension(820, 520));
        applicationFrame.setLocationRelativeTo(null);
        applicationFrame.getContentPane().setBackground(Theme.BG_DARK);
        applicationFrame.setLayout(new BorderLayout());

        applicationFrame.add(buildHeader(), BorderLayout.NORTH);
        applicationFrame.add(buildSidebar(), BorderLayout.WEST);
        applicationFrame.add(buildCardArea(), BorderLayout.CENTER);
        applicationFrame.add(buildStatusBar(), BorderLayout.SOUTH);

        selectCard("register");
        applicationFrame.setVisible(true);
    }

    private JComponent buildHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.BG_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 24, 12, 24));

        JLabel titleLabel = new JLabel("JAVA PROGRAMMING COMPETITION - " + STUDENT_NAME + "_" + STUDENT_NO);
        titleLabel.setFont(Theme.TITLE_FONT);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        return headerPanel;
    }

    private JComponent buildSidebar() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(Theme.PANEL);
        sidebarPanel.setPreferredSize(new Dimension(250, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));

        addNavButton(sidebarPanel, "register", "1. Register Team", Theme.PURPLE);
        addNavButton(sidebarPanel, "display", "2. Display All Teams", Theme.PERIWINKLE);
        addNavButton(sidebarPanel, "delete", "3. Delete Team", Theme.CORAL);
        addNavButton(sidebarPanel, "capture", "4. Capture Scores", Theme.CYAN);
        addNavButton(sidebarPanel, "winner", "5. Find Winner", Theme.PURPLE);
        addNavButton(sidebarPanel, "search", "6. Search Team", Theme.PERIWINKLE);

        sidebarPanel.add(Box.createVerticalGlue());

        NavButton exitAction = new NavButton("7. Exit", Theme.CORAL);
        exitAction.addActionListener(event -> confirmExit());
        exitAction.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(exitAction);

        return sidebarPanel;
    }

    private void addNavButton(JPanel sidebarPanel, String cardKey, String label, Color accentColor) {
        NavButton navAction = new NavButton(label, accentColor);
        navAction.addActionListener(event -> selectCard(cardKey));
        sidebarPanel.add(navAction);
        sidebarPanel.add(Box.createVerticalStrut(6));
        navButtons.put(cardKey, navAction);
    }

    private JComponent buildCardArea() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Theme.BG_DARK);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 24));

        cardPanel.add(wrapCard(buildRegisterCard()), "register");
        cardPanel.add(wrapCard(buildDisplayCard()), "display");
        cardPanel.add(wrapCard(buildDeleteCard()), "delete");
        cardPanel.add(wrapCard(buildCaptureCard()), "capture");
        cardPanel.add(wrapCard(buildWinnerCard()), "winner");
        cardPanel.add(wrapCard(buildSearchCard()), "search");

        return cardPanel;
    }

    private JComponent wrapCard(JComponent innerComponent) {
        RoundedPanel cardContainer = new RoundedPanel(14, Theme.PANEL);
        cardContainer.setLayout(new BorderLayout());
        cardContainer.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        cardContainer.add(innerComponent, BorderLayout.CENTER);
        return cardContainer;
    }

    private JComponent buildStatusBar() {
        statusBar = new JLabel(" Ready.");
        statusBar.setFont(Theme.BODY_FONT);
        statusBar.setForeground(Theme.TEXT_SECONDARY);
        statusBar.setBorder(BorderFactory.createEmptyBorder(6, 24, 10, 24));

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Theme.BG_DARK);
        wrapperPanel.add(statusBar, BorderLayout.WEST);
        return wrapperPanel;
    }

    private void selectCard(String key) {
        for (Map.Entry<String, NavButton> entry : navButtons.entrySet()) {
            entry.getValue().setNavSelected(entry.getKey().equals(key));
        }

        if (key.equals("display")) {
            refreshTeamsTable();
        } else if (key.equals("capture")) {
            refreshCaptureScoresCard();
        }
        cardLayout.show(cardPanel, key);
    }

    private void setStatus(String message) {
        statusBar.setText(" " + message);
    }

    private JLabel sectionTitle(String text) {
        JLabel titleLabel = new JLabel(text);
        titleLabel.setFont(Theme.HEADER_FONT);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        return titleLabel;
    }

    private JLabel fieldLabel(String text) {
        JLabel inputLabel = new JLabel(text);
        inputLabel.setFont(Theme.BODY_FONT);
        inputLabel.setForeground(Theme.TEXT_SECONDARY);
        return inputLabel;
    }

    private JTextField styledTextField() {
        JTextField inputField = new JTextField();
        inputField.setFont(Theme.BODY_FONT);
        inputField.setBackground(Theme.PANEL_ALT);
        inputField.setForeground(Theme.TEXT_PRIMARY);
        inputField.setCaretColor(Theme.TEXT_PRIMARY);
        inputField.setBorder(new RoundedBorder(8, Theme.TEXT_SECONDARY));
        return inputField;
    }

    private void styleTable(JTable dataTable) {
        dataTable.setBackground(Theme.PANEL_ALT);
        dataTable.setForeground(Theme.TEXT_PRIMARY);
        dataTable.setGridColor(Theme.PANEL);
        dataTable.setRowHeight(26);
        dataTable.setFont(Theme.BODY_FONT);
        dataTable.setSelectionBackground(Theme.PURPLE);
        dataTable.setSelectionForeground(Color.WHITE);
        dataTable.getTableHeader().setBackground(Theme.PERIWINKLE);
        dataTable.getTableHeader().setForeground(Color.WHITE);
        dataTable.getTableHeader().setFont(Theme.HEADER_FONT);
        dataTable.setFillsViewportHeight(true);

        dataTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable targetTable, Object tableValue, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component cellComponent = super.getTableCellRendererComponent(targetTable, tableValue, isSelected,
                        hasFocus, row, column);
                if (!isSelected) {
                    cellComponent.setBackground(row % 2 == 0 ? Theme.PANEL_ALT : Theme.PANEL);
                    boolean isEliminatedRow = "Eliminated".equals(targetTable.getValueAt(row, 2));
                    cellComponent.setForeground(isEliminatedRow && column == 2 ? Theme.CORAL : Theme.TEXT_PRIMARY);
                }
                return cellComponent;
            }
        });
    }

    private JComponent buildRegisterCard() {
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        formPanel.add(sectionTitle("Register a Team"));
        formPanel.add(Box.createVerticalStrut(16));

        JTextField teamNumberField = styledTextField();
        JTextField universityField = styledTextField();
        JTextField student1Field = styledTextField();
        JTextField student2Field = styledTextField();
        JTextField student3Field = styledTextField();

        formPanel.add(formRow("Team Number", teamNumberField));
        formPanel.add(formRow("Sponsoring University", universityField));
        formPanel.add(formRow("Student 1 Name", student1Field));
        formPanel.add(formRow("Student 2 Name", student2Field));
        formPanel.add(formRow("Student 3 Name", student3Field));

        formPanel.add(Box.createVerticalStrut(10));
        RoundedButton submitAction = new RoundedButton("Register Team", Theme.PURPLE);
        submitAction.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(submitAction);

        submitAction.addActionListener(event -> {
            String teamNumber = teamNumberField.getText().trim();
            String university = universityField.getText().trim();
            String[] students = { student1Field.getText().trim(), student2Field.getText().trim(),
                    student3Field.getText().trim() };

            if (teamNumber.isEmpty() || university.isEmpty() || students[0].isEmpty() || students[1].isEmpty()
                    || students[2].isEmpty()) {
                JOptionPane.showMessageDialog(applicationFrame, "Please fill in every field before registering.",
                        "Missing information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (competitionList.searchTeam(teamNumber) != null) {
                JOptionPane.showMessageDialog(applicationFrame, "Team number \"" + teamNumber + "\" already exists.",
                        "Duplicate team", JOptionPane.ERROR_MESSAGE);
                return;
            }

            competitionList.registerTeam(teamNumber, university, students);
            registeredTeamNumbers.add(teamNumber);

            teamNumberField.setText("");
            universityField.setText("");
            student1Field.setText("");
            student2Field.setText("");
            student3Field.setText("");

            setStatus("Team " + teamNumber + " registered successfully.");
            JOptionPane.showMessageDialog(applicationFrame, "Team " + teamNumber + " registered successfully.",
                    "Registered", JOptionPane.INFORMATION_MESSAGE);
        });

        formPanel.add(Box.createVerticalGlue());
        return formPanel;
    }

    private JComponent formRow(String labelText, JTextField inputField) {
        JPanel rowPanel = new JPanel();
        rowPanel.setOpaque(false);
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.Y_AXIS));
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rowPanel.setMaximumSize(new Dimension(420, 60));

        JLabel rowLabel = fieldLabel(labelText);
        rowLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputField.setMaximumSize(new Dimension(420, 34));

        rowPanel.add(rowLabel);
        rowPanel.add(Box.createVerticalStrut(4));
        rowPanel.add(inputField);
        rowPanel.add(Box.createVerticalStrut(10));
        return rowPanel;
    }

    private JComponent buildDisplayCard() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);

        JPanel topControls = new JPanel(new BorderLayout());
        topControls.setOpaque(false);
        topControls.add(sectionTitle("All Registered Teams"), BorderLayout.WEST);

        RoundedButton refreshAction = new RoundedButton("Refresh", Theme.PERIWINKLE);
        refreshAction.addActionListener(event -> refreshTeamsTable());

        JPanel refreshWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        refreshWrapper.setOpaque(false);
        refreshWrapper.add(refreshAction);
        topControls.add(refreshWrapper, BorderLayout.EAST);
        containerPanel.add(topControls, BorderLayout.NORTH);

        teamsTableModel = new DefaultTableModel(
                new Object[] { "Team", "University", "Status", "Students", "R1", "R2", "R3", "R4" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable dataTable = new JTable(teamsTableModel);
        styleTable(dataTable);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        scrollPane.getViewport().setBackground(Theme.PANEL_ALT);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private void refreshTeamsTable() {
        competitionList.displayAllTeams();

        teamsTableModel.setRowCount(0);
        for (String teamNumber : registeredTeamNumbers) {
            TeamNode currentTeam = competitionList.searchTeam(teamNumber);
            if (currentTeam == null)
                continue;

            Object[] tableRow = new Object[8];
            tableRow[0] = currentTeam.teamNumber;
            tableRow[1] = currentTeam.sponsoringUniversity;
            tableRow[2] = currentTeam.isEliminated ? "Eliminated" : "Active";
            tableRow[3] = String.join(", ", currentTeam.studentNames);
            for (int index = 0; index < currentTeam.roundScores.length; index++) {
                tableRow[4 + index] = currentTeam.roundScores[index];
            }
            teamsTableModel.addRow(tableRow);
        }
        setStatus(registeredTeamNumbers.isEmpty() ? "No teams registered yet."
                : registeredTeamNumbers.size() + " team(s) on record.");
    }

    private JComponent buildDeleteCard() {
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        formPanel.add(sectionTitle("Delete a Team"));
        formPanel.add(Box.createVerticalStrut(16));

        JTextField teamNumberField = styledTextField();
        formPanel.add(formRow("Team Number", teamNumberField));

        RoundedButton deleteAction = new RoundedButton("Delete Team", Theme.CORAL);
        deleteAction.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(deleteAction);

        deleteAction.addActionListener(event -> {
            String targetTeamNumber = teamNumberField.getText().trim();
            if (targetTeamNumber.isEmpty()) {
                JOptionPane.showMessageDialog(applicationFrame, "Enter a team number to delete.", "Missing information",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            TeamNode existingTeam = competitionList.searchTeam(targetTeamNumber);
            if (existingTeam == null) {
                JOptionPane.showMessageDialog(applicationFrame, "Team \"" + targetTeamNumber + "\" was not found.",
                        "Not found",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirmDelete = JOptionPane.showConfirmDialog(applicationFrame,
                    "Delete Team " + existingTeam.teamNumber + " (" + existingTeam.sponsoringUniversity + ")?",
                    "Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirmDelete != JOptionPane.YES_OPTION) {
                return;
            }

            competitionList.deleteTeam(targetTeamNumber);
            registeredTeamNumbers.remove(targetTeamNumber);
            teamNumberField.setText("");
            setStatus("Team " + targetTeamNumber + " deleted.");
        });

        formPanel.add(Box.createVerticalGlue());
        return formPanel;
    }

    private JComponent buildCaptureCard() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);

        captureHeaderLabel = sectionTitle("Capture Round Scores");
        containerPanel.add(captureHeaderLabel, BorderLayout.NORTH);

        captureFormBody = new JPanel();
        captureFormBody.setOpaque(false);
        captureFormBody.setLayout(new BoxLayout(captureFormBody, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(captureFormBody);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private void refreshCaptureScoresCard() {
        captureFormBody.removeAll();
        captureFields.clear();

        if (currentRound >= TOTAL_ROUNDS) {
            captureHeaderLabel.setText("Capture Round Scores — all " + TOTAL_ROUNDS + " rounds are complete");
            JLabel doneLabel = fieldLabel("Head to \"Find Winner\" to see the result.");
            captureFormBody.add(doneLabel);
            captureFormBody.revalidate();
            captureFormBody.repaint();
            return;
        }

        captureHeaderLabel.setText("Capture Round Scores — Round " + (currentRound + 1) + " of " + TOTAL_ROUNDS);

        List<String> activeTeamNumbers = new ArrayList<>();
        for (String teamNumber : registeredTeamNumbers) {
            TeamNode currentTeam = competitionList.searchTeam(teamNumber);
            if (currentTeam != null && !currentTeam.isEliminated) {
                activeTeamNumbers.add(teamNumber);
            }
        }

        if (activeTeamNumbers.isEmpty()) {
            captureFormBody.add(fieldLabel("No active teams available."));
            captureFormBody.revalidate();
            captureFormBody.repaint();
            return;
        }

        for (String teamNumber : activeTeamNumbers) {
            TeamNode activeTeam = competitionList.searchTeam(teamNumber);
            JTextField scoreInputField = styledTextField();
            captureFields.put(teamNumber, scoreInputField);
            captureFormBody.add(formRow("Team " + activeTeam.teamNumber + " (" + activeTeam.sponsoringUniversity + ")",
                    scoreInputField));
        }

        RoundedButton submitScoresAction = new RoundedButton("Submit Round " + (currentRound + 1) + " Scores",
                Theme.CYAN);
        submitScoresAction.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitScoresAction.addActionListener(event -> submitRoundScores(activeTeamNumbers));
        captureFormBody.add(Box.createVerticalStrut(6));
        captureFormBody.add(submitScoresAction);

        captureFormBody.revalidate();
        captureFormBody.repaint();
    }

    private void submitRoundScores(List<String> activeTeamNumbers) {
        List<String> rawScoresList = new ArrayList<>();
        for (String teamNumber : activeTeamNumbers) {
            String textInput = captureFields.get(teamNumber).getText().trim();
            int numericScore;
            try {
                numericScore = Integer.parseInt(textInput);
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(applicationFrame, "Team " + teamNumber + ": enter a whole number.",
                        "Invalid score",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (numericScore < 0) {
                JOptionPane.showMessageDialog(applicationFrame, "Team " + teamNumber + ": score can't be negative.",
                        "Invalid score", JOptionPane.WARNING_MESSAGE);
                return;
            }
            rawScoresList.add(textInput);
        }

        String simulatedInputStream = String.join("\n", rawScoresList) + "\n";
        Scanner simulatedScanner = new Scanner(simulatedInputStream);

        Set<String> previouslyEliminated = new HashSet<>();
        for (String teamNumber : activeTeamNumbers) {
            TeamNode existingTeam = competitionList.searchTeam(teamNumber);
            if (existingTeam.isEliminated)
                previouslyEliminated.add(teamNumber);
        }

        competitionList.captureScores(currentRound, simulatedScanner);
        currentRound++;

        String newlyEliminatedTeam = null;
        for (String teamNumber : activeTeamNumbers) {
            TeamNode currentTeam = competitionList.searchTeam(teamNumber);
            if (currentTeam.isEliminated && !previouslyEliminated.contains(teamNumber)) {
                newlyEliminatedTeam = teamNumber;
                break;
            }
        }

        if (newlyEliminatedTeam != null) {
            TeamNode knockedOutTeam = competitionList.searchTeam(newlyEliminatedTeam);
            String eliminationMessage = "Team " + knockedOutTeam.teamNumber + " from "
                    + knockedOutTeam.sponsoringUniversity
                    + " has been eliminated.";
            setStatus(eliminationMessage);
            JOptionPane.showMessageDialog(applicationFrame, eliminationMessage, "Round " + currentRound + " result",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            setStatus("Round " + currentRound + " scores recorded.");
        }

        refreshCaptureScoresCard();
    }

    private JComponent buildWinnerCard() {
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        formPanel.add(sectionTitle("Find the Winner"));
        formPanel.add(Box.createVerticalStrut(16));

        RoundedButton determineWinnerAction = new RoundedButton("Determine Winner", Theme.PURPLE);
        determineWinnerAction.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(determineWinnerAction);
        formPanel.add(Box.createVerticalStrut(16));

        winnerResultLabel = new JLabel(" ");
        winnerResultLabel.setFont(Theme.HEADER_FONT);
        winnerResultLabel.setForeground(Theme.CYAN);
        winnerResultLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(winnerResultLabel);

        determineWinnerAction.addActionListener(event -> {
            if (currentRound < TOTAL_ROUNDS) {
                setStatus("Note: only " + currentRound + " of " + TOTAL_ROUNDS + " rounds played so far.");
            }
            competitionList.determineWinner();

            TeamNode winningTeam = null;
            int highestPointsTotal = -1;
            for (String teamNumber : registeredTeamNumbers) {
                TeamNode currentTeam = competitionList.searchTeam(teamNumber);
                if (currentTeam == null || currentTeam.isEliminated)
                    continue;

                int teamTotal = 0;
                for (int score : currentTeam.roundScores)
                    teamTotal += score;

                if (teamTotal > highestPointsTotal) {
                    highestPointsTotal = teamTotal;
                    winningTeam = currentTeam;
                }
            }

            if (winningTeam != null) {
                winnerResultLabel.setText("<html>The winning university is <b>" + winningTeam.sponsoringUniversity
                        + "</b> (Team " + winningTeam.teamNumber + ") with " + highestPointsTotal + " points!</html>");
            } else {
                winnerResultLabel.setForeground(Theme.CORAL);
                winnerResultLabel.setText("No active teams remaining to determine a winner.");
            }
        });

        formPanel.add(Box.createVerticalGlue());
        return formPanel;
    }

    private JComponent buildSearchCard() {
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        formPanel.add(sectionTitle("Search for a Team"));
        formPanel.add(Box.createVerticalStrut(16));

        JTextField searchInputField = styledTextField();
        formPanel.add(formRow("Team Number", searchInputField));

        RoundedButton searchAction = new RoundedButton("Search", Theme.PERIWINKLE);
        searchAction.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(searchAction);
        formPanel.add(Box.createVerticalStrut(16));

        searchResultArea = new JTextArea(6, 40);
        searchResultArea.setEditable(false);
        searchResultArea.setLineWrap(true);
        searchResultArea.setWrapStyleWord(true);
        searchResultArea.setFont(Theme.BODY_FONT);
        searchResultArea.setForeground(Theme.TEXT_PRIMARY);
        searchResultArea.setBackground(Theme.PANEL_ALT);
        searchResultArea.setBorder(new RoundedBorder(8, Theme.TEXT_SECONDARY));
        searchResultArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchResultArea.setMaximumSize(new Dimension(500, 160));
        formPanel.add(searchResultArea);

        searchAction.addActionListener(event -> {
            String teamNumberToFind = searchInputField.getText().trim();
            if (teamNumberToFind.isEmpty()) {
                JOptionPane.showMessageDialog(applicationFrame, "Enter a team number to search.", "Missing information",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            TeamNode matchedTeam = competitionList.searchTeam(teamNumberToFind);
            if (matchedTeam != null) {
                searchResultArea
                        .setText("Found Team " + matchedTeam.teamNumber + " from " + matchedTeam.sponsoringUniversity
                                + "\nStudents: " + String.join(", ", matchedTeam.studentNames) + "\nStatus: "
                                + (matchedTeam.isEliminated ? "Eliminated" : "Active"));
            } else {
                searchResultArea.setText("Team \"" + teamNumberToFind + "\" not found.");
            }
        });

        formPanel.add(Box.createVerticalGlue());
        return formPanel;
    }

    private void confirmExit() {
        int confirmSelection = JOptionPane.showConfirmDialog(applicationFrame, "Exit the competition manager?",
                "Confirm exit",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmSelection == JOptionPane.YES_OPTION) {
            applicationFrame.dispose();
            System.exit(0);
        }
    }
}