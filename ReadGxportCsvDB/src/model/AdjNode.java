/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.processor.PreAssignmentProcessor;

/**
 *
 * @author Miguelangel
 */
public class AdjNode {
    @CsvBindByName (column = "FILENAME")
    private String filename;
    @PreAssignmentProcessor(processor = ConvertEmptyOrBlankStringsToDefault.class, paramString = "-1")
    @CsvBindByName (column = "ANI")
    private int ani;
    @CsvBindByName (column = "NAME")
    private String name;
    @CsvBindByName (column = "TRANST")
    private String transt;
    @PreAssignmentProcessor(processor = ConvertEmptyOrBlankStringsToDefault.class, paramString = "-1")
    @CsvBindByName (column = "NODEBID")
    private int nodeBid;
    
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getAni() {
        return ani;
    }

    public void setAni(int ani) {
        this.ani = ani;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranst() {
        return transt;
    }

    public void setTranst(String transt) {
        this.transt = transt;
    }

    public int getNodeBid() {
        return nodeBid;
    }

    public void setNodeBid(int nodeBid) {
        this.nodeBid = nodeBid;
    }

   

    @Override
    public String toString() {
        return "AdjNode{" + "filename=" + filename + ", ani=" + ani + ", name=" + name + ", transt=" + transt + ", nodeBid=" + nodeBid + '}';
    }

    
    
    
    
    
}
