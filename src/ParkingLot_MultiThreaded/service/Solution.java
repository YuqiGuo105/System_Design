package ParkingLot_MultiThreaded.service;

import ParkingLot_MultiThreaded.model.ParkingResult;
import ParkingLot_MultiThreaded.model.ParkingSpot;
import ParkingLot_MultiThreaded.untility.Helper01;

import java.util.concurrent.ConcurrentHashMap;

public class Solution implements Q001ParkingLotInterface{
    private Helper01 helper;
    private String[][][] parking;
    private int floors=0;
    private ParkingSpot[][][] parkingSpots;

    private ConcurrentHashMap<String, String> vehicleToSpotMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> ticketToSpotMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ParkingResult> spotIdMap = new ConcurrentHashMap<>();

    private final Object lock = new Object();

    public Solution(){}

    /**
     - use helper.print() and helper.println() for logging
     normal System.out.println() logs won't appear
     - parking[2][8][15] = parking spot at 2nd floor , 8th row and 15th column (0 based index),
     its spotId will be: "2-8-15"
     */
    public void init(Helper01 helper, String[][][] parking) {
        this.helper=helper;
        this.parking=parking;
        this.floors=parking.length;
        // add more initializations code here as you require
        if (floors == 0) {
            helper.println("Parking lot has no floor");
            return;
        }

        int rows = parking[0].length;
        int columns = parking[0][0].length;

        parkingSpots = new ParkingSpot[floors][rows][columns];

        for (int i = 0; i < floors; i++) {
            for (int j = 0; j < parking[i].length; j++) {
                for (int k = 0; k < parking[i][j].length; k++) {
                    String spot = parking[i][j][k];
                    String[] parts = spot.split("-");
                    int type = Integer.parseInt(parts[0]);
                    boolean active = parts[1].equals("1");
                    parkingSpots[i][j][k] = new ParkingSpot(i, j, k, type, active);
                }
            }
        }

        helper.println("parking lot initialized");
    }

    /**
     * ParkingResult status 201 for success, 404 for error
     * vehicleType = 2 or 4 for 2-wheeler or 4-wheeler vehicle
     */
    public ParkingResult park(int vehicleType, String vehicleNumber, String ticketId){
        synchronized (lock) {
            for (int i = 0; i < floors; i++) {
                for (int j = 0; j < parkingSpots[i].length; j++) {
                    for (int k = 0; k < parkingSpots[i][j].length; k++) {
                        ParkingSpot spot = parkingSpots[i][j][k];
                        if (spot.isActive() && !spot.isOccupied() && spot.getType() == vehicleType) {
                            spot.setOccupied(true);
                            String spotId = spot.getSpotId();
                            ParkingResult result = new ParkingResult(201, spotId, vehicleNumber, ticketId);
                            vehicleToSpotMap.put(vehicleNumber, spotId);
                            ticketToSpotMap.put(ticketId, spotId);
                            spotIdMap.put(spotId, result);
                            helper.println("Parked vehicle " + vehicleNumber + " at spot " + spotId);
                            return result;
                        }
                    }
                }
            }
        }
        helper.println("No available spot for vehicle " + vehicleNumber);
        return new ParkingResult(404, "", vehicleNumber, ticketId);
    }

    /**
     * - returns 201 success, 404 : vehicle not found or any other error,
     * - exactly one of spotId, vehicleNumber or ticketId will be non-empty
     */
    public int removeVehicle(String spotId, String vehicleNumber, String ticketId){
        synchronized(lock) {
            String targetSpotId = "";
            if (!spotId.isEmpty()) {
                targetSpotId = spotId;
            } else if (!vehicleNumber.isEmpty()) {
                targetSpotId = vehicleToSpotMap.getOrDefault(vehicleNumber, "");
            } else if (!ticketId.isEmpty()) {
                targetSpotId = ticketToSpotMap.getOrDefault(ticketId, "");
            }

            if (targetSpotId.isEmpty() || targetSpotId.equals("")) {
                helper.println("Failed to remove vehicle: No such vehicle found.");
                return 404;
            }

            ParkingResult result = spotIdMap.get(targetSpotId);
            if (result == null) {
                helper.println("Failed to remove vehicle: No such parking record found.");
                return 404;
            }

            ParkingSpot spot = getSpotById(targetSpotId);
            if (spot == null || !spot.isOccupied()) {
                helper.println("Failed to remove vehicle: Spot is already free.");
                return 404;
            }

            spot.setOccupied(false);
            vehicleToSpotMap.remove(result.getVehicleNumber());
            ticketToSpotMap.remove(result.getTicketId());
            helper.println("Removed vehicle " + result.getVehicleNumber() + " from spot " + targetSpotId);
            return 201;
        }
    }

    /** status = 200 : success, 404 : not found
     * exactly one of spotId, vehicleNumber or ticketId will be non-empty
     */
    public ParkingResult searchVehicle(String spotId, String vehicleNumber, String ticketId){
        synchronized (lock) {
            if (!spotId.isEmpty()) {
                return spotIdMap.getOrDefault(spotId, new ParkingResult(404, spotId, vehicleNumber, ticketId));
            } else if (!vehicleNumber.isEmpty()) {
                String sId = vehicleToSpotMap.getOrDefault(vehicleNumber, "");
                return spotIdMap.getOrDefault(sId, new ParkingResult(404, spotId, vehicleNumber, ticketId));
            } else if (!ticketId.isEmpty()) {
                String sId = ticketToSpotMap.getOrDefault(ticketId, "");
                return spotIdMap.getOrDefault(sId, new ParkingResult(404, spotId, vehicleNumber, ticketId));
            }

            return new ParkingResult(404, spotId, vehicleNumber, ticketId);
        }
    }

    // floor is 0-index based, i.e.  0<=floor<parking.length
    public int getFreeSpotsCount(int floor, int vehicleType){
        synchronized(lock) {
            if (floor < 0 || floor >= floors) {
                helper.println("Invalid floor number: " + floor);
                return 0;
            }
            int count = 0;
            for (int j = 0; j < parkingSpots[floor].length; j++) {
                for (int k = 0; k < parkingSpots[floor][j].length; k++) {
                    ParkingSpot spot = parkingSpots[floor][j][k];
                    if (spot.isActive() && !spot.isOccupied() && spot.getType() == vehicleType) {
                        count++;
                    }
                }
            }
            helper.println("Free spots on floor " + floor + " for vehicle type " + vehicleType + ": " + count);
            return count;
        }
    }

    private ParkingSpot getSpotById(String spotId) {
        String[] parts = spotId.split("-");
        if (parts.length != 3) return null;
        int floor = Integer.parseInt(parts[0]);
        int row = Integer.parseInt(parts[1]);
        int column = Integer.parseInt(parts[2]);
        if (floor < 0 || floor >= parkingSpots.length) return null;
        if (row < 0 || row >= parkingSpots[floor].length) return null;
        if (column < 0 || column >= parkingSpots[floor][row].length) return null;
        return parkingSpots[floor][row][column];
    }
}
