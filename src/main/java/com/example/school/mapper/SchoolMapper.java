package com.example.school.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface SchoolMapper {
 @Select("SELECT id, student_no AS studentNo, name, class_name AS className, email FROM students ORDER BY id")
 List<Map<String,Object>> students();
 @Insert("INSERT INTO students(student_no,name,class_name,email) VALUES(#{studentNo},#{name},#{className},#{email})")
 @Options(useGeneratedKeys=true,keyProperty="id") int addStudent(Map<String,Object> row);

 @Select("SELECT id, course_code AS courseCode, name, teacher, capacity, enrolled_count AS enrolledCount, credits FROM courses ORDER BY id")
 List<Map<String,Object>> courses();
 @Insert("INSERT INTO courses(course_code,name,teacher,capacity,enrolled_count,credits) VALUES(#{courseCode},#{name},#{teacher},#{capacity},0,#{credits})")
 @Options(useGeneratedKeys=true,keyProperty="id") int addCourse(Map<String,Object> row);
 @Update("UPDATE courses SET enrolled_count=enrolled_count+1 WHERE id=#{courseId} AND enrolled_count < capacity")
 int takeCourseSeat(long courseId);
 @Select("SELECT id FROM students WHERE id=#{id}") Long studentExists(long id);
 @Select("SELECT id FROM courses WHERE id=#{id}") Long courseExists(long id);
 @Select("SELECT COUNT(*) FROM enrollments WHERE student_id=#{studentId} AND course_id=#{courseId}")
 int enrollmentExists(@Param("studentId") long studentId,@Param("courseId") long courseId);
 @Insert("INSERT INTO enrollments(student_id,course_id,enrolled_at) VALUES(#{studentId},#{courseId},CURRENT_TIMESTAMP)")
 int enroll(@Param("studentId") long studentId,@Param("courseId") long courseId);
 @Select("SELECT e.id,e.student_id AS studentId,s.name AS studentName,e.course_id AS courseId,c.name AS courseName,e.enrolled_at AS enrolledAt FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE e.student_id=#{studentId} ORDER BY e.id")
 List<Map<String,Object>> enrollmentsByStudent(long studentId);

 @Select("SELECT id,course_id AS courseId,weekday,start_period AS startPeriod,end_period AS endPeriod,classroom FROM schedules ORDER BY weekday,start_period")
 List<Map<String,Object>> schedules();
 @Insert("INSERT INTO schedules(course_id,weekday,start_period,end_period,classroom) VALUES(#{courseId},#{weekday},#{startPeriod},#{endPeriod},#{classroom})")
 int addSchedule(Map<String,Object> row);

 @Insert("INSERT INTO grades(student_id,course_id,score,published) VALUES(#{studentId},#{courseId},#{score},FALSE)")
 @Options(useGeneratedKeys=true,keyProperty="id") int addGrade(Map<String,Object> row);
 @Update("UPDATE grades SET published=TRUE WHERE id=#{id}") int publishGrade(long id);
 @Select("SELECT g.id,g.course_id AS courseId,c.name AS courseName,g.score,g.published FROM grades g JOIN courses c ON c.id=g.course_id WHERE g.student_id=#{studentId} AND g.published=TRUE ORDER BY g.id")
 List<Map<String,Object>> publishedGrades(long studentId);

 @Insert("INSERT INTO leave_requests(student_id,reason,start_date,end_date,status,review_comment) VALUES(#{studentId},#{reason},#{startDate},#{endDate},'PENDING',NULL)")
 @Options(useGeneratedKeys=true,keyProperty="id") int addLeave(Map<String,Object> row);
 @Select("SELECT l.id,l.student_id AS studentId,s.name AS studentName,l.reason,l.start_date AS startDate,l.end_date AS endDate,l.status,l.review_comment AS reviewComment FROM leave_requests l JOIN students s ON s.id=l.student_id ORDER BY l.id DESC")
 List<Map<String,Object>> leaves();
 @Update("UPDATE leave_requests SET status=#{status},review_comment=#{comment} WHERE id=#{id} AND status='PENDING'")
 int reviewLeave(@Param("id") long id,@Param("status") String status,@Param("comment") String comment);

 @Insert("INSERT INTO attendance_status(student_id,status,location,remark,updated_at) VALUES(#{studentId},#{status},#{location},#{remark},CURRENT_TIMESTAMP)")
 int addAttendance(Map<String,Object> row);
 @Select("SELECT a.id,a.student_id AS studentId,s.name AS studentName,a.status,a.location,a.remark,a.updated_at AS updatedAt FROM attendance_status a JOIN students s ON s.id=a.student_id WHERE a.student_id=#{studentId} ORDER BY a.id DESC LIMIT 1")
 Map<String,Object> latestAttendance(long studentId);
}
