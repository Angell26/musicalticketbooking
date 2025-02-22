package musicalticketbooking;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;  // Add this import for Arrays.asList()
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MusicalTicketBooking extends JFrame {
    // Colors for dark theme
    private static final Color DARK_BG = new Color(18, 18, 18);
    private static final Color DARKER_BG = new Color(24, 24, 24);
    private static final Color ACCENT = new Color(86, 90, 255);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(180, 180, 180);
    
    private JTabbedPane tabbedPane;
    private JTextField searchField;
    private JTextField adultTickets, childTickets, seniorTickets;
    private JTextArea receiptArea;
    private String selectedShow, selectedDate, selectedTime;
    private JPanel navigationPanel;

    private double adultPrice = 45.0, childPrice = 31.5, seniorPrice = 36.0;

    private String[][] musicals = {
    {
        "Wicked", 
        "wicked.jpg", 
        "2h 45m | Apollo Victoria Theatre | Age: 7+",
        "£24",
        "Musical",
        "4.8",
        "Fantasy"
    },
    {
        "The Lion King",
        "lionking.jpg",
        "2h 30m | Lyceum Theatre | Age: 6+",
        "£30",
        "Musical",
        "4.9",
        "Family"
    },
    {
        "Mamma Mia!",
        "mammamia.jpg",
        "2h 35m | Novello Theatre | Age: 5+",
        "£20",
        "Musical",
        "4.7",
        "Comedy"
    },
    {
        "& Juliet",
        "juliet.jpg",
        "2h 30m | Shaftesbury Theatre | Age: 6+",
        "£25",
        "Musical",
        "4.8",
        "Romance"
    },
    {
        "Tina: The Tina Turner Musical",
        "tina.jpg",
        "2h 45m | Aldwych Theatre | Age: 12+",
        "£24",
        "Musical",
        "4.8",
        "Biography"
    },
    {
        "Back to the Future: The Musical",
        "backtothefuture.jpg",
        "2h 40m | Adelphi Theatre | Age: 6+",
        "£23.50",
        "Musical",
        "4.9",
        "Sci-Fi"
    }
};
    
      
    
    // ModernButton inner class
    class ModernButton extends JButton {
        public ModernButton(String text) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(true);
            setForeground(TEXT_PRIMARY);
            setBackground(ACCENT);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(ACCENT.brighter());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(ACCENT);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            super.paintComponent(g);
        }
    }

    public MusicalTicketBooking() {
    setTitle("London Musical Tickets");
    setSize(1280, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBackground(DARK_BG);
    
    // Initialize main container with dark theme
    JPanel mainContainer = new JPanel(new BorderLayout());
    mainContainer.setBackground(DARK_BG);
    setContentPane(mainContainer);

    // Create and add components
    add(createHeader(), BorderLayout.NORTH);
    add(createNavigationStrip(), BorderLayout.CENTER);
    
    setLocationRelativeTo(null);
    setVisible(true);
}

    private void initializeTabbedPane() {
    tabbedPane = new JTabbedPane(JTabbedPane.TOP) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 102, 204)); // Blue background
            g2.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    };
    
    // Style the tabs
    tabbedPane.setBackground(new Color(0, 102, 204));
    tabbedPane.setForeground(Color.BLACK); // Black text
    tabbedPane.setBorder(null);
    tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
    
    // Update tab names correctly
    tabbedPane.addTab("Musicals", createMusicalGridPanel());
    tabbedPane.addTab("Date & Time", new JPanel());
    tabbedPane.addTab("Ticket Type", new JPanel());
    tabbedPane.addTab("Tickets", createReceiptPanel());
    
    // Add hover effect
    addTabHoverEffect();
}

private void addTabHoverEffect() {
    tabbedPane.addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            int tabIndex = tabbedPane.indexAtLocation(e.getX(), e.getY());
            if (tabIndex != -1 && tabIndex != tabbedPane.getSelectedIndex()) {
                // Lighter blue for hover
                tabbedPane.setBackgroundAt(tabIndex, new Color(51, 153, 255));
            }
            // Reset other tabs
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                if (i != tabIndex && i != tabbedPane.getSelectedIndex()) {
                    // Back to darker blue
                    tabbedPane.setBackgroundAt(i, new Color(0, 102, 204));
                }
            }
        }
    });
    
    tabbedPane.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseExited(MouseEvent e) {
            // Reset all tabs when mouse exits
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                if (i != tabbedPane.getSelectedIndex()) {
                    tabbedPane.setBackgroundAt(i, new Color(0, 102, 204));
                }
            }
        }
    });
}

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(DARKER_BG);
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Title
        JLabel title = new JLabel("London Musical Tickets");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_PRIMARY);

        // Search Panel with filters
        JPanel searchPanel = createSearchPanel();

        // Exit button
        ModernButton exitButton = new ModernButton("Exit");
        exitButton.setBackground(new Color(255, 69, 58));
        exitButton.addActionListener(e -> System.exit(0));

        header.add(title, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.CENTER);
        header.add(exitButton, BorderLayout.EAST);

        return header;
    }
    
    

    private JPanel createSearchPanel() {
    JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
    searchPanel.setOpaque(false);
    
    // Search field
    searchField = new JTextField();
    searchField.setPreferredSize(new Dimension(300, 35));
    styleSearchField(searchField);
    
    // Filter buttons panel
    JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    filterPanel.setOpaque(false);
    
    // Create filter dropdowns
    JPopupMenu ratingMenu = new JPopupMenu();
    ratingMenu.setBackground(DARKER_BG);
    String[] ratingOptions = {"Highest to Lowest", "Lowest to Highest"};
    addMenuItems(ratingMenu, "Rating", ratingOptions);
    
    JPopupMenu genreMenu = new JPopupMenu();
    genreMenu.setBackground(DARKER_BG);
    // In createSearchPanel method, update the genre options:
String[] genreOptions = {"All", "Family", "Fantasy", "Comedy", "Romance", "Biography", "Sci-Fi"};
    addMenuItems(genreMenu, "Genre", genreOptions);
    
    JPopupMenu ageMenu = new JPopupMenu();
    ageMenu.setBackground(DARKER_BG);
    String[] ageOptions = {"All Ages", "5+", "8+", "12+"};
    addMenuItems(ageMenu, "Age", ageOptions);
    
    // Create and add filter buttons with dropdowns
    ModernButton ratingButton = createFilterDropdownButton("Rating ↓", ratingMenu);
    ModernButton genreButton = createFilterDropdownButton("Genre", genreMenu);
    ModernButton ageButton = createFilterDropdownButton("Age", ageMenu);
    
    filterPanel.add(ratingButton);
    filterPanel.add(genreButton);
    filterPanel.add(ageButton);

    searchPanel.add(searchField, BorderLayout.CENTER);
    searchPanel.add(filterPanel, BorderLayout.EAST);
    
    return searchPanel;
}

private void addMenuItems(JPopupMenu menu, String filterType, String[] options) {
    menu.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
    
    for (String option : options) {
        JMenuItem item = new JMenuItem(option);
        item.setBackground(Color.WHITE);
        item.setForeground(Color.BLACK);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(230, 241, 255));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(Color.WHITE);
            }
        });
        
        item.addActionListener(e -> {
            applyFilter(filterType, option);
            // Update the button text to show current selection
            ((ModernButton)menu.getInvoker()).setText(filterType + ": " + option + " ▼");
        });
        
        menu.add(item);
    }
}

private ModernButton createFilterDropdownButton(String text, JPopupMenu menu) {
    ModernButton button = new ModernButton(text + " ▼");
    button.setBackground(Color.WHITE);
    button.setForeground(Color.BLACK);
    button.setPreferredSize(new Dimension(120, 35));
    button.setFont(new Font("Segoe UI", Font.BOLD, 12));
    
    // Show menu on click
    button.addActionListener(e -> {
        menu.show(button, 0, button.getHeight());
    });
    
    // Add hover effect
    button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(new Color(230, 241, 255)); // Light blue hover
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(Color.WHITE);
        }
    });
    
    return button;
}

    private void styleSearchField(JTextField field) {
        field.setBackground(DARKER_BG.brighter());
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Add search functionality
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { searchMusicals(); }
            public void removeUpdate(DocumentEvent e) { searchMusicals(); }
            public void insertUpdate(DocumentEvent e) { searchMusicals(); }
        });
    }

    private ModernButton createFilterButton(String text) {
    ModernButton button = new ModernButton(text);
    button.setBackground(DARKER_BG.brighter());
    button.setPreferredSize(new Dimension(100, 35));
    
    // Add hover effect
    button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(ACCENT);
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(DARKER_BG.brighter());
        }
    });
    
    return button;
}
    
// Implement search functionality
private void searchMusicals() {
    String searchTerm = searchField.getText().toLowerCase();
    JPanel gridPanel = (JPanel) ((JScrollPane) tabbedPane.getComponentAt(0)).getViewport().getView();
    gridPanel.removeAll();
    
    for (String[] musical : musicals) {
        if (musical[0].toLowerCase().contains(searchTerm) || 
            musical[4].toLowerCase().contains(searchTerm) ||
            musical[6].toLowerCase().contains(searchTerm)) {
            gridPanel.add(createMusicalCard(musical));
        }
    }
    
    gridPanel.revalidate();
    gridPanel.repaint();
}

   private void applyFilter(String filterType, String option) {
    ArrayList<String[]> filteredMusicals = new ArrayList<>(Arrays.asList(musicals));
    
    switch (filterType) {
        case "Rating":
            if (option.equals("Highest to Lowest")) {
                filteredMusicals.sort((a, b) -> Double.compare(
                    Double.parseDouble(b[5]),
                    Double.parseDouble(a[5])
                ));
            } else {
                filteredMusicals.sort((a, b) -> Double.compare(
                    Double.parseDouble(a[5]),
                    Double.parseDouble(b[5])
                ));
            }
            break;
            
        case "Genre":
            if (!option.equals("All")) {
                filteredMusicals.removeIf(musical -> !musical[4].equals(option));
            }
            break;
            
        case "Age":
            if (!option.equals("All Ages")) {
                int filterAge = Integer.parseInt(option.replace("+", ""));
                filteredMusicals.removeIf(musical -> {
                    int musicalAge = Integer.parseInt(
                        musical[2].split("Age: ")[1].replace("+", "")
                    );
                    return musicalAge != filterAge;
                });
            }
            break;
    }
    
    updateGridWithFilteredMusicals(filteredMusicals);
}
private void updateFilterButtonText(String filterType, String option) {
    // Update the filter button text to show current selection
    for (Component comp : ((JPanel)searchField.getParent()).getComponents()) {
        if (comp instanceof JPanel) {
            for (Component btn : ((JPanel)comp).getComponents()) {
                if (btn instanceof ModernButton) {
                    ModernButton filterBtn = (ModernButton)btn;
                    if (filterBtn.getText().startsWith(filterType)) {
                        filterBtn.setText(filterType + ": " + option + " ▼");
                        break;
                    }
                }
            }
        }
    }
}

    private void updateGridWithFilteredMusicals(ArrayList<String[]> filteredMusicals) {
        JPanel gridPanel = (JPanel) ((JScrollPane) tabbedPane.getComponentAt(0)).getViewport().getView();
        gridPanel.removeAll();
        
        for (String[] musical : filteredMusicals) {
            gridPanel.add(createMusicalCard(musical));
        }
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createNavigationStrip() {
    navigationPanel = new JPanel(new BorderLayout());
    navigationPanel.setBackground(DARK_BG);

    // Initialize tabbedPane if not already initialized
    if (tabbedPane == null) {
        initializeTabbedPane();
    }
    
    navigationPanel.add(tabbedPane, BorderLayout.CENTER);
    return navigationPanel;
}
    
    

    private JPanel createMusicalGridPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DARK_BG);
        
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBackground(DARK_BG);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (String[] musical : musicals) {
            gridPanel.add(createMusicalCard(musical));
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBackground(DARK_BG);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createMusicalCard(String[] musical) {
    JPanel card = new JPanel(new BorderLayout(0, 10));
    card.setBackground(DARKER_BG);
    card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // Image placeholder with gradient background
    JPanel imagePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(40, 40, 40),
                0, getHeight(), new Color(30, 30, 30)
            );
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    };
    imagePanel.setPreferredSize(new Dimension(0, 200));

    // Details panel
    JPanel detailsPanel = new JPanel(new GridBagLayout());
    detailsPanel.setBackground(DARKER_BG);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(2, 0, 2, 0);

    // Title
    JLabel titleLabel = new JLabel(musical[0]);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    titleLabel.setForeground(TEXT_PRIMARY);
    detailsPanel.add(titleLabel, gbc);

    // Description
    JLabel descLabel = new JLabel(musical[2]);
    descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    descLabel.setForeground(TEXT_SECONDARY);
    detailsPanel.add(descLabel, gbc);

    // Price
    JLabel priceLabel = new JLabel("From " + musical[3]);
    priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
    priceLabel.setForeground(ACCENT);
    detailsPanel.add(priceLabel, gbc);

    // Rating
    JLabel ratingLabel = new JLabel("★ " + musical[5]);
    ratingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    ratingLabel.setForeground(TEXT_SECONDARY);
    detailsPanel.add(ratingLabel, gbc);

    // Book button with new implementation
    ModernButton bookButton = new ModernButton("Book Now");
    bookButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectedShow = musical[0];
            // Create and set the date & time panel
            JPanel dateTimePanel = createSchedulePanel();
            tabbedPane.setComponentAt(1, dateTimePanel);
            // Switch to the date & time tab
            tabbedPane.setSelectedIndex(1);
            // Ensure UI updates
            revalidate();
            repaint();
        }
    });

    // Add hover effect to card
    card.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 1),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
            ));
            card.setBackground(DARKER_BG.brighter());
            bookButton.setBackground(ACCENT.brighter());
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            card.setBackground(DARKER_BG);
            bookButton.setBackground(ACCENT);
        }
    });

    card.add(imagePanel, BorderLayout.NORTH);
    card.add(detailsPanel, BorderLayout.CENTER);
    card.add(bookButton, BorderLayout.SOUTH);

    return card;
}
    
private void showBookingDetails(String title) {
    selectedShow = title;
    tabbedPane.setComponentAt(1, createSchedulePanel());
    tabbedPane.setSelectedIndex(1);
}

    private JPanel createSchedulePanel() {
    JPanel mainPanel = new JPanel(new BorderLayout(30, 0));
    mainPanel.setBackground(Color.WHITE);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    // Left Panel - Show Details
    JPanel showDetailsPanel = createShowDetailsPanel();
    showDetailsPanel.setPreferredSize(new Dimension(350, 0));

    // Right Panel - Date & Time Selection
    JPanel selectionPanel = createDateTimeSelectionPanel();
  
    
    mainPanel.add(showDetailsPanel, BorderLayout.WEST);
    mainPanel.add(selectionPanel, BorderLayout.CENTER);

    return mainPanel;
}


    private JPanel createShowDetailsPanel() {
    JPanel panel = new JPanel(new BorderLayout(0, 20));
    panel.setBackground(DARKER_BG);
    panel.setBorder(createRoundedBorder());

    // Show Image (Placeholder)
    JPanel imagePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(40, 40, 40),
                0, getHeight(), new Color(30, 30, 30)
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        }
    };
    imagePanel.setPreferredSize(new Dimension(0, 300));

    // Show Information
    JPanel infoPanel = new JPanel(new GridBagLayout());
    infoPanel.setBackground(DARKER_BG);
    infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(5, 0, 5, 0);

    // Find selected show details
    for (String[] musical : musicals) {
        if (musical[0].equals(selectedShow)) {
            addDetailLabel(infoPanel, "Show", musical[0], gbc, true);
            addDetailLabel(infoPanel, "Duration", musical[2].split("\\|")[0].trim(), gbc, false);
            addDetailLabel(infoPanel, "Venue", musical[2].split("\\|")[1].trim(), gbc, false);
            addDetailLabel(infoPanel, "Age", musical[2].split("\\|")[2].trim(), gbc, false);
            addDetailLabel(infoPanel, "Genre", musical[4], gbc, false);
            addDetailLabel(infoPanel, "Rating", "★ " + musical[5], gbc, false);
            break;
        }
    }

    panel.add(imagePanel, BorderLayout.NORTH);
    panel.add(infoPanel, BorderLayout.CENTER);

    return panel;
}

    private JPanel createDateTimeSelectionPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(DARKER_BG);
    panel.setBorder(createRoundedBorder());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 20, 10, 20);

    // Date Selection
    JLabel dateLabel = new JLabel("Select Date");
    dateLabel.setForeground(TEXT_PRIMARY);
    dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    panel.add(dateLabel, gbc);

    // Date Grid
    JPanel dateGrid = new JPanel(new GridLayout(2, 4, 10, 10));
    dateGrid.setOpaque(false);
    
    ButtonGroup dateGroup = new ButtonGroup();
    LocalDate today = LocalDate.now();
    
    for (int i = 0; i < 8; i++) {
        LocalDate date = today.plusDays(i);
        JToggleButton dateBtn = createDateButton(date);
        dateGroup.add(dateBtn);
        dateGrid.add(dateBtn);
    }
    
    gbc.insets = new Insets(10, 20, 30, 20);
    panel.add(dateGrid, gbc);

    // Time Selection
    gbc.insets = new Insets(10, 20, 10, 20);
    JLabel timeLabel = new JLabel("Select Time");
    timeLabel.setForeground(TEXT_PRIMARY);
    timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    panel.add(timeLabel, gbc);

    // Time Grid
    JPanel timeGrid = new JPanel(new GridLayout(2, 2, 10, 10));
    timeGrid.setOpaque(false);
    
    ButtonGroup timeGroup = new ButtonGroup();
    String[] times = {"14:00", "15:30", "19:00", "19:30"};
    
    for (String time : times) {
        JToggleButton timeBtn = createTimeButton(time);
        timeGroup.add(timeBtn);
        timeGrid.add(timeBtn);
    }
    
    gbc.insets = new Insets(10, 20, 30, 20);
    panel.add(timeGrid, gbc);

    // Continue Button
    gbc.insets = new Insets(20, 20, 20, 20);
    ModernButton continueButton = new ModernButton("Continue to Tickets");
    continueButton.addActionListener(e -> {
        if (selectedDate != null && selectedTime != null) {
            tabbedPane.setSelectedIndex(2);
            tabbedPane.setComponentAt(2, createTicketTypePanel());
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select both date and time",
                "Selection Required",
                JOptionPane.WARNING_MESSAGE);
        }
    });
    panel.add(continueButton, gbc);

    return panel;
}

    private void addDetailLabel(JPanel panel, String label, String value, GridBagConstraints gbc, boolean isTitle) {
    JLabel labelComp = new JLabel(label + ":");
    labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    labelComp.setForeground(TEXT_SECONDARY);
    panel.add(labelComp, gbc);

    JLabel valueComp = new JLabel(value);
    valueComp.setFont(new Font("Segoe UI", isTitle ? Font.BOLD : Font.PLAIN, isTitle ? 18 : 14));
    valueComp.setForeground(TEXT_PRIMARY);
    panel.add(valueComp, gbc);
}

    private JToggleButton createDateButton(LocalDate date) {
    String displayText = date.format(DateTimeFormatter.ofPattern("MMM d\nEEE"));
    JToggleButton btn = new JToggleButton(displayText) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (isSelected()) {
                g2.setColor(new Color(179, 217, 255)); // Light blue for selection
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            super.paintComponent(g);
        }
    };

    btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btn.setForeground(Color.BLACK); // Black text
    btn.setBackground(Color.WHITE); // White background
    btn.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    btn.setFocusPainted(false);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Add hover effect
    btn.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (!btn.isSelected()) {
                btn.setBackground(new Color(230, 241, 255)); // Very light blue for hover
            }
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            if (!btn.isSelected()) {
                btn.setBackground(Color.WHITE);
            }
        }
    });

    btn.addActionListener(e -> selectedDate = date.toString());
    
    return btn;
}
    
    
    private JToggleButton createTimeButton(String time) {
    JToggleButton btn = new JToggleButton(time) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (isSelected()) {
                g2.setColor(new Color(179, 217, 255)); // Light blue for selection
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            super.paintComponent(g);
        }
    };

    btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btn.setForeground(Color.BLACK); // Black text
    btn.setBackground(Color.WHITE); // White background
    btn.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    btn.setFocusPainted(false);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Add hover effect
    btn.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (!btn.isSelected()) {
                btn.setBackground(new Color(230, 241, 255)); // Very light blue for hover
            }
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            if (!btn.isSelected()) {
                btn.setBackground(Color.WHITE);
            }
        }
    });

    btn.addActionListener(e -> selectedTime = time);
    
    return btn;
}
    
    
    
private JPanel createTicketTypePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Select Tickets");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Ticket selection cards
        JPanel ticketsContainer = new JPanel(new GridLayout(3, 1, 15, 15));
        ticketsContainer.setBackground(DARK_BG);

        // Initialize ticket fields
        adultTickets = new JTextField("0", 3);
        childTickets = new JTextField("0", 3);
        seniorTickets = new JTextField("0", 3);

        // Style text fields
        styleTicketTextField(adultTickets);
        styleTicketTextField(childTickets);
        styleTicketTextField(seniorTickets);

        // Create ticket type cards
        ticketsContainer.add(createTicketCard("Adult", "Ages 18-64", adultTickets, adultPrice));
        ticketsContainer.add(createTicketCard("Student", "Ages 5-17", childTickets, childPrice));
        ticketsContainer.add(createTicketCard("Senior", "Ages 65+", seniorTickets, seniorPrice));

        mainPanel.add(ticketsContainer, BorderLayout.CENTER);

        // Summary and confirm panel
        JPanel summaryPanel = new JPanel(new BorderLayout(0, 15));
        summaryPanel.setBackground(DARKER_BG);
        summaryPanel.setBorder(createRoundedBorder());

        // Total price display
        JLabel totalLabel = new JLabel("Total: £0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(ACCENT);
        summaryPanel.add(totalLabel, BorderLayout.CENTER);

        // Add document listeners to update total
        DocumentListener updateTotal = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateTotalPrice(totalLabel); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateTotalPrice(totalLabel); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateTotalPrice(totalLabel); }
        };

        adultTickets.getDocument().addDocumentListener(updateTotal);
        childTickets.getDocument().addDocumentListener(updateTotal);
        seniorTickets.getDocument().addDocumentListener(updateTotal);

        // Confirm button
        ModernButton confirmButton = new ModernButton("Confirm Booking");
        confirmButton.addActionListener(e -> {
            if (validateTicketSelection()) {
                showReceipt();
            }
        });
        summaryPanel.add(confirmButton, BorderLayout.SOUTH);

        mainPanel.add(summaryPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void styleTicketTextField(JTextField field) {
        field.setBackground(DARKER_BG.brighter());
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARKER_BG.brighter().brighter(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private JPanel createTicketCard(String type, String ageRange, JTextField quantityField, double price) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(DARKER_BG);
        card.setBorder(createRoundedBorder());

        // Left panel for type and description
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 0));
        infoPanel.setOpaque(false);
        
        JLabel typeLabel = new JLabel(type);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        typeLabel.setForeground(TEXT_PRIMARY);
        
        JLabel ageLabel = new JLabel(ageRange);
        ageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ageLabel.setForeground(TEXT_SECONDARY);
        
        infoPanel.add(typeLabel);
        infoPanel.add(ageLabel);

        // Center panel for price
        JLabel priceLabel = new JLabel(String.format("£%.2f", price));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setForeground(ACCENT);

        // Right panel for quantity controls
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        quantityPanel.setOpaque(false);

        ModernButton minusButton = new ModernButton("-");
        minusButton.setPreferredSize(new Dimension(35, 35));
        minusButton.addActionListener(e -> decrementQuantity(quantityField));

        ModernButton plusButton = new ModernButton("+");
        plusButton.setPreferredSize(new Dimension(35, 35));
        plusButton.addActionListener(e -> incrementQuantity(quantityField));

        quantityPanel.add(minusButton);
        quantityPanel.add(quantityField);
        quantityPanel.add(plusButton);

        // Add components to card
        card.add(infoPanel, BorderLayout.WEST);
        card.add(priceLabel, BorderLayout.CENTER);
        card.add(quantityPanel, BorderLayout.EAST);

        return card;
    }

    private void incrementQuantity(JTextField field) {
        try {
            int value = Integer.parseInt(field.getText());
            field.setText(String.valueOf(value + 1));
        } catch (NumberFormatException e) {
            field.setText("1");
        }
    }

    private void decrementQuantity(JTextField field) {
        try {
            int value = Integer.parseInt(field.getText());
            if (value > 0) {
                field.setText(String.valueOf(value - 1));
            }
        } catch (NumberFormatException e) {
            field.setText("0");
        }
    }

    private void updateTotalPrice(JLabel totalLabel) {
        try {
            int adults = Integer.parseInt(adultTickets.getText());
            int children = Integer.parseInt(childTickets.getText());
            int seniors = Integer.parseInt(seniorTickets.getText());

            double total = (adults * adultPrice) + (children * childPrice) + (seniors * seniorPrice);
            totalLabel.setText(String.format("Total: £%.2f", total));
        } catch (NumberFormatException e) {
            totalLabel.setText("Total: £0.00");
        }
    }

    private boolean validateTicketSelection() {
        try {
            int adults = Integer.parseInt(adultTickets.getText());
            int children = Integer.parseInt(childTickets.getText());
            int seniors = Integer.parseInt(seniorTickets.getText());

            if (adults + children + seniors == 0) {
                JOptionPane.showMessageDialog(this,
                    "Please select at least one ticket",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid ticket quantity",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private Border createRoundedBorder() {
        return BorderFactory.createCompoundBorder(
            new Border() {
                @Override
                public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(DARKER_BG.brighter());
                    g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15);
                }

                @Override
                public Insets getBorderInsets(Component c) {
                    return new Insets(1, 1, 1, 1);
                }

                @Override
                public boolean isBorderOpaque() {
                    return true;
                }
            },
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );
    }

    private void showReceipt() {
        int adults = Integer.parseInt(adultTickets.getText());
        int children = Integer.parseInt(childTickets.getText());
        int seniors = Integer.parseInt(seniorTickets.getText());

        double totalPrice = (adults * adultPrice) + (children * childPrice) + (seniors * seniorPrice);

        StringBuilder receipt = new StringBuilder();
        receipt.append("Show: ").append(selectedShow).append("\n\n");
        receipt.append("Date: ").append(selectedDate).append("\n");
        receipt.append("Time: ").append(selectedTime).append("\n\n");
        receipt.append("Tickets:\n");
        if (adults > 0) receipt.append("Adult: ").append(adults).append(" x £").append(adultPrice).append("\n");
        if (children > 0) receipt.append("Child: ").append(children).append(" x £").append(childPrice).append("\n");
        if (seniors > 0) receipt.append("Senior: ").append(seniors).append(" x £").append(seniorPrice).append("\n");
        receipt.append("\nTotal Price: £").append(String.format("%.2f", totalPrice));

        receiptArea.setText(receipt.toString());
        tabbedPane.setSelectedIndex(3);
    }

    private JPanel createReceiptPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Receipt card
        JPanel receiptCard = new JPanel(new BorderLayout(0, 15));
        receiptCard.setBackground(DARKER_BG);
        receiptCard.setBorder(createRoundedBorder());

        // Title
        JLabel titleLabel = new JLabel("Booking Confirmation");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        receiptCard.add(titleLabel, BorderLayout.NORTH);

        // Receipt content
        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setBackground(DARKER_BG);
        receiptArea.setForeground(TEXT_PRIMARY);
        receiptArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        receiptArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        receiptArea.setLineWrap(true);
        receiptArea.setWrapStyleWord(true);

        // Add receipt card to main panel
        receiptCard.add(receiptArea, BorderLayout.CENTER);
        mainPanel.add(receiptCard, BorderLayout.CENTER);

        // Add a print button at the bottom
        ModernButton printButton = new ModernButton("Print Receipt");
        printButton.addActionListener(e -> {
            try {
                receiptArea.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error printing receipt: " + ex.getMessage(),
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(DARK_BG);
        buttonPanel.add(printButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MusicalTicketBooking();
        });
    }
}