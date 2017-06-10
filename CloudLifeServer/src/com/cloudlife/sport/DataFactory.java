package com.cloudlife.sport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cloudlife.db.SqlDeal;
import com.cloudlife.sport.Individual.Sport;

/**
 * @brief 数据工厂类
 * @author wuyi
 *
 */
public class DataFactory {

	static private DataFactory m_fac;
	
	static public DataFactory getInstance() {
		if (m_fac == null)
			m_fac = new DataFactory();
		return m_fac;
	}
	
	// 运动的数据类
	private List<SportData> m_data = new ArrayList<SportData>();
	
	private DataFactory() {
		getData();
	}
	
	private boolean getData() {
		SqlDeal sql = new SqlDeal();
		PreparedStatement stmt = null;
		try {
			stmt = sql.getConnection().prepareStatement(
					"select *from resource_sport");
			ResultSet set = stmt.executeQuery();
			while (set.next()) {
				SportData tmp = new SportData(
						set.getString("name"), set.getFloat("k"), set.getString("place")
						, set.getInt("consult_time"));
				m_data.add(tmp);
			}
			set.close();
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sql.closeSql();
		}
		
		return true;
	}
	
	/**
	 * @brief 基因解码
	 * @param genes 基因
	 * @return
	 */
	public List<SportData> getDecode(Individual indiv) {
		List<Sport> genes = indiv.getGenes();
		List<SportData> retData = new ArrayList<SportData>();
		for (int i =0; i< genes.size(); ++i) {
			SportData sport = m_data.get(genes.get(i).index);
			sport.setTime(genes.get(i).time);
			retData.add(sport);
		}
		return retData;
	}
	
	public Individual createInitIndividual(UserModel model) {
		Individual indiv = new Individual();
		int totalTime = model.getTotalTime();
		int [] timeTotal = new int[model.getItemCount()];
		int rateTotal =0;
		for (int i =0; i<model.getItemCount();) {
			int index = (int) (Math.random()*this.m_data.size());
			if (indiv.getGenes().contains(index) ==false){
				indiv.add(index, 0);
				timeTotal[i] = m_data.get(index).getConsultTime();
				rateTotal += m_data.get(index).getConsultTime();
				 ++i;
			}
		}
		// 暂时采用比例求时间法来做
		for (int i =0; i< model.getItemCount(); ++i) {
			int time = (int) (timeTotal[i]*1.0/rateTotal * totalTime);
			indiv.modifyTime(i, time);
		}
		return indiv;
	}
	
	public int getRandomIndex() {
		return (int) (Math.random()*m_data.size());
	}
	
	public float getK(int index) {
		return this.m_data.get(index).getK();
	}
	
}














