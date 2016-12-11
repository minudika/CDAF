package org.abithana.ds;

import org.abithana.beans.DataStoreBeans;
import org.abithana.utill.Config;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.List;

;

/**
 * Created by acer on 11/19/2016.
 */
public interface DataStore {

    Config insatnce=Config.getInstance();
    SparkConf conf = insatnce.getConf();
    JavaSparkContext sc  = insatnce.getSc();
    SQLContext sqlContext =  insatnce.getSqlContext();

    /*
    * Read user file and cache the content in RDD
    * int storage level specifies the level of Storage.(MEMORY_ONLY,MEMORY_ONLY_SER ,MEMORY_AND_DISK, MEMORY_AND_DISK_SER)
    *
    */
     void read_file(String filename ,int storage_level);

    void read_file(String filename ,String tbleName);

    DataFrame getinitialDataFrame();

    JavaRDD<DataStoreBeans> getRDD();

    JavaRDD<Vector> getDataVector();

    List<Row> getList(String sqlQuery);

    DataFrame queryPreprocessedData(String sqlQuery);

    DataFrame queryInitialData(String sqlQuery);

    void setPreprocessDataFrame(DataFrame df,String tableName);
    String[] showColumns();
     DataFrame getPreprocessedData();
}
