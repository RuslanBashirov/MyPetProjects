package ru.transinfocom.nbd2.entity;

public class PredIdsAndDates {
    
    private int predId;
    private String startDate;
    private String finishDate;
    
    public PredIdsAndDates() {
        
    }
    
    public PredIdsAndDates(int predId, String startDate, String finishDate) {
        this.predId = predId;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }
    public int getPredId() {
        return predId;
    }
    public void setPredId(int predId) {
        this.predId = predId;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getFinishDate() {
        return finishDate;
    }
    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }
}
