package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;


public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn)
	{
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
	       PreparedStatement st = null;
	       try {
	    	   st = conn.prepareStatement(
	    			     "INSERT INTO department "
	    			     + "(Name) "
	    			     + "VALUES "
	    			     + "(?)", Statement.RETURN_GENERATED_KEYS);
	    	   
	    	   st.setString(1, obj.getName());
		       
		       int rowsAffected = st.executeUpdate();
		       
		       if(rowsAffected > 0)
		       {
		    	ResultSet rs = st.getGeneratedKeys();   
		       	if (rs.next())
		       	{
		       	  	int id = rs.getInt(1);
		       	  	obj.setId(id);
		       	}
		       	 DB.closeResultSet(rs);
		       }
		       else {
		    	   throw new DbException("Unexpected error! No rows affected!");
		       }
		}catch (SQLException e)
	       {
			throw new DbException(e.getMessage());
	       }
	       finally {
	    	   DB.closeStatement(st);
	       }
		
	}

	@Override
	public void update(Department obj) {
		  PreparedStatement st = null;
	       try {
	    	   st = conn.prepareStatement(
	    			       " UPDATE Department "
	    			     + " SET Name = ? "
	    			     + " WHERE Id = ? ", Statement.RETURN_GENERATED_KEYS);
	    	   
	    	   st.setString(1, obj.getName());
		       st.setInt(2,  obj.getId());
		       
		        st.executeUpdate();
		       
		}catch (SQLException e)
	       {
			throw new DbException(e.getMessage());
	       }
	       finally {
	    	   DB.closeStatement(st);
	       }
		
	}

	@Override
	public void deleteById(Integer id) {
		 PreparedStatement st = null;
	       try {
	    	    st = conn.prepareStatement("DELETE FROM Department WHERE id = ?");
	    	   
		        st.setInt(1,id);
		        
		        int rows = st.executeUpdate();
		        
		        if(rows == 0)
		        {
		        	throw new DbException("The Department Associated with This Id Doesent Exists");
		        }
		       
		        st.executeUpdate();
		       
		}catch (SQLException e)
	       {
			throw new DbException(e.getMessage());
	       }
	       finally {
	    	   DB.closeStatement(st);
	       }    
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			 st = conn.prepareStatement(
					   "SELECT department.Id as DepId ,department.Name as DepName "
					   + " From department "
					   + " WHERE department.Id = ? "
					 ); 
			 
			 st.setInt(1, id);
			 rs = st.executeQuery();
			 if  (rs.next())
			 {
				Department obj = new Department();
				obj.setId(rs.getInt("DepId"));
				obj.setName(rs.getString("DepName"));
				return obj;
			 }
			 return null;

		}
		catch(SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	/*private Department instantiateDepartment(ResultSet rs) throws SQLException {
		 Department dep = new Department();
		 dep.setId(rs.getInt("DepId"));
		 dep.setName(rs.getString("DepName"));
		 return dep;
	}*/
	
	//https://github.com/acenelio/demo-dao-jdbc/blob/master/src/model/dao/impl/DepartmentDaoJDBC.java


	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			 st = conn.prepareStatement(
					   "SELECT department.Id as DepartmentId,department.Name as DepName "
					   + " FROM department "
					   + " ORDER BY DepName "
					 ); 
			 
			 rs = st.executeQuery();
			 List<Department> list = new ArrayList<>();
			 
			 while  (rs.next())
			 {
				 Department obj = new Department();
				 obj.setId(rs.getInt("DepartmentId"));
				 obj.setName(rs.getString("DepName"));
				 list.add(obj);
			 }
			 return list;

		}
		catch(SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public void deleteById(Department obj) {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public List<Department> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			 st = conn.prepareStatement(
					   "SELECT department.Id as DepId, department.Name as DepName "
					   + "FROM department "
					   + "WHERE Name = ? "
					   + "ORDER BY Name"
					 ); 
			 
			 st.setInt(1, department.getId());
			 rs = st.executeQuery();
			 List<Department> list = new ArrayList<>();
			 Map<Integer, Department> map = new HashMap<>();
			 
			 while  (rs.next())
			 {
				Department dep = map.get(rs.getInt("DepId"));
				 
				 if(dep == null)
				 {
					dep  = instantiateDepartment(rs);
					map.put(rs.getInt("DepId"), dep);
				 }
				  
				 Department obj = instantiateDepartment(rs);
				 list.add(obj);
			 }
			 return list;

		}
		catch(SQLException e)
		{
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}*/

}
