package indi.ycl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import indi.ycl.model.Company;

public class CompanyDao {

	public static ArrayList<Integer> GetCompanyByIndustry() {

		ArrayList<Integer> companys = new ArrayList<>();
		Connection con = null;
		con = DbUtil.getCurrentConnection();
		PreparedStatement ps;
		try {
			// ps = con.prepareStatement("select id from company where cindustry
			// in (3,7)");
			// ps = con.prepareStatement("select id from company where
			// z_bigclass in (3,7)");
			 ps = con.prepareStatement(
					 "select id from company where industry=3");
//			 ps = con.prepareStatement(
//			 "select id from company where tonghua_bigclass in (5,7,8,10,11,12,15,16,18,19,22,24)");
//			ps = con.prepareStatement(
//					"select id from company where tonghua_bigclass in (1,2,8,9,14,19,21,22,23,29,30,32,39,40,41,47,48,49,55,56,63,66)");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				companys.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DbUtil.closeCurrentConnection();
		}
		return companys;
	}

	public Company GetCompanyById(int id) {
		Company com = new Company();
		Connection con = null;
		con = DbUtil.getCurrentConnection();
		PreparedStatement ps;
		try {
			// ps = con.prepareStatement("select id from company where cindustry
			// in (3,7)");
			// ps = con.prepareStatement("select id from company where
			// z_bigclass in (3,7)");
			ps = con.prepareStatement("select * from company where id=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				com.setId(rs.getInt("id"));
				com.setName(rs.getString("cname"));
				com.setTonghua_bigclass(rs.getInt("tonghua_bigclass"));
				com.setTonghua_smallclass(rs.getInt("tonghua_smallclass"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DbUtil.closeCurrentConnection();
		}
		return com;

	}

}
