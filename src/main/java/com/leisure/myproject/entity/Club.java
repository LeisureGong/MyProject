package com.leisure.myproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gonglei
 * @date 2020/3/30 15:55
 */
@Data
@AllArgsConstructor
public class Club implements Serializable {

	private int id;

	private String name;

	private String info;

	private Date createDate;

	private int rank;

}
