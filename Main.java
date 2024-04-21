import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import java.io.IOException;
import java.util.Arrays;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


class Main {

    public static void insertionSort(int[] array) {
        for (int j = 1; j < array.length; j++) {
            int key = array[j];
            int i = j - 1;

            while (i >= 0 && array[i] > key) {
                array[i + 1] = array[i];
                i--;
            }
            array[i + 1] = key;
        }
    }

    public static int[] mergeSort(int[] A) {
        int n = A.length;
        if (n <= 1)
            return A;

        int[] left = Arrays.copyOfRange(A, 0, n/2);
        int[] right = Arrays.copyOfRange(A, n/2, n);

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    public static int[] merge(int[] A, int[] B) {
        int[] C = new int[A.length + B.length];
        int i = 0, j = 0, k = 0;

        while (i < A.length && j < B.length) {
            if (A[i] > B[j]) {
                C[k] = B[j];
                j++;
            } else {
                C[k] = A[i];
                i++;
            }
            k++;
        }
        while (i < A.length) {
            C[k] = A[i];
            i++;
            k++;
        }
        while (j < B.length) {
            C[k] = B[j];
            j++;
            k++;
        }
        return C;
    }


    public static int[] countingSort(int[] A, int k) {
        int[] count = new int[k + 1];
        int[] output = new int[A.length];
        int size = A.length;

        for (int i = 0; i < size; i++) {
            int j = A[i];
            count[j]++;
        }

        for (int i = 1; i <= k; i++) {
            count[i] += count[i - 1];
        }

        for (int i = size - 1; i >= 0; i--) {
            int j = A[i];
            count[j]--;
            output[count[j]] = A[i];
        }
        return output;
    }

    public static int linearSearch(int[] A, int x){
        int size = A.length;
        for (int i = 0; i < size; i++){
            if (A[i] == x){
                return i;
            }
        }
        return -1;
    }

    public static int binarySearch(int[] A, int x) {
        int low = 0;
        int high = A.length - 1;
        
        while (high - low > 1) {
            int mid = (high + low) / 2;
            if (A[mid] < x) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        
        if (A[low] == x) {
            return low;
        } else if (A[high] == x) {
            return high;
        }
        
        return -1;
    }

      






    public static void main(String args[]) throws IOException {

        
        String csvFile = "TrafficFlowDataset.csv";
        String line;
        String csvSplitBy = ",";

        List<String> lastParts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            
            br.readLine();


            for (int i = 0; i <= 512; i++) { 
                line = br.readLine();
                String[] parts = line.split(csvSplitBy);
                lastParts.add(parts[6]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    
        // X axis data
        int[] inputAxis = {512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 251282};

        // Create sample data for linear runtime
        double[][] yAxis = new double[3][10];
        yAxis[0] = new double[]{ 223620, 256780,  212870, 242500, 191650, 120870, 140870, 47150, 58690, 110960}; //Linear search (random data)
        yAxis[1] = new double[]{254100, 168630, 179620, 151700, 203580, 214260, 215570, 152840, 252080, 155430}; //Linear search (sorted data)
        yAxis[2] = new double[]{95830, 95865, 96123, 98546, 98568, 99452, 98456,100799, 12566, 138330}; //Binary search (sorted data)



        int sum = 0;

        int[] intArray = new int[lastParts.size()];
        for (int i = 0; i < lastParts.size(); i++) {
            intArray[i] = Integer.parseInt(lastParts.get(i));
        }

        int[] copyArr = intArray;
        mergeSort(copyArr); //sorted
        for (int i = 1; i <= 10000; i++) {         
/* 
            //REVERSED
            int start = 0;
            int end = copyArr.length - 1;
            while (start < end) {
                // Başlangıç ve son elemanları yer değiştir
                int temp = copyArr[start];
                copyArr[start] = copyArr[end];
                copyArr[end] = temp;
                // İndeksleri güncelle
                start++;
                end--;
            }
    */        

/* 
            int max = copyArr[0];
            for (int j = 1; j < 16000; j++) { ////////////////////////////////////////////////////////////////////////////////////////
            if (copyArr[j] > max) {
                max = copyArr[j];
            }
        }
        */
        
            long startTime = System.nanoTime();
            binarySearch(copyArr, 110814);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);  //    / 1000000;  // nano saniyeden milisaniyeye
            
            sum += duration;
        }

        int result = sum / 10;
        
       



        // Save the char as .png and show it
        showAndSaveChart("Sample Test", inputAxis, yAxis);


    }

    public static void showAndSaveChart(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Milliseconds").xAxisTitle("Input Size").build();

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Add a plot for a sorting algorithm
        chart.addSeries("linear search -random data", doubleX, yAxis[0]);
        chart.addSeries("linear search -sorted data", doubleX, yAxis[1]);
        chart.addSeries("binary search -sorted data", doubleX, yAxis[2]);


        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();

        

    }

    



}



/*
 * 
 * 
 * 
 * 
 
        // Add a plot for a sorting algorithm
        chart.addSeries("insertion", doubleX, yAxis[0]);
        chart.addSeries("merge", doubleX, yAxis[1]);
        chart.addSeries("counting", doubleX, yAxis[2]);

        // X axis data
        int[] inputAxis = {512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 251282};

        // Create sample data for linear runtime
        double[][] yAxis = new double[3][10];
        yAxis[0] = new double[]{0, 0, 0, 2, 3, 15, 39, 201, 580, 2325}; //insertion
        yAxis[1] = new double[]{0, 0, 0, 0, 2, 3, 9, 16, 25, 48}; //merge
        yAxis[2] = new double[]{343, 349, 318, 339, 364, 345, 335, 370, 350, 350}; //counting

        // X axis data
        int[] inputAxis = {512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 251282};

        // Create sample data for linear runtime
        double[][] yAxis = new double[3][10];
        yAxis[0] = new double[]{0, 0, 0, 2, 5, 14, 37, 197, 463, 1879 }; //insertion
        yAxis[1] = new double[]{0, 0, 0, 0, 1, 1, 3, 7, 14, 31}; //merge
        yAxis[2] = new double[]{375, 352, 341, 334, 345, 355, 355, 354, 316, 353}; //counting

        // X axis data
        int[] inputAxis = {512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 251282};

        // Create sample data for linear runtime
        double[][] yAxis = new double[3][10];
        yAxis[0] = new double[]{ 0, 1, 3, 7, 30, 90, 376,1075, 4310, 15045}; //insertion
        yAxis[1] = new double[]{0, 0, 0, 1, 2, 3, 7, 15, 33, 47}; //merge
        yAxis[2] = new double[]{336, 349, 318, 340, 364, 345, 335, 318, 350, 303}; //counting

        ----------------------------------------------------------------------------------------------------

        // X axis data
        int[] inputAxis = {512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 251282};

        // Create sample data for linear runtime
        double[][] yAxis = new double[3][10];
        yAxis[0] = new double[]{ 223620, 256780,  212870, 242500, 191650, 120870, 140870, 47150, 58690, 110960}; //Linear search (random data)
        yAxis[1] = new double[]{254100, 168630, 179620, 151700, 203580, 214260, 215570, 152840, 252080, 155430}; //Linear search (sorted data)
        yAxis[2] = new double[]{174630, 95830, 216710, 310970, 311440, 295800, 364530, 138330, 469900, 121130}; //Binary search (sorted data)







 */