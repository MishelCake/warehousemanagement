package com.example.warehousemanagement.enums;

import lombok.Getter;

@Getter
public enum UserRoles {
    CLIENT("CLIENT"), WAREHOUSE_MANAGER("WAREHOUSE_MANAGER"), SYSTEM_ADMIN("SYSTEM_ADMIN");

    final String value;
    UserRoles(String value) { this.value = value; }
}
