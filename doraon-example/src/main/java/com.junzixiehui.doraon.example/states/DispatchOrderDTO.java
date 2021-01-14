package com.junzixiehui.doraon.example.states;

import lombok.Builder;
import lombok.Data;

/**
 * <p>Description: </p>
 * @author: by jxll
 * @date: 2020/10/20  17:13
 * @version: 1.0
 */
@Data
@Builder
public class DispatchOrderDTO {

	private String orderNo;
	private String driverId;
}
