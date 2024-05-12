package com.example.warehousemanagement.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Constants {
    //Item constant messages
    public static final String ITEM_NOT_FOUND = "Item not found";
    public static final String ITEM_ALREADY_EXISTS = "Item with that name already exists";
    public static final String ITEM_WITH_ID = "Item with ID ";
    public static final String NOT_FOUND = " not found.";

    //User constant messages
    public static final String USER_NOT_FOUND = "User not found";
    public static final String NOT_AUTHORIZED_TO_CANCEL_ORDER = "Not authorized to cancel this order";
    public static final String NOT_AUTHORIZED = "Not authorized";


    //Order constant messages
    public static final String WRONG_DEADLINE = "Deadline can not be before today";
    public static final String ORDER_NOT_FOUND = "Order not found";
    public static final String ORDER_CANNOT_BE_UPDATED = "Order cannot be updated because its status is not CREATED or DECLINED";
    public static final String INSUFFICIENT_QUANTITY_AVAILABLE = "Insufficient quantity available for item: ";
    public static final String INSUFFICIENT_QUANTITY_IN_INVENTORY = "Insufficient quantity available in inventory: ";
    public static final String THIS_ORDER_IS = "This order is ";

    //truck constant messages
    public static final String TRUCK_NOT_FOUND = "Truck not found";
    public static final String CHASSIS_NUMBER_EXISTS ="Chassis number already exists";
    public static final String LICENSE_PLATE_EXISTS ="License plate already exists";

    //role constant messages
    public static final String ROLE_NOT_FOUND = "Role not found";

}
