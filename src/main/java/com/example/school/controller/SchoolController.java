package com.example.school.controller;

import com.example.school.service.SchoolService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SchoolController {
 private final SchoolService service;
 public SchoolController(SchoolService service){this.service=service;}
 @GetMapping("/students") public Object students(){return service.students();}
 @PostMapping("/students") public Object addStudent(@RequestBody Map<String,Object>b){return service.addStudent(b);}
 @GetMapping("/courses") public Object courses(){return service.courses();}
 @PostMapping("/courses") public Object addCourse(@RequestBody Map<String,Object>b){return service.addCourse(b);}
 @GetMapping("/schedules") public Object schedules(){return service.schedules();}
 @PostMapping("/schedules") public Object addSchedule(@RequestBody Map<String,Object>b){return service.addSchedule(b);}
 @PostMapping("/enrollments") public Object enroll(@RequestBody Map<String,Object>b){return service.enroll(b);}
 @GetMapping("/enrollments/student/{studentId}") public Object enrollments(@PathVariable long studentId){return service.enrollments(studentId);}
 @PostMapping("/grades") public Object addGrade(@RequestBody Map<String,Object>b){return service.addGrade(b);}
 @PostMapping("/grades/{id}/publish") public Object publishGrade(@PathVariable long id){return service.publishGrade(id);}
 @GetMapping("/grades/student/{studentId}") public Object grades(@PathVariable long studentId){return service.publishedGrades(studentId);}
 @PostMapping("/leaves") public Object addLeave(@RequestBody Map<String,Object>b){return service.addLeave(b);}
 @GetMapping("/leaves") public Object leaves(){return service.leaves();}
 @PostMapping("/leaves/{id}/review") public Object reviewLeave(@PathVariable long id,@RequestBody Map<String,Object>b){return service.reviewLeave(id,b);}
 @PostMapping("/attendance/status") public Object attendance(@RequestBody Map<String,Object>b){return service.addAttendance(b);}
 @GetMapping("/attendance/student/{studentId}") public Object latestAttendance(@PathVariable long studentId){return service.latestAttendance(studentId);}
}
