package com.billetsdoux.webfluxcommon.repository;

import com.billetsdoux.webfluxcommon.bean.Student;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 *
 */
// 第一个泛型是该Repository操作的对象类型，第二个为主键类型
public interface StudentRepository extends ReactiveMongoRepository<Student,String> {
    /**
     *  根据年龄上下限
     * @param below 年龄下限（不包含次边界点）
     * @param top 年龄上限（不包含此边界点）
     * @return
     */
    Flux<Student> findByAgeBetween(String below, String top);

    /**
     *  使用MongoDB原始查询实现根据年龄上下限查询
     * @param below
     * @param top
     * @return
     */
    @Query("{'age' : { '$gte' : ?0, '$lt' : ?1}}")
    Flux<Student> queryByAge(String below,String top);
}
