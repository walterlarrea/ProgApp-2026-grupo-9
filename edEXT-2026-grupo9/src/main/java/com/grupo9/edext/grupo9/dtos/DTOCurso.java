/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo9.edext.grupo9.dtos;

import jakarta.persistence.Id;
import java.util.Date;

/**
 *
 * @author Walter
 */
public class DTOCurso {
    private String nombre;
    private String nombreInst;
    private String descripcion;
    private int duracion;
    private int cantHoras;
    private int cantCred;
    private Date fechaReg;
    private String url;
    
    public DTOCurso(String nombreInst, String nombre, String descripcion, int duracion, int cantHoras, int cantCred, Date fechaReg, String url) {
        this.nombreInst = nombreInst;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCred = cantCred;
        this.fechaReg = fechaReg;
        this.url = url;
    }
    
    public String getNombre(){
        return this.nombre;
    }
}
