package org.abithana.preprocessor.facade;

import org.abithana.preprocessor.impl.MissingDataHandler;
import org.abithana.preprocessor.impl.Preprocessing;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.DataFrame;

/**
 * Created by acer on 11/20/2016.
 */
public class PreprocessorFacade {

    MissingDataHandler missingDataHandler=new MissingDataHandler();
    Preprocessing preprocessing=new Preprocessing();

    public DataFrame handelMissingValues(DataFrame df){
        System.out.println("Running missing value handling");
        return missingDataHandler.deleteRows(df);
    }

    public DataFrame getTimeIndexedDF(DataFrame df,String columnWithTime){
        System.out.println("Time and Date separation");
        return preprocessing.getTimeIndexedDF(df,columnWithTime);
    }

    public DataFrame dropCol(DataFrame df,String column){

        return preprocessing.dropCol(df,column);
    }
    public Column getCol(DataFrame df,String column){

        return preprocessing.getCol(df,column);

    }
    DataFrame aggregateDataFrames(DataFrame f1,DataFrame f2){
        return preprocessing.aggregateDataFrames(f1,f2);
    }

    DataFrame stringIndexing(DataFrame df){
        return preprocessing.stringIndexing(df);
    }

    DataFrame createFeatureFrame(DataFrame f1){
        return preprocessing.createFeatureFrame(f1);

    }

    DataFrame removeDupliates(DataFrame f1){
        return preprocessing.removeDupliates(f1);

    }

    public String[][] getFetureSet() {

        return preprocessing.getFetureSet();
    }

    public void setFetureSet(String[][] fetureSet) {

        preprocessing.setFetureSet(fetureSet);
    }

    public int getRowLimit() {

        return preprocessing.getRowLimit();
    }

    public void setRowLimit(int rowLimit) {

        preprocessing.setRowLimit(rowLimit);
    }


}
