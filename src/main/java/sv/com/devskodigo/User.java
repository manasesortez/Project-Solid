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

public class User implements DataOperations {

    private int menuOption;
    private Scanner rawData = new Scanner(System.in);

    @Getter @Setter
    private int id;
    @Getter @Setter
    private String firstName;
    @Getter @Setter
    private String lastName;
    @Getter @Setter
    private String accountName;
    @Getter @Setter
    private String password;
    private int status = 1;

    XSSFWorkbook workbook;
    XSSFSheet sheet;
    Map<String, Object[]> data;
    int recordCounter = 1; //1 is Spreadsheet's Header
    List cellDataList;
    Iterator rowIterator;

    public User(){
        super();
        readDataset();
        selectOption();
    }

    @Override
    public void readDataset(){
        menuOption = 0;
        rawData = new Scanner(System.in);
        try{
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("User");
            data = new TreeMap<String, Object[]>();
            data.put("1", new Object[] {"ID", "FIRST_NAME", "LAST_NAME", "ACCOUNT_NAME", "PASSWORD", "STATUS"});
            System.out.println("User dataset loaded");
        }catch(Exception ioe){
            System.out.println("Error during reading dataset routine");
            ioe.printStackTrace();
        }
    }//end of readDataSet()

    @Override
    public void selectOption(){
        System.out.println("User dataset: please type a number equivalent to any of the options above: ");
        int usrContinue = 1;
        do{
            System.out.println("1. Add a User");
            System.out.println("2. Display Users");
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
        System.out.println("Please type id number");
        this.setId(rawData.nextInt());
        //numeric input data does not send enter at the end
        rawData.nextLine();
        System.out.println("Please enter the First Name: ");
        this.setFirstName(rawData.next());

        System.out.println("Please enter the Lastname");
        this.setLastName(rawData.next());

        System.out.println("Please enter Account Name: ");
        this.setAccountName(rawData.next());

        System.out.println("Please enter the password");
        this.setPassword(rawData.next());

        this.addData();

    }
    @Override
    public void addData(){
        String localRecordCounter;
        try{
            localRecordCounter = String.valueOf(recordCounter++);
            data.put(localRecordCounter, new Object[] {this.getId(), this.getFirstName(), this.getLastName(), this.getAccountName(), this.getPassword(), status});

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
            FileOutputStream out = new FileOutputStream(new File("user.xlsx"));
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
        int UserTarget;
        int updateRowOperation = 0;
        boolean dataFound = false;

        try{
            System.out.println("Displaying current list of countries");
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
                System.out.println("please type User ID you wish to update:");
                UserTarget = rawData.nextInt();
                System.out.println("Please type: 1->Edit Record, 2->Delete Record, 3->Back to PRevious Menu");
                updateRowOperation = rawData.nextInt();

                switch(updateRowOperation){
                    case 1: //updateData
                        this.updateData(UserTarget);
                        break;
                    case 2: //deleteData
                        this.deleteData(UserTarget);
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
