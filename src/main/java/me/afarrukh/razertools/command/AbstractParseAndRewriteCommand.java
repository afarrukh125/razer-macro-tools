package me.afarrukh.razertools.command;

import com.github.rvesse.airline.annotations.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractParseAndRewriteCommand implements Runnable {
    @Option(name = "--file")
    protected String fileName;

    @Option(name = "--outputDir")
    protected String outputDir;

    private static final Logger logger = LoggerFactory.getLogger(AbstractParseAndRewriteCommand.class);

    @Override
    public void run() {
        try {
            File file = determineFile(fileName);
            logger.info("Getting ready to write to file {}", file.getAbsolutePath());
            Document document = getDocument(file);
            logger.info("Executing {}", this.getClass().getSimpleName());
            execute(document);
            Path resultingPath = writeXmlDocumentToXmlFile(document, file.getName());
            logger.info("Execution complete, file output to {}", resultingPath.toAbsolutePath());
        } catch (IOException | ParserConfigurationException | SAXException | UnsupportedLookAndFeelException |
                 ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void execute(Document document);

    final File determineFile(String fileName) throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        if (fileName != null) return new File(fileName);
        JFileChooser chooser = new JFileChooser();
        Path desiredPath;
        String userDir = System.getProperty("user.dir");
        if (outputDir == null) desiredPath = Path.of(userDir);
        else {
            Path outputDirPath = Path.of(userDir, outputDir);
            if (!Files.exists(outputDirPath)) Files.createDirectory(outputDirPath);
            desiredPath = outputDirPath;
        }
        File startLocation = desiredPath.toFile();
        chooser.setCurrentDirectory(startLocation);
        FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("XML", "xml");
        chooser.setFileFilter(fileNameExtensionFilter);
        chooser.addChoosableFileFilter(fileNameExtensionFilter);
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if (!selectedFile.getName().contains(".xml")) {
                JOptionPane.showMessageDialog(null, "Please select a razer macro exported as an XML.");
                System.exit(0);
            }
            return selectedFile;
        }
        throw new IllegalStateException("No file selected");
    }

    final Path writeXmlDocumentToXmlFile(Document xmlDocument, String fileName) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        fileName = fileName.replace(".xml", "_" + this.getClass().getSimpleName() + ".xml");
        Path path = generatePath(fileName);
        try {
            transformer = tf.newTransformer();
            FileOutputStream outStream = new FileOutputStream(path.toFile());
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(outStream));
            return path;
        } catch (FileNotFoundException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    Path generatePath(String fileName) {
        if (outputDir != null) return Path.of(outputDir, fileName);
        else return Path.of(fileName);
    }

    final Document getDocument(File file) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        StringBuilder xmlStringBuilder = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new FileReader(file));

        while ((line = br.readLine()) != null) {
            xmlStringBuilder.append(line).append("\n");
        }

        ByteArrayInputStream input =
                new ByteArrayInputStream(xmlStringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        return builder.parse(input);
    }
}
