package xyz.zzd.book.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zzd.book.common.GlobalResult;
import xyz.zzd.book.entity.Book;
import xyz.zzd.book.entity.Record;
import xyz.zzd.book.mapper.BookMapper;
import xyz.zzd.book.mapper.RecordMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    BookMapper bookMapper;

    public Book selectBook(String isbn){
        Book book = bookMapper.selectById(isbn);
        return book;
    }

    public List<Book> queryBooks(String content){
        List<Book> books = bookMapper.selectList(new LambdaQueryWrapper<Book>()
                .like(Book::getIsbn, content).or()
                .like(Book::getBName, content).or()
                .like(Book::getBAuthor, content)
        );
        System.out.println(books);
        return books;

    }

    public GlobalResult checkBook(String isbn) {
        Book book = bookMapper.selectById(isbn);
        if (book==null){
            return GlobalResult.errorTokenMsg("找不到该书籍");
        }
        if (book.getBReserve()==0){
            return GlobalResult.errorTokenMsg("该书籍已借出，请检查是否已借入，若未借入，清联系工作人员");
        }else return GlobalResult.ok(book);


    }

    @Autowired
    RecordMapper recordMapper;

    public GlobalResult checkRecord(String isbn, Integer userSid) {
        String msg ;
        //不能借同一本书
        List<Record> records = recordMapper.selectList(new LambdaQueryWrapper<Record>()
                .eq(Record::getRIsbn, isbn)
                .eq(Record::getRSid, userSid)
        );
        Optional<Record> record1 = records.stream().filter(record -> record.getIsReturn() == 0).findFirst();
        if (record1.isPresent()){
            msg="您已经借阅该书，且尚未归还";
            return GlobalResult.errorTokenMsg(msg);
        }
        //不能借超过五本
        List<Record> records1 = recordMapper.selectList(new LambdaQueryWrapper<Record>()
                .eq(Record::getRSid, userSid)
                .eq(Record::getIsReturn, 0));
        if (records1.size()>=5){
            msg="您已经借阅超过5本，请归还或部分归还再进行借阅";
            return GlobalResult.errorTokenMsg(msg);
        }
        //不能有过期未还
        Optional<Record> any = records1.stream().filter(record -> record.getIsOverdue() == 1).findAny();
        if (any.isPresent()) {
            msg="您有过期未归还的书，请检查后再操作";
            return GlobalResult.errorTokenMsg(msg);
        }
        return GlobalResult.ok();

    }

    public GlobalResult borrowBook(String isbn) {
        Book book = bookMapper.selectById(isbn);
        Integer bReserve = book.getBReserve();
        if (bReserve == 0){
            return GlobalResult.errorTokenMsg("库存为零");
        }else {
            book.setBReserve( bReserve- 1);
            bookMapper.updateById(book);
            return GlobalResult.ok();
        }


    }
}
