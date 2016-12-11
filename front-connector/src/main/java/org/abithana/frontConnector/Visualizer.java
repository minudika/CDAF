package org.abithana.frontConnector;

import org.abithana.ds.CrimeDataStore;
import org.abithana.ds.DataStore;
import org.abithana.preprocessor.facade.PreprocessorFacade;
import org.abithana.stat.facade.StatFacade;
import org.abithana.statBeans.CordinateBean;
import org.abithana.statBeans.HistogramBean;
import org.abithana.utill.Converter;
import org.apache.spark.sql.DataFrame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by acer on 11/23/2016.
 */
public class Visualizer  implements Serializable{

    private String path;
    private String tblName;

    PreprocessorFacade preprocessorFacade=new PreprocessorFacade();
    DataStore dataStore=new CrimeDataStore();

    public void readFile(String path,String tblName){
        this.path="/home/minudika/Codes/Project/FYP/Data/sample.csv";
        //tblName="crimeData";
        dataStore.read_file(path,tblName);
    }
    public void doPreprocessing(String tableName){

        this.tblName=tableName;
        DataFrame df=dataStore.getinitialDataFrame();
        DataFrame f2=preprocessorFacade.handelMissingValues(df);

        List columns= Arrays.asList(f2.columns());
        if(columns.contains("Dates")) {
            f2=preprocessorFacade.getTimeIndexedDF(f2, "Dates");
        }
        f2=preprocessorFacade.dropCol(f2,"resolution");
        f2=preprocessorFacade.dropCol(f2,"descript");
        f2=preprocessorFacade.dropCol(f2,"address");

        f2.show(10);

       // f2=preprocessorFacade.getTimeIndexedDF(f2,"Dates");
        dataStore.setPreprocessDataFrame(f2,tableName);
        /*Runnable preprocessorThread= new Runnable() {
            @Override
            public void run() {
                DataFrame df=dataStore.getinitialDataFrame();
                DataFrame f2=preprocessorFacade.handelMissingValues(df);
                f2.show(50);
            }
        };
        preprocessorThread.run();*/

    }

    public ArrayList<ArrayList> executeQueries(String query){
        try {
            DataFrame dataFrame=dataStore.queryPreprocessedData(query);
            Converter converter=new Converter();
            /*for(ArrayList list:converter.convert(dataFrame)){
                System.out.println(list.toString());
            }*/
            ArrayList<ArrayList> list=converter.convert(dataFrame);
            return list;

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<HistogramBean> categoryWiseData(String category){
       DataFrame df=dataStore.getPreprocessedData();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.categoryTimeData(df,category,tblName);
        return statFacade.getVisualizeList(dataFrame);

    }
    public List<CordinateBean> heatMapData(String[] categories){
        DataFrame df=dataStore.getPreprocessedData();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.categoryWiseCoordinates(df,categories,tblName);
        dataFrame.show(20);
        return statFacade.getCordinateList(dataFrame);

    }

    public List<HistogramBean> yearWiseData(int year){
        DataFrame df=dataStore.getPreprocessedData();
        StatFacade statFacade=new StatFacade();
        DataFrame dataFrame=statFacade.yearCategoryData(df,year,tblName);
        return statFacade.getVisualizeList(dataFrame);

    }

}
