package xyz.zzd.book.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RecordInfo {
//    记录号1
    private Integer rId;
//学号1
    private Integer sid;
//书号1
    private String isbn;
//借书日1
    private LocalDate borrowDate;
//还书日1
    private LocalDate returnDate;
    //续借1
    private Integer isRenew;
    //过期1
    private Integer isOverdue;
    //异常
    private Integer isAbnormal;
    //归还
    private Integer isReturn;
    //书名1
    private String bName;
    //作者1
    private String bAuthor;
    //出版社1
    private String bPublishingHouse;
    //简介1
    private String bSummary;
    //图片地址1
    private String bImgPath;
    //图书所在书库1
    private String location;
    //图书索书号1
    private String callNumber;

    public void getBorrowing(Integer rId, Integer rSid, String rIsbn, LocalDateTime borrowTime, LocalDateTime returnTime, Integer isRenew, Integer isOverdue, String bName, String bAuthor, String bPublishingHouse, String bSummary, String bImgPath, String location, String callNumber) {
    }
    public RecordInfo(Integer rId, Integer rSid, String rIsbn, LocalDate borrowTime, LocalDate returnTime,
                      Integer isRenew, Integer isOverdue, String bName, String bAuthor, String bPublishingHouse,
                      String bSummary, String bImgPath, String location, String callNumber){
        this.rId=rId;
        this.sid=rSid;
        this.isbn=rIsbn;
        this.borrowDate=borrowTime;
        this.returnDate=returnTime;
        this.isRenew=isRenew;
        this.isOverdue=isOverdue;
        this.bName=bName;
        this.bAuthor=bAuthor;
        this.bPublishingHouse=bPublishingHouse;
        this.bSummary=bSummary;
        this.bImgPath=bImgPath;
        this.location=location;
        this.callNumber=callNumber;
    }
}
