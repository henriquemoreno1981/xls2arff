package org.projectomandacaru.xls2arrf.model;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;

public class CsvToArff {
    public static void convert(String csvFilename, String arrfFilename) throws IOException {
        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csvFilename));
        Instances data = loader.getDataSet();

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(arrfFilename));
        saver.writeBatch();
        // .arff file will be created in the output location
    }
}
