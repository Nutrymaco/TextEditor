package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class TextEditor extends JFrame {

    public static void main(String[] args) {
        TextEditor textEditor = new TextEditor();

    }

    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800, 500);
        setTitle("The first stage");

        initComponents();

        setVisible(true);
        setLayout(null);
    }

    void initComponents() {
        JTextArea textArea = new JTextArea();
        JButton loadButton = new JButton();
        JButton saveButton = new JButton();
        JMenu fileMenu = new JMenu();
        JMenu searchMenu = new JMenu();
        JMenuBar menuBar = new JMenuBar();
        JTextField searchField = new JTextField();
        JButton startSearchButton = new JButton();
        JButton nextMatchButton = new JButton();
        JButton previousMatchButton = new JButton();
        JMenuItem saveMenuItem = new JMenuItem();
        JMenuItem loadMenuItem = new JMenuItem();
        JMenuItem exitMenuItem = new JMenuItem();
        JMenuItem startSearchMenuItem = new JMenuItem();
        JMenuItem nextMenuItem = new JMenuItem();
        JMenuItem previousMenuItem = new JMenuItem();
        JMenuItem useRegMenuItem = new JMenuItem();
        ScrollPane scrollPane = new ScrollPane();
        JPanel fileButtonPanel = new JPanel();
        JPanel searchButtonPanel = new JPanel();
        JCheckBox regCheckBox = new JCheckBox();
        JFileChooser jfc = new JFileChooser();

        AtomicReference<List<List<Integer>>> searchResults = new AtomicReference<>();
        AtomicReference<Integer> currentMatch = new AtomicReference<>();

        jfc.setName("FileChooser");
        add(jfc);

        Consumer<ActionEvent> saveLambda = e -> {
            jfc.showSaveDialog(this);
            File file = jfc.getSelectedFile();
            new SaveFileWorker(textArea, file).execute();
        };
        Consumer<ActionEvent> loadLambda = l -> {
            jfc.showOpenDialog(this);
            File file = jfc.getSelectedFile();
            new LoadFileWorker(textArea, file).execute();
        };

        Consumer<Integer> selectText = i -> {
            int start = searchResults.get().get(currentMatch.get()).get(0);
            int end = start + searchResults.get().get(currentMatch.get()).get(1);
            textArea.setCaretPosition(end);
            textArea.select(start, end);
            textArea.grabFocus();
        };

        Consumer<ActionEvent> searchLambda = event -> {
            SearchWorker w = new SearchWorker(textArea, searchField, regCheckBox.isSelected());
            w.execute();
            while (w.getState() != SwingWorker.StateValue.DONE);
            searchResults.set(w.getFindResults());
            currentMatch.set(0);
            selectText.accept(currentMatch.get());
//            currentMatch.set(1);
        };

        Consumer<ActionEvent> nextLambda = event -> {
            currentMatch.set(currentMatch.get() + 1);
            if (currentMatch.get().compareTo(searchResults.get().size()) >= 0) {
                currentMatch.set(0);
            }
            selectText.accept(currentMatch.get());
        };

        Consumer<ActionEvent> previousLambda = event -> {
            currentMatch.set(currentMatch.get() - 1);
            if (currentMatch.get() < 0) {
                currentMatch.set(searchResults.get().size() - 1);
            }
            selectText.accept(currentMatch.get());
        };


        loadButton.setName("OpenButton");
        loadButton.setIcon(new ImageIcon("/Users/smykovefim/Documents/MyProjects/Java/TestGUI/icons/icons8-open-dir-64.png"));
        loadButton.addActionListener(e -> loadLambda.accept(e));

        saveButton.setName("SaveButton");
        saveButton.setIcon(new ImageIcon("/Users/smykovefim/Documents/MyProjects/Java/TestGUI/icons/icons8-save-64.png"));
        saveButton.addActionListener(e -> saveLambda.accept(e));

        searchField.setName("SearchField");
        searchField.setPreferredSize(new Dimension(200, 25));

        startSearchButton.setName("StartSearchButton");
        startSearchButton.setIcon(new ImageIcon("/Users/smykovefim/Documents/MyProjects/Java/TestGUI/icons/icons8-find-and-replace-64.png"));
        startSearchButton.addActionListener(e -> searchLambda.accept(e));

        previousMatchButton.setName("PreviousMatchButton");
        previousMatchButton.setIcon(new ImageIcon("/Users/smykovefim/Documents/MyProjects/Java/TestGUI/icons/icons8-back-64.png"));
        previousMatchButton.addActionListener(e -> previousLambda.accept(e));

        nextMatchButton.setName("NextMatchButton");
        nextMatchButton.setIcon(new ImageIcon("/Users/smykovefim/Documents/MyProjects/Java/TestGUI/icons/icons8-front-64.png"));
        nextMatchButton.addActionListener(e -> nextLambda.accept(e));

        saveMenuItem.setName("MenuSave");
        saveMenuItem.setText("save");
        saveMenuItem.addActionListener(e -> saveLambda.accept(e));

        loadMenuItem.setName("MenuOpen");
        loadMenuItem.setText("open");
        loadMenuItem.addActionListener(e -> loadLambda.accept(e));

        textArea.setName("TextArea");

        exitMenuItem.setName("MenuExit");
        exitMenuItem.setText("exit");
        exitMenuItem.addActionListener(e -> dispose());

        scrollPane.setName("ScrollPane");
        scrollPane.add(textArea);
        scrollPane.setSize(300, 100);

        fileButtonPanel.setSize(200, 100);
        fileButtonPanel.add(loadButton);
        fileButtonPanel.add(saveButton);

        regCheckBox.setName("UseRegExCheckbox");
        regCheckBox.setText("use regex");
        regCheckBox.setPreferredSize(new Dimension(100, 25));

        startSearchMenuItem.setName("MenuStartSearch");
        startSearchMenuItem.setText("start search");
        startSearchMenuItem.addActionListener(e -> searchLambda.accept(e));

        previousMenuItem.setName("MenuPreviousMatch");
        previousMenuItem.setText("previous");
        previousMenuItem.addActionListener(e -> previousLambda.accept(e));

        nextMenuItem.setName("MenuNextMatch");
        nextMenuItem.setText("next");
        nextMenuItem.addActionListener(e -> nextLambda.accept(e));

        useRegMenuItem.setName("MenuUseRegExp");
        useRegMenuItem.setText("use regex");
        useRegMenuItem.addActionListener(e -> {
            regCheckBox.doClick();
        });

        searchButtonPanel.setSize(800, 100);
        searchButtonPanel.add(saveButton);
        searchButtonPanel.add(loadButton);
        searchButtonPanel.add(searchField);
        searchButtonPanel.add(startSearchButton);
        searchButtonPanel.add(previousMatchButton);
        searchButtonPanel.add(nextMatchButton);
        searchButtonPanel.add(regCheckBox);

        fileMenu.setName("MenuFile");
        fileMenu.setText("File");
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        fileMenu.add(exitMenuItem);

        searchMenu.setName("MenuSearch");
        searchMenu.setText("Search");
        searchMenu.add(startSearchMenuItem);
        searchMenu.add(previousMenuItem);
        searchMenu.add(nextMenuItem);
        searchMenu.add(useRegMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(searchMenu);

        setJMenuBar(menuBar);
        add(scrollPane, BorderLayout.CENTER);
        add(searchButtonPanel, BorderLayout.PAGE_START);
    }
}
