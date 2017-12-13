package indi.ycl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CompanyDao {

	public ArrayList<Integer> GetCompanyByIndustry() {

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
					"select id from company where ctype in (1,9,39,40)");
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

}
