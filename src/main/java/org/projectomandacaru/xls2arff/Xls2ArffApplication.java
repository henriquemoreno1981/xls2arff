package org.projectomandacaru.xls2arff;

import org.apache.commons.io.FilenameUtils;
import org.projectomandacaru.xls2arff.model.CsvToArff;
import org.projectomandacaru.xls2arff.model.SpreadSheetToCsv;
import org.projectomandacaru.xls2arff.utils.SpreadSheetToCsvFactory;
import org.projectomandacaru.xls2arff.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import weka.core.AbstractInstance;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class Xls2ArffApplication implements CommandLineRunner {

    public static final String BUFFER = "5000";
    public static final int DEC = AbstractInstance.s_numericAfterDecimalPoint;

    private boolean processando = false;

    private Logger logger = LoggerFactory.getLogger(Xls2ArffApplication.class);

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Xls2ArffApplication.class);
        builder.headless(false).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0) {
            String nomeArquivo = getFileName(args);
            if (new File(nomeArquivo).exists()) {
                transform(nomeArquivo, null);
            } else {
                logger.error(String.format("Arquivo '%s' não existe", nomeArquivo));
            }
        } else {
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel","xls", "xlsx");
            jFileChooser.setFileFilter(filter);
            int returnVal = jFileChooser.showOpenDialog(new JFrame());
            switch (returnVal) {
                case JFileChooser.APPROVE_OPTION:
                    final JFrame frame = new JFrame("LOG");
                    JTextArea log = new JTextArea();
                    log.setLineWrap(true);
                    log.setEditable(false);
                    frame.add(log);
                    frame.pack();
                    frame.setSize(600, 600);
                    frame.setVisible(true);
                    frame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                            if (!processando || JOptionPane.showConfirmDialog(frame,
                                    "Fechando essa janela vai interromper o processamento?", "Fechar Janela?",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                                System.exit(0);
                            }
                        }
                    });
                    processando = true;
                    transform(jFileChooser.getSelectedFile().getAbsolutePath(), log);
                    processando = false;
                    break;
                default:
                    JOptionPane.showMessageDialog(new JFrame(), "Sem arquivos", "Nenhum arquivo selecionado.", JOptionPane.WARNING_MESSAGE);
                    break;
            }
        }
    }

    private void writeLog(JTextArea log, String str) {
        if (log != null) {
            log.append(str + "\r\n");
            log.update(log.getGraphics());
        }
        logger.info(str);
    }

    private void transform(String nomeArquivo, JTextArea log) {
        writeLog(log, String.format("Abrindo: '%s'", nomeArquivo));
        SpreadSheetToCsv spreadSheetToCsv = SpreadSheetToCsvFactory.factory(nomeArquivo);
        String[] files = null;
        try {
             files = spreadSheetToCsv.readFileToCsv(nomeArquivo, Utils.getPath(nomeArquivo), new String[]{"Cluster1", "Cluster2", "Cluster3"});
        } catch ( IOException e) {
            writeLog(log, String.format("Não foi possivel encontrar '%s'", nomeArquivo));
            return;
        }
        for (String clusterName : files) {
            File csvFile = new File(clusterName);
            writeLog(log, String.format("Gerando arff a partir do '%s'", csvFile.getAbsolutePath()));
            if (csvFile.exists()) {
                try {
                    String arrfFilename = FilenameUtils.removeExtension(clusterName) + ".arff";
                    CsvToArff.convert(clusterName, arrfFilename);
                    writeLog(log, String.format("Arquivo arrf gerado '%s'", arrfFilename));
                    csvFile.delete();
                } catch (IOException e) {
                    writeLog(log, "Ocorreu o seguinte erro: " + e.getMessage());
                }
            } else {
                writeLog(log, String.format("Arquivo '%s' não existe", csvFile.getAbsoluteFile()));
            }
        }
        writeLog(log, "Fim!!!");
    }

    private String getFileName(String[] args) {
        StringBuilder nomeArquivo = new StringBuilder();
        for (int x = 0; x < args.length; x++) {
            if (x > 0) {
                nomeArquivo.append(" ");
            }
            nomeArquivo.append(args[x]);
        }
        return nomeArquivo.toString();
    }
}
