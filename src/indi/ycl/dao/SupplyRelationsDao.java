package indi.ycl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import indi.ycl.model.SupplyRelation;



public class SupplyRelationsDao {
	 public void InsertSupplyRelation(SupplyRelation re,int report_id) {
	        Connection con = null;
	        con = DbUtil.getCurrentConnection();
	        PreparedStatement ps;
	        try {
	            ps = con.prepareStatement("insert into supply_relationships(product,company,client,confidence_level,type,report_id) values(?,?,?,?,?,?)");
	            ps.setString(1, re.getProduct());
	            ps.setString(2, re.getCompany());
	            ps.setString(3, re.getClient());
	            ps.setInt(4, re.getConfidence_level());
	            ps.setInt(5, re.getType());
	            ps.setInt(6, report_id);
	            ps.execute();
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } finally {
	            DbUtil.closeCurrentConnection();
	        }
	    }

}
