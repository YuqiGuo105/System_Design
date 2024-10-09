package ParkingLot_MultiThreaded.untility;

public class Helper01 {
    public void print(String s){System.out.print(s);}
    public void println(String s){
        print(s+"\n");
    }
    // returns ""+floor + "-" + row + "-" + column
    public String getSpotId(int floor, int row, int column) {
        return ""+floor + "-" + row + "-" + column;
    }

    // location[0]=floor, location[1]=row, location[2]=column
    public Integer[] getSpotLocation(String spotId) {
        Integer[] location = {-1, -1, -1};
        String[] ss = spotId.split("-");
        for (int i = 0; i < 3 && i < ss.length; i++) {
            location[i] = Integer.parseInt(ss[i]);
        }
        return location;
    }
}
