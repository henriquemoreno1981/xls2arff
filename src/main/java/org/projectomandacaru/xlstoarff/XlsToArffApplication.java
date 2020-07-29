package org.projectomandacaru.xlstoarff;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.projectomandacaru.xlstoarff.model.CsvToArff;
import org.projectomandacaru.xlstoarff.model.Utils;
import org.projectomandacaru.xlstoarff.model.XslToCsv;
import org.projectomandacaru.xlstoarff.model.XslToCsvInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import weka.core.AbstractInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.IncrementalConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class XlsToArffApplication implements CommandLineRunner {

    public static final String BUFFER = "5000";
    public static final int DEC = AbstractInstance.s_numericAfterDecimalPoint;

    private Logger logger = LoggerFactory.getLogger(XlsToArffApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(XlsToArffApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0) {
            StringBuilder nomeArquivo = new StringBuilder();
            for (int x = 0; x < args.length; x++) {
                if (x > 0) {
                    nomeArquivo.append(" ");
                }
                nomeArquivo.append(args[x]);
            }
            if (new File(nomeArquivo.toString()).exists()) {
                logger.info("Abrindo: " + nomeArquivo.toString());
                String[] files = new XslToCsv().readFileToCsv(nomeArquivo.toString(), Utils.getPath(nomeArquivo.toString()));
                for (String clusterName: files) {
                    File csvFile = new File(clusterName);
                    logger.info("Gerando arff a partir do '" + csvFile.getName() + "'");
                    if (csvFile.exists()) {
                        CsvToArff.convert(clusterName, FilenameUtils.removeExtension(clusterName) + ".arff");
                        csvFile.delete();
                    } else {
                        logger.error("Arquivo '" + csvFile.getName() + "' não existe");
                    }
                }
            } else {
                logger.error("Arquivo '" + nomeArquivo.toString() + "' não existe");
            }
        }
    }
}
