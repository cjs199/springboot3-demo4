package com.example.demo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 继承了太多的类,所以提取一个公共类继承,其他类实现
 * 
 * @author robert
 *
 * @datetime 2020年12月25日 上午4:11:54
 */
@NoRepositoryBean // 标识这个不是Repository防止被JPA扫描出来创建层数据访问对象
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, // 提供了增删改查Repository
		JpaSpecificationExecutor<T> // 提供了多条件查询Repository
{

}
