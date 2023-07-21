package com.example.demo.jpa;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统用户表
 * 
 * @author robert 2021年1月24日 下午3:41:13
 */
@Getter
@Setter
@Entity
@Table(name = SysUser.TABLE_NAME)
public class SysUser {

    public static final String TABLE_NAME="sys_user";

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

    // 密码
    private String password;

    // 用户名
	private String username;
    

}


