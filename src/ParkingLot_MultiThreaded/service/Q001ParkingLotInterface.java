package ParkingLot_MultiThreaded.service;

import ParkingLot_MultiThreaded.model.ParkingResult;
import ParkingLot_MultiThreaded.untility.Helper01;

public interface Q001ParkingLotInterface {
    void init(Helper01 helper, String [][][] parking);
    ParkingResult park(int vehicleType, String vehicleNumber, String ticketId);
    int removeVehicle(String spotId, String vehicleNumber, String ticketId);
    ParkingResult searchVehicle(String spotId, String vehicleNumber, String ticketId);
    int getFreeSpotsCount(int floor, int vehicleType);
}
