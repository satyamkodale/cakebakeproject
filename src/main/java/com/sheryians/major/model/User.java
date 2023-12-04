package com.sheryians.major.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Data;

// this class is created when we are staring authentication/security part 

@Data
@Entity
@Table(name="users")
public class User {

	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;
	
	@NotEmpty
	@Column(nullable=false)
	private String firstName;
	
	private String lastName;
	
	@Column(nullable=false,unique=true)
	@NotEmpty
	@Email(message="{errors.invlid_email}")
	private String email;
	
	
//	@NotEmpty if we are using google auth then no pass is filled
	private String password;
	
	
	// to connect with roles  --> admin/user
	@ManyToMany(cascade=CascadeType.MERGE,fetch=FetchType.EAGER)
	//cascade if one work done with one col then refrenced col in another col in another table should be affected
	//if user is deleted then all order related to user is deleted
	//one user can have multiple roles and one roles can have multiple users
	@JoinTable(
			name="user_role",
			joinColumns= {@JoinColumn(name="USER_ID",referencedColumnName="ID")},
			inverseJoinColumns= {@JoinColumn(name="ROLE_ID",referencedColumnName="ID")}
			)
	//merge the table based on primary key one one table and foregian key of another table 
	private List<Role> roles;
	
	public User(User user) 
	{
		this.firstName=user.getFirstName();
		this.lastName=user.getLastName();
		this.email=user.getEmail();
		this.password=user.getPassword();
		this.roles=user.getRoles();
		
	}
	
	

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + "]";
	}



	public User() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	
	
	
}
