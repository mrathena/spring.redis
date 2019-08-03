package com.mrathena.redis;

import lombok.*;

import java.io.Serializable;

/**
 * @author mrathena on 2019/8/2 17:21
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Data implements Serializable {
	private String string;
	private O o;
	private O o2;
	private byte a;
	private short b;
	private int c;
	private long d;
	private float e;
	private double f;
	private char g;
	private boolean h;
	private Byte a2;
	private Short b2;
	private Integer c2;
	private Long d2;
	private Float e2;
	private Double f2;
	private Character g2;
	private Boolean h2;
}

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class O implements Serializable {
	private String u;
	private String n;
}
