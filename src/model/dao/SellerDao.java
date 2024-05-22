package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	
	void insert(Seller obj);
	void update(Seller obj);
	void deleteById(Seller obj);
	List<Seller> findAll();
	List<Seller> findByDepartment(Department department);
	Seller findById(Integer id);
	void deleteById(Integer id);

}
