# Design a Parking Lot - Multi-Threaded

Write code for the low-level design of a parking lot system. The parking lot has two kinds of parking spaces:

- Type `2` for **2-wheeler** vehicles
- Type `4` for **4-wheeler** vehicles

## Overview

- The parking lot consists of multiple floors.
- On each floor, parking spots are arranged in rows and columns.
- For simplicity, assume that each floor has the same number of rows and each row has the same number of columns.
- Some parking spots are **inactive**—vehicles cannot be parked in these spots.

## Implementation Details

Your task is to implement the following methods in the `Solution` class:

### 1. `init(Helper helper, String[][][] parking)`

- **Parameters:**
    - `helper`: An instance with methods like `helper.print("")` and `helper.println("")` for printing logs.
    - `parking[i][j][k]`: Represents the parking spot at floor `i`, row `j`, and column `k`.
        - Each item in `parking` is of the following type:
            - `"4-1"`: An active 4-wheeler parking spot.
            - `"2-1"`: An active 2-wheeler parking spot.
            - `"4-0"` or `"2-0"`: Inactive parking spots—you can't park here.

### 2. `park(int vehicleType, String vehicleNumber, String ticketId)`

- **Functionality:**
    - Assigns an empty parking spot to the vehicle.
    - Maps `vehicleNumber` and `ticketId` to the assigned `spotId`.
    - The `spotId` is formatted as `"floor-row-column"`.
        - Example: `parking[2][0][15]` corresponds to `spotId`: `"2-0-15"`.
- **Return:**
    - **Java:** Return a `ParkingResult` object with `status`, `spotId`, `vehicleNumber`, and `ticketId`.
    - **Python:** Return the assigned `spotId` as a string, or an empty string `""` if no spot can be assigned.

### 3. `removeVehicle(String spotId, String vehicleNumber, String ticketId)`

- **Functionality:**
    - Unparks or removes a vehicle from a parking spot.
    - **Exactly one** of `spotId`, `vehicleNumber`, or `ticketId` will be non-blank.
    - `vehicleNumber` and `ticketId` are from those assigned in the `park()` method.
- **Return:**
    - `201` for success.
    - `404` for failure to remove the vehicle.

### 4. `searchVehicle(String spotId, String vehicleNumber, String ticketId)`

- **Functionality:**
    - Searches for a vehicle parked via the `park()` method.
    - **Exactly one** of `spotId`, `vehicleNumber`, or `ticketId` will be non-blank.
    - If the vehicle has been removed, you should still be able to get its last `spotId` using either `vehicleNumber` or `ticketId`.
- **Return:**
    - **Java:** Return a `ParkingResult` object with `status`, `spotId`, `vehicleNumber`, and `ticketId`.
    - **Python:** Return the assigned `spotId` as a string, or an empty string `""` if the vehicle was never parked in this parking lot.

### 5. `getFreeSpotsCount(int floor, int vehicleType)`

- **Functionality:**
    - At any point in time, get the number of free spots of a given vehicle type (`2` or `4` wheeler) on a specified floor.
    - The floor index `0 <= floor < number of floors` from the `init()` method.

## Constraints

- **Vehicle Types:**
    - `type = 2` for two-wheeler vehicles.
    - `type = 4` for four-wheeler vehicles.
- **Parking Lot Dimensions:**
    - `1 <= floors <= 5`
    - `1 <= rows <= 10,000`
    - `1 <= columns <= 10,000`
    - `1 <= rows * columns <= 10,000`
- **Variable Naming Conventions:**
    - **Java:** Use camelCase.
    - **Python:** Use snake_case.

## Input Examples

### Example 1

```plaintext
parking = [
  [
    ["4-1", "4-1", "2-1", "2-0"],
    ["2-1", "4-1", "2-1", "2-1"],
    ["4-0", "2-1", "4-0", "2-1"],
    ["4-1", "4-1", "4-1", "2-1"]
  ]
]
