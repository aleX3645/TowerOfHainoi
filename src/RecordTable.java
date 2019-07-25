import java.io.*;
import java.util.ArrayList;

public class RecordTable {

    public RecordTable(){
        try(BufferedReader reader = new BufferedReader(new FileReader("recordTable.txt"))){

            String temp;
            while((temp = reader.readLine())!=null){
                String[] stringPlayer = temp.split("::");

                CellOfTable cell = new CellOfTable();
                cell.Name = stringPlayer[0];
                cell.Moves = Integer.parseInt(stringPlayer[1]);
                cell.Time = Integer.parseInt(stringPlayer[2]);


                table.add(cell);
            }
        }catch (FileNotFoundException fileNotFoundException){ }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    ArrayList<CellOfTable> table = new ArrayList<>();

    public void addNewRecord(CellOfTable newCell){
        if(table.size() == 0){
            table.add(newCell);
            Save();

            return;
        }
        for(int i = 0; i < table.size(); i++){
            if(newCell.Moves < table.get(i).Moves){
                table.add(i,newCell);
                Save();

                return;

            }
            if(newCell.Moves == table.get(i).Moves &&
                    newCell.Time < table.get(i).Time){
                table.add(i, newCell);
                Save();

                return;
            }
        }
        table.add(newCell);
        Save();
    }

    private void Save(){
        try(FileWriter writer = new FileWriter("recordTable.txt",false)){
            writer.write(toString());
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    @Override
    public String toString(){
        String stringTable = "";

        stringTable += table.get(0).Name + "::";
        stringTable += table.get(0).Moves + "::";
        stringTable += table.get(0).Time;
        for(int i = 1; i < table.size(); i++){
            stringTable += "\n";
            stringTable += table.get(i).Name + "::";
            stringTable += table.get(i).Moves + "::";
            stringTable += table.get(i).Time;
        }

        return stringTable;
    }


}



