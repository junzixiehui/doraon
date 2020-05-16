package com.junzixiehui.doraon.business.event.custom.exception;


public class InfraException extends RuntimeException{
    
    private static final long serialVersionUID = 1L;
    
    public InfraException(String errMessage){
        super(errMessage);
    }
    
    public InfraException(String errMessage, Throwable e) {
        super(errMessage, e);
    }
}