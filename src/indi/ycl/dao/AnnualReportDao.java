package indi.ycl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import indi.ycl.model.Annual_Report;
import indi.ycl.util.CountChar;

public class AnnualReportDao {
	public ArrayList<String> getReportById(int id) {
		ArrayList<String> reports = new ArrayList<String>();

		Connection con = null;
		con = DbUtil.getCurrentConnection();
		PreparedStatement ps;
		Annual_Report qu;

		try {

			ps = con.prepareStatement("select * from creport where id=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				String content = rs.getString("content");
            
                reports.add(rs.getString("title"));
				int startIndex = content.indexOf("第三节 公司业务概要");
				startIndex = content.indexOf("第三节 公司业务概要", startIndex + 1);
				int endIndex = content.indexOf("第五节 重要事项");
				endIndex = content.indexOf("第五节 重要事项", endIndex + 1);
				System.out.println(startIndex + " " + endIndex);
				if(startIndex==-1||endIndex==-1){
					return null;
				}
				content = content.substring(startIndex, endIndex);
			

				String[] splitresult = content.split("\n");
				ArrayList<String> sentence = new ArrayList<String>();
				for (int i = 0; i < splitresult.length; i++) {

					if (splitresult[i].length() > 1) {
						sentence.add(splitresult[i].substring(0, splitresult[i].length() - 1));
					}
				}

				Boolean findSenBegin = false;
				String prepart = "";
				for (int i = 0; i < sentence.size(); i++) {
					String linenow = sentence.get(i);
					CountChar counter = new CountChar();
					counter.count(linenow);

					CountChar countNext = new CountChar();
					int taginNext = -1;
					if (i + 1 <= sentence.size() - 1) {
						countNext.count(sentence.get(i + 1));
						taginNext = sentence.get(i + 1).indexOf("√");
					}

				
					if (!findSenBegin) {
						if (counter.getEnCharacter() + counter.getNumberCharacter() + counter.getChCharacter() >= 25
								&& counter.getSpaceCharacter() <= 10 && countNext.getSpaceCharacter() <= 7
								&& taginNext == -1) {

							if (linenow.indexOf("。") > 0) {

								if (linenow.charAt(linenow.length() - 1) == '。') {
									reports.add(prepart + linenow);
									prepart = "";
									findSenBegin = false;

								} else {
									String[] tmp = linenow.split("。");
									for (int j = 0; j < tmp.length - 1; j++) {
										prepart += tmp[j];
									}
									reports.add(prepart + "。");
									prepart = tmp[tmp.length - 1];
									findSenBegin = true;

								}
							} else {
								findSenBegin = true;
								prepart = linenow;

							}

						} else {
							continue;

						}

					} else {
						if(linenow.indexOf("√")!=-1){
							prepart="";
							findSenBegin=false;
							
						}else{
							if (linenow.indexOf("。") > 0) {

								if (linenow.charAt(linenow.length() - 1) == '。') {
								
									reports.add(prepart + linenow);
									prepart = "";
									findSenBegin = false;
	
								} else {
									String[] tmp = linenow.split("。");
									for (int j = 0; j < tmp.length - 1; j++) {
										prepart += tmp[j];
									}
									
									reports.add(prepart + "。");
									prepart = tmp[tmp.length - 1];
								}
							}else {
								if(linenow.indexOf(" ")==0||linenow.indexOf("                                       ")==0){
									
								}else{
									prepart = prepart.concat(linenow);
								}
								
								
							}
						}
						
					}

				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		} finally {
			DbUtil.closeCurrentConnection();
		}

		return reports;

	}
	public boolean ExistReport(int id,ArrayList<Integer> companys) {
        boolean flag = false;
        Connection con = null;
        con = DbUtil.getCurrentConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("select * from creport where id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
              
                if(companys.contains(rs.getInt("company_id"))){
                	  flag = true;
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DbUtil.closeCurrentConnection();
        }
        return flag;
    }

	public static void main(String[] args) {
		new AnnualReportDao().getReportById(9103);

	}
	public String getReportTitleById(int id) {
		// TODO Auto-generated method stub
		String result="";
		Connection con = null;
		con = DbUtil.getCurrentConnection();
		PreparedStatement ps;
		try {
			// ps = con.prepareStatement("select id from company where cindustry
			// in (3,7)");
			// ps = con.prepareStatement("select id from company where
			// z_bigclass in (3,7)");
			ps = con.prepareStatement(
					"select title from creport where where id=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result=rs.getString("title");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DbUtil.closeCurrentConnection();
		}
		
		return result;
	}

}
