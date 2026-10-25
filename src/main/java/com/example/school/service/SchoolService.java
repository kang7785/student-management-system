package com.example.school.service;

import com.example.school.mapper.SchoolMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class SchoolService {
 private final SchoolMapper mapper;
 public SchoolService(SchoolMapper mapper) { this.mapper=mapper; }

 public List<Map<String,Object>> students(){return mapper.students();}
 public Map<String,Object> addStudent(Map<String,Object> b){required(b,"studentNo","name","className");mapper.addStudent(b);return b;}
 public List<Map<String,Object>> courses(){return mapper.courses();}
 public Map<String,Object> addCourse(Map<String,Object> b){
  required(b,"courseCode","name","teacher","capacity","credits");
  if(Integer.parseInt(b.get("capacity").toString())<1)throw new IllegalArgumentException("课程容量必须大于 0");
  mapper.addCourse(b);return b;
 }
 public List<Map<String,Object>> schedules(){return mapper.schedules();}
 public Map<String,Object> addSchedule(Map<String,Object> b){
  required(b,"courseId","weekday","startPeriod","endPeriod","classroom");
  if(mapper.courseExists(Long.parseLong(b.get("courseId").toString()))==null)throw new IllegalArgumentException("课程不存在");
  int day=Integer.parseInt(b.get("weekday").toString()), start=Integer.parseInt(b.get("startPeriod").toString()), end=Integer.parseInt(b.get("endPeriod").toString());
  if(day<1||day>7||start<1||end<start)throw new IllegalArgumentException("星期应为 1-7，节次必须有效");
  mapper.addSchedule(b);return b;
 }
 @Transactional
 public Map<String,Object> enroll(Map<String,Object> b){
  required(b,"studentId","courseId");long sid=Long.parseLong(b.get("studentId").toString()),cid=Long.parseLong(b.get("courseId").toString());
  if(mapper.studentExists(sid)==null)throw new IllegalArgumentException("学生不存在");
  if(mapper.courseExists(cid)==null)throw new IllegalArgumentException("课程不存在");
  if(mapper.enrollmentExists(sid,cid)>0)throw new IllegalArgumentException("请勿重复选课");
  if(mapper.takeCourseSeat(cid)==0)throw new IllegalArgumentException("课程名额已满");
  mapper.enroll(sid,cid);return Map.of("success",true,"message","选课成功");
 }
 public List<Map<String,Object>> enrollments(long id){return mapper.enrollmentsByStudent(id);}
 public Map<String,Object> addGrade(Map<String,Object>b){
  required(b,"studentId","courseId","score");double score=Double.parseDouble(b.get("score").toString());
  if(score<0||score>100)throw new IllegalArgumentException("成绩必须在 0 到 100 之间");
  mapper.addGrade(b);return Map.of("success",true,"message","成绩已录入，尚未发布");
 }
 public Map<String,Object> publishGrade(long id){if(mapper.publishGrade(id)==0)throw new IllegalArgumentException("成绩记录不存在");return Map.of("success",true,"message","成绩已发布");}
 public List<Map<String,Object>> publishedGrades(long id){return mapper.publishedGrades(id);}
 public Map<String,Object> addLeave(Map<String,Object>b){
  required(b,"studentId","reason","startDate","endDate");
  if(mapper.studentExists(Long.parseLong(b.get("studentId").toString()))==null)throw new IllegalArgumentException("学生不存在");
  mapper.addLeave(b);return Map.of("success",true,"message","请假申请已提交");
 }
 public List<Map<String,Object>> leaves(){return mapper.leaves();}
 public Map<String,Object> reviewLeave(long id,Map<String,Object>b){
  required(b,"status");String status=b.get("status").toString().toUpperCase();
  if(!List.of("APPROVED","REJECTED").contains(status))throw new IllegalArgumentException("status 只能是 APPROVED 或 REJECTED");
  if(mapper.reviewLeave(id,status,b.getOrDefault("comment","").toString())==0)throw new IllegalArgumentException("申请不存在或已审批");
  return Map.of("success",true,"message","审批完成");
 }
 public Map<String,Object> addAttendance(Map<String,Object>b){
  required(b,"studentId","status");String status=b.get("status").toString().toUpperCase();
  if(!List.of("ON_CAMPUS","OFF_CAMPUS","ON_LEAVE").contains(status))throw new IllegalArgumentException("status 只能是 ON_CAMPUS、OFF_CAMPUS、ON_LEAVE");
  if(mapper.studentExists(Long.parseLong(b.get("studentId").toString()))==null)throw new IllegalArgumentException("学生不存在");
  b.put("status",status);b.putIfAbsent("location","");b.putIfAbsent("remark","");
  mapper.addAttendance(b);return Map.of("success",true,"message","在校状态已记录");
 }
 public Map<String,Object> latestAttendance(long id){Map<String,Object> row=mapper.latestAttendance(id);if(row==null)throw new IllegalArgumentException("没有找到该学生的在校记录");return row;}
 private void required(Map<String,Object>b,String...keys){for(String k:keys)if(b.get(k)==null||b.get(k).toString().isBlank())throw new IllegalArgumentException("缺少必填字段："+k);}
}
