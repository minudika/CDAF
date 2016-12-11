package org.abithana.ds;

import org.abithana.beans.CrimeDataBean;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.storage.StorageLevel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by acer on 11/19/2016.
 */
public class CrimeDataStore implements DataStore, Serializable{

    private static DataFrame input;
    private static DataFrame preprocessedDf;
    private String tableName= "crimeData";

    public void read_file(String filename ,int storage_level){

        // Load the input data to a static Data Frame
         input=sqlContext.read()
                .format("com.databricks.spark.csv")
                .option("header","true")
                .option("inferSchema","true")
                .load(filename);

        input.saveAsTable("crimeData");
        cache_data(storage_level);
    }
    public void read_file(String filename ,String tbleName){

        tableName=tbleName;
        // Load the input data to a static Data Frame
        input= org.abithana.utill.Config.getInstance().getSqlContext().read()
                .format("com.databricks.spark.csv")
                .option("header","true")
                .option("inferSchema","true")
                .load(filename);

        input.registerTempTable(tableName);
        cache_data(1);
    }
    private void cache_data(int storage_level){

        if(storage_level==1)
            input.persist(StorageLevel.MEMORY_ONLY());
        else if(storage_level==2)
            input.persist(StorageLevel.MEMORY_ONLY_SER());
        else if(storage_level==3)
            input.persist(StorageLevel.MEMORY_AND_DISK());
        else
            input.persist(StorageLevel.MEMORY_AND_DISK_SER());
    }


    public JavaRDD getRDD(){

        try{
            DataFrame rdd=sqlContext.sql("Select Dates,DayOfWeek,PdDistrict,Category,X,Y from "+tableName);
            JavaRDD<CrimeDataBean> crimeDataBeanJavaRDD = rdd.javaRDD().map(new Function<Row, CrimeDataBean>() {
                public CrimeDataBean call(Row row) {
                    CrimeDataBean crimeDataBean = new CrimeDataBean(row.getTimestamp(0),row.getString(1),row.getString(2),row.getString(3),row.getDouble(4),row.getDouble(5));
                    return crimeDataBean;
                }
            });
            return crimeDataBeanJavaRDD;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public  JavaRDD<Vector> getDataVector(){

        try{
            DataFrame rdd=sqlContext.sql("Select Dates,DayOfWeek,PdDistrict,Category,X,Y from "+tableName);

            JavaRDD<Vector> crimeDataBeanJavaRDD = rdd.javaRDD().map(new Function<Row, Vector>() {
                public Vector call(Row row) {
                    return Vectors.dense(row.getDouble(4), row.getDouble(5));
                }
            });
            return crimeDataBeanJavaRDD;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public List<Row> getList(String sqlQuery){

        try{
            return sqlContext.sql(sqlQuery).collectAsList();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public DataFrame queryInitialData(String sqlQuery){

        try{
            DataFrame df=sqlContext.sql(sqlQuery);
            return df;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public String[] showColumns(){
        if(input==null){
            return null;
        }
        else{
            return input.columns();
        }
    }
    public DataFrame getinitialDataFrame(){
        return input;
    }

    public void setPreprocessDataFrame(DataFrame df,String tableName){
        preprocessedDf=df;
        preprocessedDf.registerTempTable(tableName);
        cache_data(1);
    }

    public DataFrame queryPreprocessedData(String sqlQuery){

        try{
            DataFrame df=sqlContext.sql(sqlQuery);
            return df;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
    public DataFrame getPreprocessedData(){
        return preprocessedDf;
    }
}
