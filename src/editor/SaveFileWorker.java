package editor;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveFileWorker extends SwingWorker<String, Object> {

    final private JTextArea textArea;
    final private File file;

    public SaveFileWorker(JTextArea textArea, File file) {
        this.textArea = textArea;
        this.file = file;
    }

    @Override
    protected String doInBackground() throws Exception {
        String text = textArea.getText();
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(text);
            writer.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return text;
    }

    @Override
    protected void done() {
        super.done();
    }
}
