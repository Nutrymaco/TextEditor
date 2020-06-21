package editor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchWorker extends SwingWorker<List<List<Integer>>, Object> {

    private Matcher matcher;
    private final JTextArea textArea;
    private final JTextField regField;
    private final boolean isReg;
    private final List<List<Integer>> findResults = new ArrayList<>();

    public SearchWorker(JTextArea textArea, JTextField regField, boolean isReg) {
        this.textArea = textArea;
        this.regField = regField;
        this.isReg = isReg;
    }

    @Override
    protected List<List<Integer>> doInBackground() throws Exception {
        Pattern pattern;
        if (isReg) {
            pattern = Pattern.compile(regField.getText());
        } else{
            pattern = Pattern.compile(regField.getText(), Pattern.LITERAL);
        }
        matcher = pattern.matcher(textArea.getText());
        while (matcher.find()) {
            String find = matcher.group();
            findResults.add(List.of(matcher.start(), find.length()));
        }
        return findResults;
    }

    @Override
    protected void done() {
        super.done();
    }

    public List<List<Integer>> getFindResults() {
        return findResults;
    }
}
