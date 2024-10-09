package ParkingLot_MultiThreaded.model;

public class ParkingSpot {
    int floor;
    int row;
    int column;
    int type; // 2 或 4
    boolean active;
    boolean occupied;

    public ParkingSpot(int floor, int row, int column, int type, boolean active) {
        this.floor = floor;
        this.row = row;
        this.column = column;
        this.type = type;
        this.active = active;
        this.occupied = false;
    }

    public String getSpotId() {
        return floor + "-" + row + "-" + column;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

}
