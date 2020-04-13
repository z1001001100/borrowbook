package xyz.zzd.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zzd.book.entity.Student;
import xyz.zzd.book.mapper.StudentMapper;

@Service
public class StudentInfoService {
    @Autowired
    StudentMapper studentMapper;

    //更新邮箱并返回结果
    public Student updateMail(Integer sid,String mail){
        Student student = studentMapper.selectById(sid);
        student.setSMail(mail);
        int i = studentMapper.updateById(student);
        if (i==1){
            return student;
        }else return null;
    }

    public Student updatePhone(Integer sid, String phone) {
        Student student = studentMapper.selectById(sid);
        student.setSPhone(phone);
        int i = studentMapper.updateById(student);
        if (i==1){
            return student;
        }else return null;
    }

    public Student updatePassword(Integer sid, String password) {
        Student student = studentMapper.selectById(sid);
        student.setSPassword(password);
        int i = studentMapper.updateById(student);
        if (i==1){
            return student;
        }else return null;
    }
}
