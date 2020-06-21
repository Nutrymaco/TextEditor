package editor;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoadFileWorker extends SwingWorker<String, Object> {

    private final JTextArea textArea;
    private File file;
    private String text;

    public LoadFileWorker(JTextArea textArea, File file) {
        this.textArea = textArea;
        this.file = file;
    }

    @Override
    protected void done() {
        try {
            textArea.setText(get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground() throws Exception {
        FileInputStream fileInputStream = null;
        byte[] arr = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            textArea.setText("");
        }
        try {
            arr = fileInputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(arr);
    }
}
