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
public class User implements Serializable {
	private Name name;
	private Integer age;
	private Boolean sex;
}

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class Name implements Serializable {
	private String username;
	private String nickname;
}
