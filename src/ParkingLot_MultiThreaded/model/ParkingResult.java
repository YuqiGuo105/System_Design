package ParkingLot_MultiThreaded.model;

public class ParkingResult {
    private int status;
    private String spotId, vehicleNumber, ticketId;

    public ParkingResult(int status, String spotId, String vehicleNumber, String ticketId){
        this.status=status;
        this.spotId=spotId;
        this.vehicleNumber=vehicleNumber;
        this.ticketId=ticketId;
    }

    public int getStatus(){return status;}
    public String getSpotId(){return spotId;}
    public String getVehicleNumber(){return vehicleNumber;}

    public String getTicketId(){return ticketId;}

    @Override
    public boolean equals(Object obj) {
        if(obj==null) return false;
        ParkingResult result=(ParkingResult) obj;
        return result.getSpotId().equals(spotId)
                && result.getVehicleNumber().equals(vehicleNumber)
                && result.getTicketId().equals(ticketId);
    }

    public String toString(){
        return "[Spot : "+spotId+", vehicle id : "+vehicleNumber+", ticket id : "+ticketId+"]";
    }
}
