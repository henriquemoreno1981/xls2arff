package org.projectomandacaru.xls2arff;

import org.apache.commons.io.FilenameUtils;
import org.projectomandacaru.xls2arff.model.CsvToArff;
import org.projectomandacaru.xls2arff.model.SpreadSheetToCsv;
import org.projectomandacaru.xls2arff.utils.SpreadSheetToCsvFactory;
import org.projectomandacaru.xls2arff.utils.Utils;
import org.projectomandacaru.xls2arff.view.JLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class Xls2ArrfApplication implements CommandLineRunner {

    public static final String ARFF_EXTENSION = "arff";

    private boolean processando = false;

    private static Logger LOGGER = LoggerFactory.getLogger(Xls2ArrfApplication.class);

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Xls2ArrfApplication.class);
        builder.headless(false).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0) {
            String nomeArquivo = getFileName(args);
            if (new File(nomeArquivo).exists()) {
                transform(nomeArquivo);
            } else {
                LOGGER.error(String.format("Arquivo '%s' não existe", nomeArquivo));
            }
        } else {
            openInterface();
        }
    }

    private void openInterface() {
        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel","xls", "xlsx");
        jFileChooser.setFileFilter(filter);
        int returnVal = jFileChooser.showOpenDialog(new JFrame());
        switch (returnVal) {
            case JFileChooser.APPROVE_OPTION:
                JFrame frame = new JLog();
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
                transform(jFileChooser.getSelectedFile().getAbsolutePath());
                processando = false;
                break;
            default:
                JOptionPane.showMessageDialog(new JFrame(), "Sem arquivos", "Nenhum arquivo selecionado.", JOptionPane.WARNING_MESSAGE);
                break;
        }
    }

    private void transform(String nomeArquivo) {
        LOGGER.info(String.format("Abrindo: '%s'", nomeArquivo));
        SpreadSheetToCsv spreadSheetToCsv = SpreadSheetToCsvFactory.factory(nomeArquivo);
        String[] files = null;
        try {
             files = spreadSheetToCsv.readFileToCsv(nomeArquivo, Utils.getPath(nomeArquivo), new String[]{"Cluster1", "Cluster2", "Cluster3"});
        } catch ( IOException e) {
            LOGGER.info(String.format("Não foi possivel encontrar '%s'", nomeArquivo));
            return;
        }
        for (String clusterName : files) {
            File csvFile = new File(clusterName);
            LOGGER.info(String.format("Gerando %1s a partir do '%2s'", ARFF_EXTENSION, csvFile.getAbsolutePath()));
            if (csvFile.exists()) {
                try {
                    String arrfFilename = FilenameUtils.removeExtension(clusterName) + "." + ARFF_EXTENSION;
                    CsvToArff.convert(clusterName, arrfFilename);
                    LOGGER.info(String.format("Arquivo %1s gerado '%2s'", ARFF_EXTENSION, arrfFilename));
                    csvFile.delete();
                } catch (IOException e) {
                    LOGGER.info("Ocorreu o seguinte erro: " + e.getMessage());
                }
            } else {
                LOGGER.info(String.format("Arquivo '%s' não existe", csvFile.getAbsoluteFile()));
            }
        }
        LOGGER.info("Fim!!!");
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
