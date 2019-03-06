package com.billetsdoux.webfluxcommon.controller;

import com.billetsdoux.webfluxcommon.bean.Student;
import com.billetsdoux.webfluxcommon.repository.StudentRepository;
import com.billetsdoux.webfluxcommon.util.NameValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 *
 */

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository repository;


    @Autowired
    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    // 一次性返回
    @GetMapping("/all")
    public Flux<Student> getAll(){

        return repository.findAll();
    }

    // 以sse形式实时性返回数据
    @GetMapping(value = "/sse/all",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Student> getSseAll(){

        return repository.findAll();
    }

    // 添加数据
    @PostMapping("/save")
    public Mono<Student> saveStudent(@Valid Student student){

        // 验证姓名的合法性
        NameValidateUtil.validateName(student.getName());
        return repository.save(student);
    }


    // 添加数据
    @PostMapping("/save2")
    public Mono<Student> saveStudent2(@Valid @RequestBody Student student){
        NameValidateUtil.validateName(student.getName());

        return repository.save(student);
    }

    // 无状态删除
    @DeleteMapping("/delcomm/{id}")
    public Mono<Void> deleteStudent(@PathVariable("id") String id){

        return repository.deleteById(id);
    }

    // 有状态删除
    // ResponseEntity 可以封装响应体中的数据及响应码
    // Mono的map()与flatMap()方法均可以用于做元素映射，选择标准是：一般情况下映射的过程
    // 需要做一些其他操作，需要使用flatMap();仅仅是元素的映射，而无需执行一些操作，则选择map()

    // 若一个方法没有返回值，但又需要让其具有返回值，则可以用then()方法，该方法的返回值将作为这个方法的返回值
    // defaultIfEmpty()执行的条件是，其Mono中若没有元素则执行
    @DeleteMapping("/del/{id}")
    public Mono<ResponseEntity<Void>> deleteStatusStudent(@PathVariable("id") String id){

        return repository.findById(id)
                .flatMap(student -> repository.delete(student)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 修改操作；若修改成功，则返回修改后的对象数据，若指定id对象不存在，则返回404
    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Student>> updateStudent(@PathVariable("id") String id,@Valid @RequestBody Student student){

        NameValidateUtil.validateName(student.getName());
        return repository.findById(id)
                .flatMap(stu -> {
                    stu.setName(student.getName());
                    stu.setAge(student.getAge());
                    return repository.save(stu);
                })
                .map(stu -> new ResponseEntity<>(stu, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 根据id查询,若查询到了，则返回查询对象数据及200；若指定的id对象不存在，则返回404
    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<Student>> findById(@PathVariable("id") String id){

        return repository.findById(id)
                .map(student -> new ResponseEntity<>(student,HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    // 根据年龄上下限进行查询 --- 一次性查询
    @GetMapping("/age/{below}/{top}")
    public Flux<Student> findStudentsByAge(@PathVariable("below") String below,@PathVariable("top") String top){

        return repository.findByAgeBetween(below,top);
    }


    // 根据年龄上下限进行查询 --- sse性查询
    @GetMapping(value = "/sse/age/{below}/{top}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Student> findStudentsByAgeSSE(@PathVariable("below") String below,@PathVariable("top") String top){

        return repository.findByAgeBetween(below,top);
    }

    @GetMapping("/query/age/{below}/{top}")
    public Flux<Student> queryByAge(@PathVariable("below") String below,@PathVariable("top") String top){

        return repository.queryByAge(below,top);
    }


    @GetMapping("/sse/query/age/{below}/{top}")
    public Flux<Student> queryByAgeSSE(@PathVariable("below") String below,@PathVariable("top") String top){

        return repository.queryByAge(below,top);
    }

}
