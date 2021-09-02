package com.mvbbb.yim.common.protoc.http.response;

import com.mvbbb.yim.common.protoc.http.ResCode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class GenericResponse <T> {
    private int code;
    private String msg;
    private T data;
    private Date timestamp;

//    public static <T> GenericResponse<T> success(ResCode resCode,T date){
//        return new GenericResponse<>(resCode.getCode(),resCode.getMsg(),date,new Date(System.currentTimeMillis()));
//    }

    public static GenericResponse<Object> success(){
        return new GenericResponse<>(ResCode.SUCCESS.getCode(), ResCode.SUCCESS.getMsg(), null,new Date(System.currentTimeMillis()));
    }

    public static <T> GenericResponse<T> success(T data){
        return new GenericResponse<>(ResCode.SUCCESS.getCode(), ResCode.SUCCESS.getMsg(),data,new Date(System.currentTimeMillis()));
    }

    public static <T> GenericResponse<T> failed(ResCode err, T data){
        return new GenericResponse<>(err.getCode(),err.getMsg(),data,new Date(System.currentTimeMillis()));
    }
    public static <T> GenericResponse<T> failed(ResCode err){
        return new GenericResponse<>(err.getCode(),err.getMsg(),null,new Date(System.currentTimeMillis()));
    }
}
