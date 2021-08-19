package sv.com.devskodigo;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class Aircraft implements DataOperations {
    private int menuOption;
    private Scanner rawData = new Scanner(System.in);

    @Getter @Setter
    private int id;

    @Getter @Setter
    private String model;

    @Getter @Setter
    private int passengersCapacity;

    @Getter @Setter
    private float maxFuel;

    @Getter @Setter
    private int Status;

    XSSFWorkbook workbook;
    XSSFSheet sheet;
    Map<String, Object[]> data;
    int recordCounter = 1; //1 is Spreadsheet's Header
    List cellDataList;
    Iterator rowIterator;

    public Aircraft(){
        readDataset();
        selectOption();
    }
    @Override
    public void readDataset(){
        menuOption = 0;
        rawData = new Scanner(System.in);
        try{
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Aircraft");
            data = new TreeMap<String, Object[]>();
            data.put("1", new Object[] {"ID", "AIRCRAFT_MODEL", "PASSENGER", "MAX_FUEL", "STATUS"});
            System.out.println("Aircraft dataset loaded");
        }catch(Exception ioe){
            System.out.println("Error during reading dataset routine");
            ioe.printStackTrace();
        }
    }

    @Override
    public void selectOption(){
        System.out.println("Aircraft dataset: please type a number equivalent to any of the options above: ");
        int usrContinue = 1;
        do{
            System.out.println("1. Add an Aircraft");
            System.out.println("2. Display data");
            System.out.println("3. Back to Previous Menu");

            try{
                menuOption = rawData.nextInt();
                switch(menuOption){
                    case 1:
                        this.getData();
                        break;
                    case 2:
                        this.searchData(); //updateData
                        break;
                    case 3:
                        usrContinue = 0;
                        break;
                }

            }catch(InputMismatchException ime){
                System.out.println("Please only type integer numbers");
                ime.printStackTrace();
            }
        }while(usrContinue == 1);
    }

    @Override
    public void getData(){
        System.out.println("Please enter the Id Number");
        this.setId(rawData.nextInt());
        //numeric input data does not send enter at the end
        rawData.nextLine();
        System.out.println("Please enter the Aircraft Model");
        this.setModel((rawData.nextLine()));
        System.out.println("Please enter the Pasengger Number");
        this.setPassengersCapacity((rawData.nextInt()));
        rawData.nextLine();
        System.out.println("Please enter the Fuel Capacity");
        this.setMaxFuel((rawData.nextFloat()));
        rawData.nextLine();
        this.setStatus(1);
        this.addData();
    }

    @Override
    public void addData(){
        String localRecordCounter;
        try{
            localRecordCounter = String.valueOf(recordCounter++);
            data.put(localRecordCounter, new Object[] {this.getId(), this.getModel(), this.getPassengersCapacity(), this.getMaxFuel(), this.getStatus()});

            //it is necesary to iterate the data to savbe it into a row
            Set<String> keyset = data.keySet();
            int rownum = 0;
            for (String key : keyset)
            {
                Row row = sheet.createRow(rownum++);
                Object [] objArr = data.get(key);
                int cellnum = 0;
                for (Object obj : objArr)
                {
                    Cell cell = row.createCell(cellnum++);
                    if(obj instanceof String) //name
                        cell.setCellValue((String)obj);
                    else if(obj instanceof Integer) //id
                        cell.setCellValue((Integer)obj);
                    else if(obj instanceof Float) //float
                        cell.setCellValue((Float)obj);
                }
            }
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("Aircraft.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("Your data is saved");
        }catch(Exception e){
            System.out.println("An error has ocurred");
            e.printStackTrace();
        }

    }

    @Override
    public void updateData(int recordId){

    }

    @Override
    public void searchData(){
        int aircraftTarget;
        int updateRowOperation = 0;
        boolean dataFound = false;

        try{
            System.out.println("Displaying current list of Aircraft");
            //searchData routine
            cellDataList = new ArrayList();
            rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = (Row) rowIterator.next();
                Iterator iterator = row.cellIterator();
                List cellTempList = new ArrayList();
                while (iterator.hasNext()) {
                    Cell cell = (Cell) iterator.next();
                    cellTempList.add(cell);
                }
                cellDataList.add(cellTempList);
            }
            if(cellDataList.size() > 0){
                dataFound = true;
                //print the content of the cellDataList
                for (int i = 0; i < cellDataList.size(); i++) {
                    List cellTempList = (List) cellDataList.get(i);
                    for (int j = 0; j < cellTempList.size(); j++) {
                        Cell cell = (Cell) cellTempList.get(j);
                        String stringCellValue = cell.toString();
                        System.out.print(stringCellValue + "\t");
                    }
                    System.out.println();
                }
            }

            //ask the user what other operation will need to execute
            if(dataFound){
                System.out.println("please type Country ID you wish to update:");
                aircraftTarget = rawData.nextInt();
                System.out.println("Please type: 1->Edit Record, 2->Delete Record, 3->Back to PRevious Menu");
                updateRowOperation = rawData.nextInt();

                switch(updateRowOperation){
                    case 1: //updateData
                        this.updateData(aircraftTarget);
                        break;
                    case 2: //deleteData
                        this.deleteData(aircraftTarget);
                        break;
                    case 3:
                        break;
                }//end of switch(usrOpt)
            }//end of dataFound
        }catch(Exception e){
            System.out.println("An error ocurred");
        }

    }

    @Override
    public void deleteData(int recordId){

    }

    @Override
    public void updateStatus(int recordId){

    }
}
