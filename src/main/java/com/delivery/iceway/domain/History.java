package com.delivery.iceway.domain;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class History {
	private int id;
	private String name;
	private boolean delivery_status;
	private boolean recall_status;
	private Timestamp delivery_time;
	private Timestamp arrival_time;
	private Timestamp recall_time;
	private String dateString;
	private int startRowNum;
	private int endRowNum;
	private int totalRow;
}