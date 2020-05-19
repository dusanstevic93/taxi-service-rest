package com.dusan.taxiservice.entity;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

import com.dusan.taxiservice.entity.converter.GenderConverter;
import com.dusan.taxiservice.entity.enums.Gender;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_info")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements Persistable<String> {
	
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Transient
    boolean isNew;
    
	@Id
	private String username;
	
	private String password;
	
	protected String firstName;
	
	private String lastName;
	
	@Convert(converter = GenderConverter.class)
	private Gender gender;
	
	private String phone;
	
	private String email;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private UserRole role;
	
	public User(boolean isNew) {
	    this.isNew = isNew;
	}

    @Override
    public String getId() {
        return username;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
