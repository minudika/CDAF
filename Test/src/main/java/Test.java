import org.abithana.frontConnector.Visualizer;

/**
 * Created by acer on 11/23/2016.
 */
public class Test {

   Visualizer v=new Visualizer();
    public static void main(String args[]){
        Test t=new Test();
        t.read();
       // t.timeWiseData();



    }

    public void read(){
        v.readFile("/home/minudika/Codes/Project/FYP/Data/sf_crime/sample.csv", "crimeTable");
        v.doPreprocessing();

        v.executeQueries("Select * from preprocessedData");
        v.categoryWiseData("WARRANTS");
        /*String[] categories={"LARCENY/THEFT","WARRANTS"};
        v.heatMapData(categories);*/
        /*df.show();

        DataFrame df1=ds.getDataFrame("Select Category,count(*) from crimeTable group by Category");
        df1.show();

        for(String s: ds.showColumns()){
            System.out.println(s);
        }
*/
    }

    public void timeWiseData(){
       /* ds.read_file("D:\\CDAP\\sf_crime.csv","crimeTable");
        DataFrame df=ds.getDataFrame("Select * from crimeTable ");


        DataFrame df2=pre.getTimeIndexedDF(df,"Dates");
        df2.show(20);*/
    }
}
