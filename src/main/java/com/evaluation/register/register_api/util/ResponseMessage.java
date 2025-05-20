package com.evaluation.register.register_api.util;

public class ResponseMessage {
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String CREATE = "Usuario creado";
    public static final String UPDATE = "Usuario actualizado";
    public static final String DELETE = "Usuario eliminado";

    public static String msg (Integer operation) {
        switch (operation) {
            case 1:
                return SUCCESS;
            case 2:
                return ERROR;
            case 3:
                return CREATE;
            case 4:
                return UPDATE;
            case 5:
                return DELETE;
            default:
                return "";
        }

    }
}
